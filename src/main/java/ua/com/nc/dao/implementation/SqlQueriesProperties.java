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
}
