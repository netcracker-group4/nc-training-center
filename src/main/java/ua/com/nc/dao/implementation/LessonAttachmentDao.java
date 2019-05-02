package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ILessonAttachmentDao;
import ua.com.nc.domain.LessonAttachment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
@PropertySource("classpath:sql_queries.properties")
public class LessonAttachmentDao extends GenericAbstractDao<LessonAttachment,Integer> implements ILessonAttachmentDao {
    @Value("${lesson_attachment.insert}")
    private String lessonAttachmentInsert;
    @Value("${lesson_attachment.delete-by-attachment-id}")
    private String lessonAttachmentDeleteByAttachmentId;

    public LessonAttachmentDao(@Value("${spring.datasource.url}") String DATABASE_URL,
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
        return null;
    }

    @Override
    protected String getSelectQuery() {
        return null;
    }

    @Override
    protected String getInsertQuery() {
        return lessonAttachmentInsert;
    }

    @Override
    protected String getDeleteQuery() {
        return null;
    }

    @Override
    protected String getUpdateQuery() {
        return null;
    }

    @Override
    protected void setId(PreparedStatement statement, Integer id) throws SQLException {

    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, LessonAttachment entity) throws SQLException {

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, LessonAttachment entity) throws SQLException {

    }

    @Override
    protected List<LessonAttachment> parseResultSet(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public void deleteByAttachmentId(Integer attachmentId) throws PersistException {
        String sql = lessonAttachmentDeleteByAttachmentId;
        log(sql, "LOG DeleteQuery");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,attachmentId);
            int count = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new PersistException(e);
        }
    }

}
