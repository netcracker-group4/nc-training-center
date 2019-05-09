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
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:sql_queries.properties")
public class LessonAttachmentDao extends GenericAbstractDao<LessonAttachment, Integer> implements ILessonAttachmentDao {
    @Value("${lesson_attachment.insert}")
    private String lessonAttachmentInsert;
    @Value("${lesson_attachment.delete-by-attachment-id}")
    private String lessonAttachmentDeleteByAttachmentId;
    @Value("${lesson_attachment.select-all-by-lesson-id}")
    private String selectAllByLessonId;
    @Value("${lesson_attachment.delete-all-by-lesson-id}")
    private String deleteAllByLessonId;
    @Value("${lesson_attachment.delete}")
    private String lessonAttachmentDelete;

    public LessonAttachmentDao(@Value("${spring.datasource.url}") String DATABASE_URL,
                               @Value("${spring.datasource.username}") String DATABASE_USER,
                               @Value("${spring.datasource.password}") String DATABASE_PASSWORD) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }


    @Override
    protected Integer parseId(ResultSet rs) {
        return 0;
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
        return lessonAttachmentDelete;
    }

    @Override
    protected String getUpdateQuery() {
        return null;
    }

    @Override
    protected void setId(PreparedStatement statement, Integer id) {
        //there is no id
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, LessonAttachment entity) throws SQLException {
        statement.setInt(1, entity.getAttachmentId());
        statement.setInt(2, entity.getLessonId());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, LessonAttachment entity) {
    }

    @Override
    protected List<LessonAttachment> parseResultSet(ResultSet rs) throws SQLException {
        List<LessonAttachment> lessonAttachments = new ArrayList<>();
        while (rs.next()) {
            int attachmentId = rs.getInt("ATTACHMENT_ID");
            int lessonId = rs.getInt("LESSON_ID");
            LessonAttachment lessonAttachment = new LessonAttachment(attachmentId, lessonId);
            lessonAttachments.add(lessonAttachment);
        }
        return lessonAttachments;
    }

    @Override
    public void deleteByAttachmentId(Integer attachmentId) throws PersistException {
        String sql = lessonAttachmentDeleteByAttachmentId;
        log.info(sql + "LOG DeleteQuery " + attachmentId);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, attachmentId);
            int count = statement.executeUpdate();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public List<LessonAttachment> getAllByLessonId(Integer lessonId) throws PersistException {
        List<LessonAttachment> list;
        String sql = selectAllByLessonId;
        log.info(sql + "getAllByLessonId " + lessonId);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lessonId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
        return list;
    }

    @Override
    public void deleteByLessonId(Integer lessonId) throws PersistException {
        String sql = deleteAllByLessonId;
        log.info(sql + "LOG deleteByLessonId " + lessonId);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lessonId);
            statement.executeUpdate();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public void insertAttachment(LessonAttachment lessonAttachment) throws PersistException {
        String sql = lessonAttachmentInsert;
        log.info(sql + " insert " + lessonAttachment);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, lessonAttachment);
            statement.execute();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

}
