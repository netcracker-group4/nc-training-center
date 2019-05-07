package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.*;
import ua.com.nc.service.ReportService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Log4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private
    ILessonDao iLessonDao;
    @Autowired
    private
    IUserDao iUserDao;
    @Autowired
    private
    IGroupDao iGroupDao;
    @Autowired
    private
    ILevelDao iLevelDao;
    @Autowired
    private
    ICourseDao iCourseDao;


    private Level findLevelById(List<Level> levels, int id) {
        for (Level level : levels) {
            if (level.getId() == id) {
                return level;
            }
        }
        return null;
    }

    private CellStyle headerStyle(XSSFWorkbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }


    @Override
    public ByteArrayInputStream getAttendanceExcel() throws IOException {

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();
/*
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFont(headerFont);
 */
            CellStyle headerStyle = headerStyle(workbook);

            List<User> trainers = iUserDao.getAllTrainers();
            for (User trainer : trainers) {
                List<Group> groups = iGroupDao.getGroupByTrainerId(trainer.getId());
                for (Group group : groups) {
                    List<User> students = iUserDao.getByGroupId(group.getId());
                    //foreach trainer's group create sheet with title
                    Sheet groupSheet = workbook.createSheet(trainer.getLastName()
                            + "'s " + group.getTitle());

                    int rowCount = 0;
                    Row headerRow = groupSheet.createRow(rowCount);
                    for (User student : students) {
                        List<Lesson> lessons = iLessonDao.getByGroupIdAndUserId(group.getId(), student.getId());

                        Row studentRow = groupSheet.createRow(++rowCount);
                        int cellCount = 0;
                        groupSheet.setColumnWidth(cellCount, 255 * 18);
                        studentRow.createCell(cellCount).setCellValue(student.getLastName()
                                + " " + student.getFirstName());
                        for (Lesson lesson : lessons) {
                            Cell headerCell = headerRow.createCell(++cellCount);
                            headerCell.setCellStyle(headerStyle);
                            headerCell.setCellValue(lesson.getTopic() + " " + lesson.getTimeDate());

                            studentRow.createCell(cellCount).setCellValue(lesson.getAbsenceStatus());

                            groupSheet.setColumnWidth(cellCount,
                                    255 * (lesson.getTopic() + " " + lesson.getTimeDate()).length());
                        }
                    }
                }
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ByteArrayInputStream getDashboardExcel() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet levelAndQuantity = workbook.createSheet("Level And Quantity");
            int levelRowCount = 0;
            for (Level level : iLevelDao.getAll()) {
                Row levelRowColumn = levelAndQuantity.createRow(levelRowCount++);
                int levelCellCount = 0;
                levelRowColumn.createCell(levelCellCount++).setCellValue(level.getTitle());
                for (Course course : iCourseDao.getAllByLevel(level.getId())) {
                    Row courseRowColumn = levelAndQuantity.createRow(levelRowCount++);
                    courseRowColumn.createCell(levelCellCount++).setCellValue(course.getName());
                    for (Group group : iGroupDao.getAllGroupsOfCourse(course.getId())) {
                        Row groupRowColumn = levelAndQuantity.createRow(levelRowCount++);
                        groupRowColumn.createCell(levelCellCount).setCellValue(group.getTitle());
                    }
                }
            }

            Sheet levelAndTrainers = workbook.createSheet("Level And Trainers");
            int trainerRowCount = 0;
            for (User trainer : iUserDao.getAllTrainers()) {
                Row trainerRowColumn = levelAndTrainers.createRow(trainerRowCount++);
                int trainerCellCount = 0;
                trainerRowColumn.createCell(trainerCellCount++).setCellValue(trainer.getFirstName()
                                                                            + " " + trainer.getLastName());

                for (Course course : iCourseDao.getAllByTrainer(trainer.getId())) {
                    Level level = findLevelById(iLevelDao.getAllByTrainer(trainer.getId()), course.getLevel());

                    Row courseRowColumn = levelAndTrainers.createRow(trainerRowCount++);
                    if(level.getTitle() != null){
                        courseRowColumn.createCell(trainerCellCount).setCellValue(course.getName()
                                + " " + level.getTitle());
                    } else {
                        courseRowColumn.createCell(trainerCellCount).setCellValue(course.getName());
                    }
                }
            }

            Sheet trainingAndQuantity = workbook.createSheet("Training And Quantity");
            String[] columns = {"Course Name", "Group Name", "Amount of Employees"};
            Row headerRow = trainingAndQuantity.createRow(0);
            for (int i = 0; i < columns.length; i++){
                headerRow.createCell(i).setCellValue(columns[i]);
            }
            int rowCounter = 1;
            int[] courseCell = {0,2};
            int[] groupCell = {1,2};
            for (Course course : iCourseDao.getAll()) {
                Row courseRow = trainingAndQuantity.createRow(rowCounter++);
                courseRow.createCell(courseCell[0]).setCellValue(course.getName());
                int numberOfEmployeesInCourse = 0;
                Cell employeesAmountCourse = courseRow.createCell(courseCell[1]);
                for (Group group : iGroupDao.getAllGroupsOfCourse(course.getId())) {
                    int numberOfEmployeesInGroup = iGroupDao.getNumberOfEmployeesInGroup(group.getId());
                    numberOfEmployeesInCourse += numberOfEmployeesInGroup;
                    Row groupRow = trainingAndQuantity.createRow(rowCounter++);
                    groupRow.createCell(groupCell[0]).setCellValue(group.getTitle());
                    groupRow.createCell(groupCell[1]).setCellValue(numberOfEmployeesInGroup);
                }
                employeesAmountCourse.setCellValue(numberOfEmployeesInCourse);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}