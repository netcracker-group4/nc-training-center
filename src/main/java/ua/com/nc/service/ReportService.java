package ua.com.nc.service;

import ua.com.nc.domain.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Report interface provides creating excel reports
 */
public interface ReportService {
    /**
     * Some constants using in reports
     */
    String[] dashboardSheetNames = {"Level And Quantity", "Level And Trainers", "Training And Quantity"};
    String[] levelAndQuantityColumns = {"Level", "Course Name", "Group Name"};
    String[] levelAndTrainersColumns = {"Trainer", "Course Name and Level"};
    String[] trainingAndQuantityColumns = {"Course Name", "Group Name", "Amount of Employees"};
    String groupsNotFound = "No groups to report";
    String levelException = "Can't find Level for Id ";

    /**
     * @return full attendance report of all courses and their groups
     * @throws IOException if any exception during XSSFWorkbook.write()
     * @see org.apache.poi.xssf.usermodel.XSSFWorkbook
     */
    ByteArrayInputStream getAttendanceExcel() throws IOException;

    /**
     * @return attendance report of all groups of current user
     * @param user user object, to get user information
     * @throws IOException if any exception during XSSFWorkbook.write()
     * @see org.apache.poi.xssf.usermodel.XSSFWorkbook
     */
    ByteArrayInputStream getAttendanceExcel(User user) throws IOException;

    /**
     * @return attendance report of particular group
     * @param groupId id, to find group in database
     * @throws IOException if any exception during XSSFWorkbook.write()
     * @see org.apache.poi.xssf.usermodel.XSSFWorkbook
     */
    ByteArrayInputStream getAttendanceExcel(Integer groupId) throws IOException;

    /**
     * @return dashboard report of all courses, their groups, and users
     * @throws IOException if any exception during XSSFWorkbook.write()
     * @see org.apache.poi.xssf.usermodel.XSSFWorkbook
     */
    ByteArrayInputStream getDashboardExcel() throws IOException;


}
