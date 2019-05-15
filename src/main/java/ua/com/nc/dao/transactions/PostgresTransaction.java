package ua.com.nc.dao.transactions;

import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.*;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresTransaction implements Transaction {

    AttachmentDao    attachmentDao;
    CourseDao courseDao;
    DesiredScheduleDao desiredScheduleDao;
    GroupDao groupDao;
    LessonAttachmentDao lessonAttachmentDao;
    LessonDao lessonDao;
    SuitabilityDao suitabilityDao;
    UserDao userDao;
    UserGroupDao userGroupDao;

    private Connection connection;

    PostgresTransaction(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    @Override
    public AttachmentDao attachment() {
        return null;
    }

    @Override
    public CourseDao course() {
        return null;
    }

    @Override
    public DesiredScheduleDao desiredSchedule() {
        return null;
    }

    @Override
    public GroupDao group() {
        return null;
    }

    @Override
    public LessonAttachmentDao lessonAttachment() {
        return null;
    }

    @Override
    public LessonDao lesson() {
        return null;
    }

    @Override
    public SuitabilityDao suitability() {
        return null;
    }

    @Override
    public UserDao user() {
        return null;
    }

    @Override
    public UserGroupDao userGroupDao() {
        return null;
    }

    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistException("Error while committing transaction", e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.rollback();
        connection.close();
    }
}
