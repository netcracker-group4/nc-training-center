package ua.com.nc.dao.interfaces;

import ua.com.nc.dao.PersistException;
import ua.com.nc.domain.Attachment;

import java.util.List;

public interface IAttachmentDao extends GenericDao<Attachment, Integer> {
    List<Attachment> getByLessonId(Integer lessonId);

    Attachment getByUrl(String url) throws PersistException;
}
