package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.interfaces.ICourseStatus;

@Log4j
@Component
@PropertySource("classpath:sql_queries.properties")
public class CourseStatusDao implements ICourseStatus {

    public CourseStatusDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                           @Value("${spring.datasource.username}") String DATABASE_USER,
                           @Value("${spring.datasource.password}") String DATABASE_PASSWORD) {
    }

    @Override
    public int getIdByName(String name) {
        return 0;
    }
}
