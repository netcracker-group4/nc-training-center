package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@SuppressWarnings("unused")
@Configuration
@PropertySource("classpath:sql_queries.properties")
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

    String getUsrSelectAll() {
        return usrSelectAll;
    }

    void setUsrSelectAll(String usrSelectAll) {
        this.usrSelectAll = usrSelectAll;
    }

    String getUsrSelectById() {
        return usrSelectById;
    }

    void setUsrSelectById(String usrSelectById) {
        this.usrSelectById = usrSelectById;
    }

    String getUsrSelectByEmail() {
        return usrSelectByEmail;
    }

    void setUsrSelectByEmail(String usrSelectByEmail) {
        this.usrSelectByEmail = usrSelectByEmail;
    }

    String getUsrUpdate() {
        return usrUpdate;
    }

    void setUsrUpdate(String usrUpdate) {
        this.usrUpdate = usrUpdate;
    }

    String getUsrDelete() {
        return usrDelete;
    }

    void setUsrDelete(String usrDelete) {
        this.usrDelete = usrDelete;
    }

    String getUsrInsert() {
        return usrInsert;
    }

    void setUsrInsert(String usrInsert) {
        this.usrInsert = usrInsert;
    }

    String getUsrLandingPage() {
        return usrLandingPage;
    }

    void setUsrLandingPage(String usrLandingPage) {
        this.usrLandingPage = usrLandingPage;
    }

    String getUsrUpdateLandingPage() {
        return usrUpdateLandingPage;
    }

    void setUsrUpdateLandingPage(String usrUpdateLandingPage) {
        this.usrUpdateLandingPage = usrUpdateLandingPage;
    }


    String getCourseSelectAll() {
        return courseSelectAll;
    }

    void setCourseSelectAll(String courseSelectAll) {
        this.courseSelectAll = courseSelectAll;
    }

    String getCourseSelectById() {
        return courseSelectById;
    }

    void setCourseSelectById(String courseSelectById) {
        this.courseSelectById = courseSelectById;
    }

    String getCourseUpdate() {
        return courseUpdate;
    }

    void setCourseUpdate(String courseUpdate) {
        this.courseUpdate = courseUpdate;
    }

    String getCourseDelete() {
        return courseDelete;
    }

    void setCourseDelete(String courseDelete) {
        this.courseDelete = courseDelete;
    }

    String getCourseInsert() {
        return courseInsert;
    }

    void setCourseInsert(String courseInsert) {
        this.courseInsert = courseInsert;
    }

    String getCourseLandingPage() {
        return courseLandingPage;
    }

    void setCourseLandingPage(String courseLandingPage) {
        this.courseLandingPage = courseLandingPage;
    }

    String getCourseUpdateLandingPage() {
        return courseUpdateLandingPage;
    }

    void setCourseUpdateLandingPage(String updateDropFromLandingPage) {
        this.courseUpdateLandingPage = updateDropFromLandingPage;
    }


    public String getUserGroupSelectAttendanceByUsrAndGroup() {
        return userGroupSelectAttendanceByUsrAndGroup;
    }

    public void setUserGroupSelectAttendanceByUsrAndGroup(String userGroupSelectAttendanceByUsrAndGroup) {
        this.userGroupSelectAttendanceByUsrAndGroup = userGroupSelectAttendanceByUsrAndGroup;
    }

    String getGroupeSelectAll() {
        return courseSelectAll;
    }

    void setGroupSelectAll(String courseSelectAll) {
        this.courseSelectAll = courseSelectAll;
    }

    String getGroupSelectById() {
        return groupSelectById;
    }

    public void setGroupSelectById(String groupSelectById) {
        this.groupSelectById = groupSelectById;
    }

    public String getGroupUpdate() {
        return groupUpdate;
    }

    public void setGroupUpdate(String groupUpdate) {
        this.groupUpdate = groupUpdate;
    }

    public String getGroupDelete() {
        return groupDelete;
    }

    public void setGroupDelete(String groupDelete) {
        this.groupDelete = groupDelete;
    }

    public String getGroupInsert() {
        return groupInsert;
    }



    public void setGroupInsert(String groupInsert) {
        this.groupInsert = groupInsert;
    }


    String getUsrSelectAllTrainers() {
        return usrSelectAllTrainers;
    }

    void setUsrSelectAllTrainers(String usrSelectAllTrainers) {
        this.usrSelectAllTrainers = usrSelectAllTrainers;
    }

    String getGroupSelectAll() {
        return groupSelectAll;
    }


    String getLevelSelectAll() {
        return levelSelectAll;
    }

    void setLevelSelectAll(String levelSelectAll) {
        this.levelSelectAll = levelSelectAll;
    }

    String getLevelSelectById() {
        return levelSelectById;
    }

    String getLevelSelectByName() {
        return levelSelectByName;
    }

    void setLevelSelectById(String levelSelectById) {
        this.levelSelectById = levelSelectById;
    }

    String getLevelUpdate() {
        return levelUpdate;
    }

    void setLevelUpdate(String levelUpdate) {
        this.levelUpdate = levelUpdate;
    }

    String getLevelDelete() {
        return levelDelete;
    }

    void setLevelDelete(String levelDelete) {
        this.levelDelete = levelDelete;
    }

    String getLevelInsert() {
        return levelInsert;
    }

    void setLevelInsert(String levelInsert) {
        this.levelInsert = levelInsert;
    }

    String getCourseSelectByLevel() {
        return courseSelectByLevel;
    }

    void setCourseSelectByLevel(String courseSelectByLevel) {
        this.courseSelectByLevel = courseSelectByLevel;
    }


    String getGroupSelectByCourse() {
        return groupSelectByCourse;
    }

    void setGroupSelectByCourse(String groupSelectByCourse) {
        this.groupSelectByCourse = groupSelectByCourse;
    }


    String getLevelSelectByTrainer() {
        return levelSelectByTrainer;
    }

    void setLevelSelectByTrainer(String levelSelectByTrainer) {
        this.levelSelectByTrainer = levelSelectByTrainer;
    }

    String getCourseSelectByTrainer() {
        return courseSelectByTrainer;
    }

    void setCourseSelectByTrainer(String courseSelectByTrainer) {
        this.courseSelectByTrainer = courseSelectByTrainer;
    }

    String getGroupSelectNumberOfEmployees() {
        return groupSelectNumberOfEmployees;
    }

    void setGroupSelectNumberOfEmployees(String groupSelectNumberOfEmployees) {
        this.groupSelectNumberOfEmployees = groupSelectNumberOfEmployees;
    }

    public String getUsrSelectByGroupId() {
        return usrSelectByGroupId;
    }

    public void setUsrSelectByGroupId(String usrSelectByGroupId) {
        this.usrSelectByGroupId = usrSelectByGroupId;
    }

    public String getGroupSelectByTrainerId() {
        return groupSelectByTrainerId;
    }

    public void setGroupSelectByTrainerId(String groupSelectByTrainerId) {
        this.groupSelectByTrainerId = groupSelectByTrainerId;
    }

    public void setAbsenceReasonDelete(String absenceReasonDelete) {
        this.absenceReasonDelete = absenceReasonDelete;
    }

    public void setAbsenceReasonInsert(String absenceReasonInsert) {
        this.absenceReasonInsert = absenceReasonInsert;
    }

    public void setAbsenceReasonSelectAll(String absenceReasonSelectAll) {
        this.absenceReasonSelectAll = absenceReasonSelectAll;
    }

    public void setAbsenceReasonSelectById(String absenceReasonSelectById) {
        this.absenceReasonSelectById = absenceReasonSelectById;
    }

    public void setAbsenceReasonUpdate(String absenceReasonUpdate) {
        this.absenceReasonUpdate = absenceReasonUpdate;
    }

    public String getAbsenceReasonDelete() {
        return absenceReasonDelete;
    }

    public String getAbsenceReasonInsert() {
        return absenceReasonInsert;
    }

    public String getAbsenceReasonSelectAll() {
        return absenceReasonSelectAll;
    }

    public String getAbsenceReasonSelectById() {
        return absenceReasonSelectById;
    }

    public String getAbsenceReasonUpdate() {
        return absenceReasonUpdate;
    }

    public String getUsrSelectAllTrainersById() {
        return usrSelectAllTrainersById;
    }

    public void setUsrSelectAllTrainersById(String usrSelectAllTrainersById) {
        this.usrSelectAllTrainersById = usrSelectAllTrainersById;
    }

    public String getUsrSelectManagerById() {
        return usrSelectManagerById;
    }

    public void setUsrSelectManagerById(String usrSelectManagerById) {
        this.usrSelectManagerById = usrSelectManagerById;
    }

    public void setLevelSelectByName(String levelSelectByName) {
        this.levelSelectByName = levelSelectByName;
    }

    public String getGroupSelectByEmployee() {
        return groupSelectByEmployee;
    }

    public void setGroupSelectByEmployee(String groupSelectByEmployee) {
        this.groupSelectByEmployee = groupSelectByEmployee;
    }

    public String getDesirableScheduleSelectAll() {
        return desirableScheduleSelectAll;
    }

    public void setDesirableScheduleSelectAll(String desirableScheduleSelectAll) {
        this.desirableScheduleSelectAll = desirableScheduleSelectAll;
    }

    public String getDesirableScheduleInsert() {
        return desirableScheduleInsert;
    }

    public void setDesirableScheduleInsert(String desirableScheduleInsert) {
        this.desirableScheduleInsert = desirableScheduleInsert;
    }

    public String getDesirableScheduleSelectByCourseId() {
        return desirableScheduleSelectByCourseId;
    }

    public void setDesirableScheduleSelectByCourseId(String desirableScheduleSelectByCourseId) {
        this.desirableScheduleSelectByCourseId = desirableScheduleSelectByCourseId;
    }

    public String getSuitabilitySelectAll() {
        return suitabilitySelectAll;
    }

    public void setSuitabilitySelectAll(String suitabilitySelectAll) {
        this.suitabilitySelectAll = suitabilitySelectAll;
    }


    public String getUsrSelectUngroupedByCourseId() {
        return usrSelectUngroupedByCourseId;
    }

    public void setUsrSelectUngroupedByCourseId(String usrSelectUngroupedByCourseId) {
        this.usrSelectUngroupedByCourseId = usrSelectUngroupedByCourseId;
    }


    public String getUserGroupDeleteForGroup() {
        return userGroupDeleteForGroup;
    }

    public void setUserGroupDeleteForGroup(String userGroupDeleteForGroup) {
        this.userGroupDeleteForGroup = userGroupDeleteForGroup;
    }

    public String getUserGroupSelectByUsrAndCourse() {
        return userGroupSelectByUsrAndCourse;
    }

    public void setUserGroupSelectByUsrAndCourse(String userGroupSelectByUsrAndCourse) {
        this.userGroupSelectByUsrAndCourse = userGroupSelectByUsrAndCourse;
    }

    public String getUserGroupInsert() {
        return userGroupInsert;
    }

    public void setUserGroupInsert(String userGroupInsert) {
        this.userGroupInsert = userGroupInsert;
    }

    public String getUserGroupUpdate() {
        return userGroupUpdate;
    }

    public void setUserGroupUpdate(String userGroupUpdate) {
        this.userGroupUpdate = userGroupUpdate;
    }

    public String getUserGroupSelectByGroup() {
        return userGroupSelectByGroup;
    }

    public void setUserGroupSelectByGroup(String userGroupSelectByGroup) {
        this.userGroupSelectByGroup = userGroupSelectByGroup;
    }

    public String getUsrUpdateUsrByAdmin() {
        return usrUpdateUsrByAdmin;
    }

    public void setUsrUpdateUsrByAdmin(String usrUpdateUsrByAdmin) {
        this.usrUpdateUsrByAdmin = usrUpdateUsrByAdmin;
    }

    public String getUsrUpdateChangeActive() {
        return usrUpdateChangeActive;
    }

    public void setUsrUpdateChangeActive(String usrUpdateChangeActive) {
        this.usrUpdateChangeActive = usrUpdateChangeActive;
    }

    public String getUsrSelectAllManagers() {
        return usrSelectAllManagers;
    }

    public void setUsrSelectAllManagers(String usrSelectAllManagers) {
        this.usrSelectAllManagers = usrSelectAllManagers;
    }
}
