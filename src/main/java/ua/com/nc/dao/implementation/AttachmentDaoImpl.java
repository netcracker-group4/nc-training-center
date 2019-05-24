package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.AttachmentDao;
import ua.com.nc.domain.Attachment;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Repository
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
    @Value("${attachment.select-by-name}")
    private String attachmentSelectByName;
    @Value("${attachment.select-by-lesson-id}")
    private String attachmentSelectByLessonId;
    @Value("${attachment.select-by-trainer-id}")
    private String attachmentSelectByTrainerId;

    @Autowired
    public AttachmentDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
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
    protected void prepareStatementForInsert(PreparedStatement statement, Attachment entity) throws SQLException {
        setAllFields(statement, entity);
    }

    private void setAllFields(PreparedStatement statement, Attachment entity) throws SQLException {
        statement.setString(1, entity.getUrl());
        statement.setString(2,entity.getName());
        statement.setInt(3,entity.getTrainerId());
        statement.setString(4, entity.getDescription());

    }

    @Override
    protected List<Attachment> parseResultSet(ResultSet rs) throws SQLException {
        List<Attachment> attachments = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String url = rs.getString("url");
            Integer trainerId = rs.getInt("trainer_id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            Attachment attachment = new Attachment(id, url, name, trainerId, description);
            attachments.add(attachment);
        }
        log.info("received Attachments from database  "  + attachments);
        return attachments;
    }

    @Override
    public Attachment getByName(String url) throws PersistException {
        String sql = attachmentSelectByName;
        log.info(" Select attachment by url  " + url + " " + sql);
        return getUniqueFromSqlByString(sql, url);
    }


    @Override
    public List<Attachment> getByLessonId(Integer lessonId) {
        String sql = attachmentSelectByLessonId;
        log.info(" Select attachment for lesson by lesson id  " + lessonId + " " + sql);
        return getFromSqlById(sql, lessonId);
    }

    @Override
    public List<Attachment> getByTrainerId(Integer trainerId) {
        String sql = attachmentSelectByTrainerId;
        log.info(" Select attachments uploaded by  trainer, trainerId  " + trainerId + " " + sql);
        return getFromSqlById(sql, trainerId);
    }


}
