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
    
    @Value("${course.select-all}")
    private String courseSelectAll;
    @Value("${course.select-by-id}")
    private String courseSelectById;
    @Value("${course.update}")
    private String courseUpdate;
    @Value("${course.delete}")
    private String courseDelete;
    @Value("${course.insert}")
    private String courseInsert;

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


    @Value("${level.select-all}")
    private String levelSelectAll;
    @Value("${level.select-by-id}")
    private String levelSelectById;
    @Value("${level.update}")
    private String levelUpdate;
    @Value("${level.delete}")
    private String levelDelete;
    @Value("${level.insert}")
    private String levelInsert;
    

    String getUsrSelectAll() {
        return usrSelectAll;
    }

    public void setUsrSelectAll(String usrSelectAll) {
        this.usrSelectAll = usrSelectAll;
    }

    String getUsrSelectById() {
        return usrSelectById;
    }

    public void setUsrSelectById(String usrSelectById) {
        this.usrSelectById = usrSelectById;
    }

    String getUsrSelectByEmail() {
        return usrSelectByEmail;
    }

    public void setUsrSelectByEmail(String usrSelectByEmail) {
        this.usrSelectByEmail = usrSelectByEmail;
    }

    String getUsrUpdate() {
        return usrUpdate;
    }

    public void setUsrUpdate(String usrUpdate) {
        this.usrUpdate = usrUpdate;
    }

    String getUsrDelete() {
        return usrDelete;
    }

    public void setUsrDelete(String usrDelete) {
        this.usrDelete = usrDelete;
    }

    public String getUsrInsert() {
        return usrInsert;
    }

    public void setUsrInsert(String usrInsert) {
        this.usrInsert = usrInsert;
    }

    public String getCourseSelectAll() {
        return courseSelectAll;
    }

    public void setCourseSelectAll(String courseSelectAll) {
        this.courseSelectAll = courseSelectAll;
    }

    public String getCourseSelectById() {
        return courseSelectById;
    }

    public void setCourseSelectById(String courseSelectById) {
        this.courseSelectById = courseSelectById;
    }

    public String getCourseUpdate() {
        return courseUpdate;
    }

    public void setCourseUpdate(String courseUpdate) {
        this.courseUpdate = courseUpdate;
    }

    public String getCourseDelete() {
        return courseDelete;
    }

    public void setCourseDelete(String courseDelete) {
        this.courseDelete = courseDelete;
    }

    public String getCourseInsert() {
        return courseInsert;
    }

    public void setCourseInsert(String courseInsert) {
        this.courseInsert = courseInsert;
    }

    public String getGroupeSelectAll() {
        return courseSelectAll;
    }

    public void setGroupSelectAll(String courseSelectAll) {
        this.courseSelectAll = courseSelectAll;
    }

    public String getGroupSelectById() {
        return courseSelectById;
    }

    public void setGroupSelectById(String courseSelectById) {
        this.courseSelectById = courseSelectById;
    }

    public String getGroupUpdate() {
        return courseUpdate;
    }

    public void setGroupUpdate(String courseUpdate) {
        this.courseUpdate = courseUpdate;
    }

    public String getGroupDelete() {
        return courseDelete;
    }

    public void setGroupDelete(String courseDelete) {
        this.courseDelete = courseDelete;
    }

    public String getGroupInsert() {
        return courseInsert;
    }

    public void setGroupInsert(String courseInsert) {
        this.courseInsert = courseInsert;
    }


    public String getUsrSelectAllTrainers() {
        return usrSelectAllTrainers;
    }

    public void setUsrSelectAllTrainers(String usrSelectAllTrainers) {
        this.usrSelectAllTrainers = usrSelectAllTrainers;
    }

    public String getGroupSelectAll() {
        return groupSelectAll;
    }


    public String getLevelSelectAll() {
        return levelSelectAll;
    }

    public void setLevelSelectAll(String levelSelectAll) {
        this.levelSelectAll = levelSelectAll;
    }

    public String getLevelSelectById() {
        return levelSelectById;
    }

    public void setLevelSelectById(String levelSelectById) {
        this.levelSelectById = levelSelectById;
    }

    public String getLevelUpdate() {
        return levelUpdate;
    }

    public void setLevelUpdate(String levelUpdate) {
        this.levelUpdate = levelUpdate;
    }

    public String getLevelDelete() {
        return levelDelete;
    }

    public void setLevelDelete(String levelDelete) {
        this.levelDelete = levelDelete;
    }

    public String getLevelInsert() {
        return levelInsert;
    }

    public void setLevelInsert(String levelInsert) {
        this.levelInsert = levelInsert;
    }
}
