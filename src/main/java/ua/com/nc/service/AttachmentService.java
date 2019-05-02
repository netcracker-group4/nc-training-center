package ua.com.nc.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.domain.Attachment;

public interface AttachmentService {
    void add(Integer lessonId, Attachment attachment);
    void add(Integer lessonId, String url, String description);
    void delete(Integer id);
    void uploadFile(Integer lessonId, MultipartFile file);
}
