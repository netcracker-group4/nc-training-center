package ua.com.nc.service;

import ua.com.nc.domain.Attachment;
import ua.com.nc.dto.DtoAttachment;

import java.io.InputStream;

public interface AttachmentService {
    void add(Integer lessonId, Attachment attachment);

    void add(Integer lessonId, String url, String name, Integer trainerId, String description);

    void delete(Integer id);

    Attachment uploadFile(Integer trainerId, DtoAttachment dtoAttachment);

    InputStream downloadFile(Integer id);

    void link(Integer lessonId, Integer attachmentId);

    void unlink(Integer lessonId, Integer attachmentId);
}
