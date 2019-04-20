package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Configuration
@PropertySource("classpath:sql_queries.properties")
public class SqlQueriesProperties {
    @Value("${usr.select-all}")
    private String selectAll;
    @Value("${usr.select-by-id}")
    private String selectById;
    @Value("${usr.select-by-email}")
    private String selectByEmail;
    @Value("${usr.update}")
    private String update;
    @Value("${usr.delete}")
    private String delete;
    @Value("${usr.insert}")
    private String insert;


    public String getSelectAll() {
        return selectAll;
    }

    public void setSelectAll(String selectAll) {
        this.selectAll = selectAll;
    }

    public String getSelectById() {
        return selectById;
    }

    public void setSelectById(String selectById) {
        this.selectById = selectById;
    }

    public String getSelectByEmail() {
        return selectByEmail;
    }

    public void setSelectByEmail(String selectByEmail) {
        this.selectByEmail = selectByEmail;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getInsert() {
        return insert;
    }

    public void setInsert(String insert) {
        this.insert = insert;
    }
}
