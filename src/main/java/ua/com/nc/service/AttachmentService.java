package ua.com.nc.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.domain.Attachment;

import java.io.FileInputStream;

public interface AttachmentService {
    void add(Integer lessonId, Attachment attachment);

    void add(Integer lessonId, String url, String name, Integer trainerId, String description);

    void delete(Integer id);

    void uploadFile(Integer lessonId, Integer trainerId, String description, MultipartFile file);

    FileInputStream downloadFile(Integer id);

    void link(Integer lessonId, Integer attachmentId);
    void unlink(Integer lessonId, Integer attachmentId);
}
