package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.LessonAttachmentDao;
import ua.com.nc.domain.LessonAttachment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@PropertySource("classpath:sql_queries.properties")
//TODO delete it?????
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
    @Value("${lesson_attachment.unlink}")
    private String lessonAttachmentUnlink;

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
        log.info("Retrieved LessonAttachments from database " + lessonAttachments);
        return lessonAttachments;
    }

    @Override
    public void deleteByAttachmentId(Integer attachmentId) throws PersistException {
        String sql = lessonAttachmentDeleteByAttachmentId;
        log.info("delete attachments for all lessons " + attachmentId + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, attachmentId);
            int count = statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    @Override
    public List<LessonAttachment> getAllByLessonId(Integer lessonId) throws PersistException {
        String sql = selectAllByLessonId;
        log.info("find attachments for lesson by lessonId " + lessonId + " " + sql );
        return getFromSqlById(sql, lessonId);
    }

    @Override
    public void deleteByLessonId(Integer lessonId) throws PersistException {
        String sql = deleteAllByLessonId;
        log.info("delete attachments for lesson with id " + lessonId + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lessonId);
            statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    @Override
    public void insertAttachment(LessonAttachment lessonAttachment) throws PersistException {
        String sql = lessonAttachmentInsert;
        log.info("insert attachment " + lessonAttachment + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, lessonAttachment);
            statement.execute();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    @Override
    public void unlink(Integer lessonId, Integer attachmentId) {
        String sql = lessonAttachmentUnlink;
        log.info(sql + "LOG unlink " + "lessonId: " + lessonId + ", attachmentId: " + attachmentId);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,attachmentId);
            statement.setInt(2, lessonId);
            statement.executeUpdate();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

}
