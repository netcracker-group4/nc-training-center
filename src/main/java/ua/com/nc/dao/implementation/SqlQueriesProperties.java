package ua.com.nc.dao.implementation;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@SuppressWarnings("unused")
@Configuration
@PropertySource("classpath:sql_queries.properties")
@Deprecated
@Data
public class SqlQueriesProperties {
    @Value("${usr.select-all}")
    private String usrSelectAll;
    @Value("${usr.select-by-id}")
    private String usrSelectById;
    @Value("${usr.select-by-email}")
    private String usrSelectByEmail;
    @Value("${usr.update}")
    private String usrUpdate;
    @Value("${usr.delete}")
    private String usrDelete;
    @Value("${usr.insert}")
    private String usrInsert;
    @Value("${usr.select-all-trainers}")
    private String usrSelectAllTrainers;
    @Value("${usr.select-all-managers}")
    private String usrSelectAllManagers;
    @Value("${usr.select-by-group-id}")
    private String usrSelectByGroupId;
    @Value("${usr.select-on-landing-page}")
    private String usrLandingPage;
    @Value("${usr.select-all-trainers-by-id}")
    private String usrSelectAllTrainersById;
    @Value("${usr.select-manager-by-id}")
    private String usrSelectManagerById;
    @Value("${usr.update-landing-page}")
    private String usrUpdateLandingPage;
    @Value("${usr.update-usr-by-admin}")
    private String usrUpdateUsrByAdmin;
    @Value("${usr.update-change-active}")
    private String usrUpdateChangeActive;
    @Value("${usr.select-ungrouped-by-course-id}")
    private String usrSelectUngroupedByCourseId;

    @Value("${course.select-all}")
    private String courseSelectAll;
    @Value("${course.select-by-id}")
    private String courseSelectById;
    @Value("${course.select-by-level}")
    private String courseSelectByLevel;
    @Value("${course.select-by-trainer}")
    private String courseSelectByTrainer;
    @Value("${course.update}")
    private String courseUpdate;
    @Value("${course.delete}")
    private String courseDelete;
    @Value("${course.insert}")
    private String courseInsert;
    @Value("${course.select-on-landing-page}")
    private String courseLandingPage;
    @Value("${course.update-landing-page}")
    private String courseUpdateLandingPage;

    @Value("${group.select-all}")
    private String groupSelectAll;
    @Value("${group.select-by-id}")
    private String groupSelectById;
    @Value("${group.update}")
    private String groupUpdate;
    @Value("${group.delete}")
    private String groupDelete;
    @Value("${group.insert}")
    private String groupInsert;
    @Value("${group.select-by-course}")
    private String groupSelectByCourse;
    @Value("${group.select-number-of-employees}")
    private String groupSelectNumberOfEmployees;
    @Value("${group.select-by-employee}")
    private String groupSelectByEmployee;
    @Value("${group.select-by-trainer-id}")
    private String groupSelectByTrainerId;

    //
    @Value("${usr_group.delete-all-for-group}")
    private String userGroupDeleteForGroup;
    @Value("${usr_group.delete-all-for-user}")
    private String userGroupDeleteForUser;
    @Value("${usr_group.select-by-usr-and-course}")
    private String userGroupSelectByUsrAndCourse;
    @Value("${usr_group.insert}")
    private String userGroupInsert;
    @Value("${usr_group.update}")
    private String userGroupUpdate;
    @Value("${usr_group.select-by-group}")
    private String userGroupSelectByGroup;

    @Value("${usr_group.select-by-usr-and-group}")
    private String userGroupSelectAttendanceByUsrAndGroup;

    @Value("${level.select-all}")
    private String levelSelectAll;
    @Value("${level.select-by-id}")
    private String levelSelectById;
    @Value("${level.select-by-name}")
    private String levelSelectByName;
    @Value("${level.update}")
    private String levelUpdate;
    @Value("${level.delete}")
    private String levelDelete;
    @Value("${level.insert}")
    private String levelInsert;
    @Value("${level.select-by-trainer}")
    private String levelSelectByTrainer;

    @Value("${absence_reason.select-all}")
    private String absenceReasonSelectAll;
    @Value("${absence_reason.select-by-id}")
    private String absenceReasonSelectById;
    @Value("${absence_reason.update}")
    private String absenceReasonUpdate;
    @Value("${absence_reason.delete}")
    private String absenceReasonDelete;
    @Value("${absence_reason.insert}")
    private String absenceReasonInsert;

    @Value("${desirable.schedule.select-all}")
    private String desirableScheduleSelectAll;
    @Value("${desirable.schedule.insert}")
    private String desirableScheduleInsert;
    @Value("${desirable.schedule.select-by-course-id}")
    private String desirableScheduleSelectByCourseId;

    @Value("${suitability.select-all}")
    private String suitabilitySelectAll;
}
