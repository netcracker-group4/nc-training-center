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

    String getInsert() {
        return usrInsert;
    }

    public void setInsert(String insert) {
        this.usrInsert = insert;
    }
}
