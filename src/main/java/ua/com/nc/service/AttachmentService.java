package ua.com.nc.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.domain.Attachment;
import ua.com.nc.dto.DtoAttachment;

import java.io.FileInputStream;
import java.io.InputStream;

public interface AttachmentService {
    void add(Integer lessonId, Attachment attachment);

    void add(Integer lessonId, String url, String name, Integer trainerId, String description);

    void delete(Integer id);

    Attachment uploadFile(Integer lessonId, Integer trainerId, String description, MultipartFile file);

    Attachment uploadFile(Integer trainerId, DtoAttachment dtoAttachment);


    InputStream downloadFile(Integer id);

    void link(Integer lessonId, Integer attachmentId);

    void unlink(Integer lessonId, Integer attachmentId);
}
