package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.*;
import ua.com.nc.exceptions.NoSuchUserException;
import ua.com.nc.service.ReportService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ReportService
 */
@Log4j2
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private AttendanceDao attendanceDao;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private LevelDao levelDao;
    @Autowired
    private CourseDao courseDao;

    private String[] dashboardSheetNames = {"Level And Quantity", "Level And Trainers", "Training And Quantity"};
    private String[] levelAndQuantityColumns = {"Level", "Course Name", "Group Name"};
    private String[] levelAndTrainersColumns = {"Trainer", "Course Name and Level"};
    private String[] trainingAndQuantityColumns = {"Course Name", "Group Name", "Amount of Employees"};


    //get full attendance report
    @Override
    public ByteArrayInputStream getAttendanceExcel() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            List<Group> groups = groupDao.getAll();
            if (!groups.isEmpty()) {
                drawGroupsPage(workbook, headerStyle(workbook), presenceStyle(workbook), groups);
                workbook.write(out);
                return new ByteArrayInputStream(out.toByteArray());
            } else {
                throw new IOException("No groups to report");
            }
        }
    }

    //get attendance of trainers each group
    @Override
    public ByteArrayInputStream getAttendanceExcel(User trainer) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            List<Group> groups = groupDao.getGroupByTrainerId(trainer.getId());
            drawTrainersGroups(workbook, headerStyle(workbook), presenceStyle(workbook), trainer, groups);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    //get attendance of group by id
    @Override
    public ByteArrayInputStream getAttendanceExcel(Integer groupId) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            List<Group> groups = new ArrayList<>();
            groups.add(groupDao.getEntityById(groupId));
            User trainer = userDao.getTrainerByCourseId(groupId);

            drawTrainersGroups(workbook, headerStyle(workbook), presenceStyle(workbook), trainer, groups);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    //get dashboard report
    @Override
    public ByteArrayInputStream getDashboardExcel() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            drawLevelAndQuantity(createSheetAndHeader(
                    workbook,
                    dashboardSheetNames[0],
                    levelAndQuantityColumns));

            drawLevelAndTrainers(createSheetAndHeader(
                    workbook,
                    dashboardSheetNames[1],
                    levelAndTrainersColumns));

            drawTrainingAndQuantity(createSheetAndHeader(
                    workbook,
                    dashboardSheetNames[2],
                    trainingAndQuantityColumns));
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void drawTrainersGroups(XSSFWorkbook workbook, CellStyle headerStyle,
                                    CellStyle presenceStyle, User trainer, List<Group> groups) {
        for (Group group : groups) {
            //foreach trainer's group create sheet with title
            Sheet groupSheet = workbook.createSheet(trainer.getLastName()
                    + "'s " + group.getTitle());
            int rowCount = 0;
            Row headerRow = groupSheet.createRow(rowCount);
            drawGroupSheet(headerStyle, presenceStyle, group, groupSheet, rowCount, headerRow);
        }
    }

    private void drawGroupsPage(XSSFWorkbook workbook, CellStyle headerStyle,
                                CellStyle presenceStyle, List<Group> groups) {
        for (Group group : groups) {
            //foreach group create sheet with title
            Sheet groupSheet = workbook.createSheet(group.getTitle());
            int rowCount = 0;
            Row headerRow = groupSheet.createRow(rowCount);
            drawGroupSheet(headerStyle, presenceStyle, group, groupSheet, rowCount, headerRow);
        }
    }

    private void drawGroupSheet(CellStyle headerStyle, CellStyle presenceStyle, Group group,
                                Sheet groupSheet, int rowCount, Row headerRow) {
        for (User student : userDao.getByGroupId(group.getId())) {
            rowCount++;
            int cellCount = 0;
            String studentFullName = student.getFirstName() + " " + student.getLastName();
            Row studentRow = groupSheet.createRow(rowCount);
            writeCell(headerStyle, studentRow, cellCount, studentFullName);
            groupSheet.autoSizeColumn(cellCount);
            writeAttendance(headerStyle, presenceStyle, group,
                    groupSheet, headerRow, student, cellCount, studentRow);
        }
    }

    private void writeAttendance(CellStyle headerStyle, CellStyle presenceStyle,
                                 Group group, Sheet groupSheet, Row headerRow,
                                 User student, int cellCount, Row studentRow) {
        for (Attendance attendance :
                attendanceDao.getAttendanceByStudentIdAndGroupId(
                        student.getId(), group.getId())) {
            Lesson lesson = lessonDao.getEntityById(attendance.getLessonId());
            String lessonTopicAndDate = lesson.getTopic() + "\n" + lesson.getTimeDate();
            cellCount++;
            writeStudentsAttendance(headerStyle, presenceStyle,
                    groupSheet, headerRow, studentRow, cellCount,
                    attendance.getStatus(), lessonTopicAndDate);
        }
    }

    private void writeStudentsAttendance(CellStyle headerStyle, CellStyle presenceStyle,
                                         Sheet groupSheet, Row headerRow, Row studentRow,
                                         int cellCount, String studentsAbsenceStatus,
                                         String lessonTopicAndDate) {
        writeCell(headerStyle, headerRow, cellCount, lessonTopicAndDate);
        writeCell(presenceStyle, studentRow, cellCount, studentsAbsenceStatus);
        groupSheet.autoSizeColumn(cellCount);
    }


    private Sheet createSheetAndHeader(XSSFWorkbook workbook, String dashboardName,
                                       String[] trainingAndQuantityColumns) {
        Sheet sheetName = workbook.createSheet(dashboardName);
        Row headerRow = sheetName.createRow(0);
        for (int i = 0; i < trainingAndQuantityColumns.length; i++) {
            headerRow.setRowStyle(headerStyle(workbook));
            headerRow.createCell(i).setCellValue(trainingAndQuantityColumns[i]);
        }
        return sheetName;
    }

    private void drawLevelAndQuantity(Sheet sheet) {

        for (Level level : levelDao.getAll()) {
            Row levelRowColumn = sheet
                    .createRow(sheet.getLastRowNum() + 1);
            writeCell(headerStyle((XSSFWorkbook) sheet.getWorkbook()),
                    levelRowColumn, levelRowColumn.getLastCellNum() + 1, level.getTitle());
            sheet
                    .autoSizeColumn(levelRowColumn.getLastCellNum());

            writeCourseQuantity(sheet, level);
        }
    }

    private void writeCourseQuantity(Sheet sheet, Level level) {
        for (Course course : courseDao.getAllByLevel(level.getId())) {
            Row courseRowColumn = sheet
                    .createRow(sheet.getLastRowNum() + 1);
            writeCell(presenceStyle((XSSFWorkbook) sheet.getWorkbook()),
                    courseRowColumn, courseRowColumn.getLastCellNum() + 2, course.getName());
            sheet
                    .autoSizeColumn(courseRowColumn.getLastCellNum());

            writeGroupsQuantity(sheet, course);
        }
    }

    private void writeGroupsQuantity(Sheet sheet, Course course) {
        for (Group group : groupDao.getAllGroupsOfCourse(course.getId())) {
            Row groupRowColumn = sheet
                    .createRow(sheet.getLastRowNum() + 1);
            writeCell(presenceStyle((XSSFWorkbook) sheet.getWorkbook()),
                    groupRowColumn, groupRowColumn.getLastCellNum() + 3, group.getTitle());
            sheet
                    .autoSizeColumn(groupRowColumn.getLastCellNum());
        }
    }


    private void drawLevelAndTrainers(Sheet sheet) {
        int trainerRowCount = 1;
        for (User trainer : userDao.getAllTrainers()) {
            int trainerCellCount = 0;
            writeCell(presenceStyle((XSSFWorkbook) sheet.getWorkbook()),
                    sheet.createRow(trainerRowCount++), trainerCellCount++,
                    trainer.getFirstName() + " " + trainer.getLastName());
            sheet.autoSizeColumn(trainerCellCount);
            for (Course course : courseDao.getAllByTrainer(trainer.getId())) {
                Level level = findLevelById(levelDao.getAllByTrainer(trainer.getId()), course.getLevel());
                writeCell(presenceStyle((XSSFWorkbook) sheet.getWorkbook()),
                        sheet.createRow(trainerRowCount++),
                        trainerCellCount,
                        course.getName() + " " + level.getTitle());
                sheet.autoSizeColumn(trainerCellCount);
            }
        }
    }

    //
    private void drawTrainingAndQuantity(Sheet sheet) {

        //for each course creating row, writing name
        for (Course course : courseDao.getAll()) {
            int numberOfEmployeesInCourse = 0;
            Row courseRow = sheet.createRow(sheet.getLastRowNum() + 1);
            writeCell(presenceStyle((XSSFWorkbook) sheet.getWorkbook()),
                    courseRow, courseRow.getLastCellNum() + 1, course.getName());
            sheet.autoSizeColumn(courseRow.getFirstCellNum());

            writeEmployeesAmount(sheet, course, numberOfEmployeesInCourse, courseRow);
        }

    }

    private void writeEmployeesAmount(Sheet sheet, Course course, int numberOfEmployeesInCourse, Row courseRow) {
        Cell employeesAmountCourse = courseRow.createCell(courseRow.getLastCellNum() + 1);
        //Write groups and amount of employees
        for (Group group : groupDao.getAllGroupsOfCourse(course.getId())) {
            int numberOfEmployeesInGroup = groupDao.getNumberOfEmployeesInGroup(group.getId());
            numberOfEmployeesInCourse += numberOfEmployeesInGroup;

            Row groupRow = sheet.createRow(sheet.getLastRowNum() + 1);
            writeCell(presenceStyle((XSSFWorkbook) sheet.getWorkbook()), groupRow,
                    groupRow.getLastCellNum() + 2, group.getTitle());
            writeCell(presenceStyle((XSSFWorkbook) sheet.getWorkbook()),
                    groupRow, numberOfEmployeesInGroup);

            sheet.autoSizeColumn(groupRow.getFirstCellNum());
            sheet.autoSizeColumn(employeesAmountCourse.getColumnIndex());
        }
        employeesAmountCourse.setCellValue(numberOfEmployeesInCourse);
    }

    private Level findLevelById(List<Level> levels, int id) {
        for (Level level : levels) {
            if (level.getId() == id) {
                return level;
            }
        }
        throw new NoSuchUserException("Can't find Level for Id " + id);
    }

    private CellStyle headerStyle(XSSFWorkbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        return setCellStyle(workbook, headerFont);
    }

    private CellStyle presenceStyle(XSSFWorkbook workbook) {
        Font presenceFont = workbook.createFont();
        presenceFont.setItalic(true);
        return setCellStyle(workbook, presenceFont);
    }

    private CellStyle setCellStyle(XSSFWorkbook workbook, Font presenceFont) {
        CellStyle presenceStyle = workbook.createCellStyle();
        presenceStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        presenceStyle.setAlignment(HorizontalAlignment.CENTER);
        presenceStyle.setWrapText(true);
        presenceStyle.setFont(presenceFont);
        return presenceStyle;
    }

    private void writeCell(CellStyle cellStyle, Row row, int cellCount, String content) {
        Cell cell = row.createCell(cellCount);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(content);
    }

    private void writeCell(CellStyle cellStyle, Row row, int content) {
        Cell cell = row.createCell(2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(content);
    }
}