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
    @Value("${usr.select-on-landing-page}")
    private String usrLandingPage;
    @Value("${usr.select-all-trainers-by-id}")
    private String usrSelectAllTrainersById;
    @Value("${usr.select-manager-by-id}")
    private String usrSelectManagerById;

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
    @Value("${course.update-drop-from-landing-page}")
    private String updateDropFromLandingPage;


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

    String getUsrLandingPage() { return usrLandingPage; }

    void setUsrLandingPage(String usrLandingPage) { this.usrLandingPage = usrLandingPage; }



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

    String getCourseLandingPage() { return courseLandingPage; }

    void setCourseLandingPage(String courseLandingPage) { this.courseLandingPage = courseLandingPage; }

    String getUpdateDropFromLandingPage() { return updateDropFromLandingPage; }

    void setUpdateDropFromLandingPage(String updateDropFromLandingPage) { this.updateDropFromLandingPage = updateDropFromLandingPage; }


    String getGroupeSelectAll() {
        return courseSelectAll;
    }

    void setGroupSelectAll(String courseSelectAll) {
        this.courseSelectAll = courseSelectAll;
    }

    String getGroupSelectById() {
        return courseSelectById;
    }

    void setGroupSelectById(String courseSelectById) {
        this.courseSelectById = courseSelectById;
    }

    String getGroupUpdate() {
        return courseUpdate;
    }

    void setGroupUpdate(String courseUpdate) {
        this.courseUpdate = courseUpdate;
    }

    String getGroupDelete() {
        return courseDelete;
    }

    void setGroupDelete(String courseDelete) {
        this.courseDelete = courseDelete;
    }

    String getGroupInsert() {
        return courseInsert;
    }

    void setGroupInsert(String courseInsert) {
        this.courseInsert = courseInsert;
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
    String getLevelSelectByName(){ return levelSelectByName;}

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
}
