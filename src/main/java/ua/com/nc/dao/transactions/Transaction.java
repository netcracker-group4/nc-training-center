package ua.com.nc.dao.transactions;

import ua.com.nc.dao.interfaces.*;

public interface Transaction extends AutoCloseable {

    AttachmentDao attachment();
    CourseDao course();
    DesiredScheduleDao desiredSchedule();
    GroupDao group();
    LessonAttachmentDao lessonAttachment();
    LessonDao lesson();
    SuitabilityDao suitability();
    UserDao user();
    UserGroupDao userGroupDao();

    void commit();
}
