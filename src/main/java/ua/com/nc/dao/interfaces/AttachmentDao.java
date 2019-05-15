package ua.com.nc.dao.interfaces;

import ua.com.nc.dao.PersistException;
import ua.com.nc.domain.Attachment;

import java.util.List;

public interface AttachmentDao extends GenericDao<Attachment> {
    List<Attachment> getByLessonId(Integer lessonId);
    List<Attachment> getByTrainerId(Integer trainerId);
    Attachment getByName(String url) throws PersistException;
}
