package ua.com.nc.dao.interfaces;

import ua.com.nc.dao.PersistException;
import ua.com.nc.domain.LessonAttachment;

public interface ILessonAttachmentDao extends GenericDao<LessonAttachment,Integer> {
    public void deleteByAttachmentId(Integer attachmentId) throws PersistException;
}
