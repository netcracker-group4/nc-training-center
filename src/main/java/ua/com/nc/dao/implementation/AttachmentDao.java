package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.IAttachmentDao;
import ua.com.nc.domain.Attachment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Component
@PropertySource("classpath:sql_queries.properties")
public class AttachmentDao extends GenericAbstractDao<Attachment, Integer> implements IAttachmentDao {

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



    public AttachmentDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                    @Value("${spring.datasource.username}") String DATABASE_USER,
                    @Value("${spring.datasource.password}") String DATABASE_PASSWORD) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    @Override
    protected Integer parseId(ResultSet rs) throws SQLException {
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
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Attachment entity) throws SQLException {
        setAllFields(statement,entity);
     }

    private void setAllFields(PreparedStatement statement, Attachment entity) throws SQLException {
        statement.setString(1, entity.getUrl());
        statement.setString(2, entity.getDescription());

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Attachment entity) throws SQLException {

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
        log(sql, "LOG SelectByUrlQuery");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,url);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public List<Attachment> getByLessonId(Integer lessonId) {
        List<Attachment> attachments;
        String sql = attachmentSelectByLessonId;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lessonId);
            ResultSet rs = statement.executeQuery();
            attachments = parseResultSet(rs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new PersistException(e);
        }
        return attachments;
    }


}
