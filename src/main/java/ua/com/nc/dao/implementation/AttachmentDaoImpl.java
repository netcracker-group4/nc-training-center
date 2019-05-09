package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.AttachmentDao;
import ua.com.nc.domain.Attachment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:sql_queries.properties")
public class AttachmentDaoImpl extends AbstractDaoImpl<Attachment> implements AttachmentDao {

    @Value("${attachment.select-all}")
    private String attachmentSelectAll;
    @Value("${attachment.select-by-id}")
    private String attachmentSelectById;
    @Value("${attachment.delete}")
    private String attachmentDelete;
    @Value("${attachment.insert}")
    private String attachmentInsert;
    @Value("${attachment.select-by-url}")
    private String attachmentSelectByUrl;
    @Value("${attachment.select-by-lesson-id}")
    private String attachmentSelectByLessonId;


    public AttachmentDaoImpl(@Value("${spring.datasource.url}") String DATABASE_URL,
                             @Value("${spring.datasource.username}") String DATABASE_USER,
                             @Value("${spring.datasource.password}") String DATABASE_PASSWORD) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    @Override
    protected Integer parseId(ResultSet rs) {
        return null;
    }

    @Override
    protected String getSelectByIdQuery() {
        return attachmentSelectById;
    }

    @Override
    protected String getSelectQuery() {
        return attachmentSelectAll;
    }

    @Override
    protected String getInsertQuery() {
        return attachmentInsert;
    }

    @Override
    protected String getDeleteQuery() {
        return attachmentDelete;
    }

    @Override
    protected String getUpdateQuery() {
        return null;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Attachment entity) throws SQLException {
        setAllFields(statement, entity);
    }

    private void setAllFields(PreparedStatement statement, Attachment entity) throws SQLException {
        statement.setString(1, entity.getUrl());
        statement.setString(2, entity.getDescription());

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Attachment entity) {

    }

    @Override
    protected List<Attachment> parseResultSet(ResultSet rs) throws SQLException {
        List<Attachment> attachments = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String url = rs.getString("url");
            String description = rs.getString("description");
            Attachment attachment = new Attachment(id, url, description);
            attachments.add(attachment);
        }

        return attachments;
    }

    @Override
    public Attachment getByUrl(String url) throws PersistException {
        List<Attachment> list;
        String sql = attachmentSelectByUrl;
        log.info(sql + " SelectByUrlQuery " + url);
        return getUniqueFromSqlByString(sql, url);
    }



    @Override
    public List<Attachment> getByLessonId(Integer lessonId) {
        String sql = attachmentSelectByLessonId;
        return getFromSqlById(sql, lessonId);
    }


}
