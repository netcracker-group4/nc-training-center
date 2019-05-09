package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.LessonAttachmentDao;
import ua.com.nc.domain.LessonAttachment;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
@PropertySource("classpath:sql_queries.properties")
public class LessonAttachmentDaoImpl extends AbstractDaoImpl<LessonAttachment> implements LessonAttachmentDao {
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

    @Autowired
    public LessonAttachmentDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
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
    protected void prepareStatementForInsert(PreparedStatement statement, LessonAttachment entity) throws SQLException {
        statement.setInt(1, entity.getAttachmentId());
        statement.setInt(2, entity.getLessonId());
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
        String sql = selectAllByLessonId;
        log.info(sql + "getAllByLessonId " + lessonId);
        return getFromSqlById(sql, lessonId);
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
