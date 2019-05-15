package ua.com.nc.dao.interfaces;

import ua.com.nc.dao.PersistException;
import ua.com.nc.domain.LessonAttachment;

import java.util.List;

public interface LessonAttachmentDao extends GenericDao<LessonAttachment> {
    void deleteByAttachmentId(Integer attachmentId) throws PersistException;

    List<LessonAttachment> getAllByLessonId(Integer lessonId) throws PersistException;

    void deleteByLessonId(Integer lessonId) throws PersistException;

    void insertAttachment(LessonAttachment lessonAttachment) throws PersistException;

    void unlink(Integer lessonId, Integer attachmentId);
}
