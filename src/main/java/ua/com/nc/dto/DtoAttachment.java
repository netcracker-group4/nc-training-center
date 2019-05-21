package ua.com.nc.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DtoAttachment {
    private MultipartFile file;
    private String lessonId;
    private String description;
}
