package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.interfaces.AttachmentDao;
import ua.com.nc.dao.interfaces.LessonAttachmentDao;
import ua.com.nc.domain.Attachment;
import ua.com.nc.domain.LessonAttachment;
import ua.com.nc.service.AttachmentService;

import java.io.*;

@Log4j2
@Service
public class AttachmentServiceImpl implements AttachmentService {
    @Autowired
    private AttachmentDao attachmentDao;
    @Autowired
    private LessonAttachmentDao lessonAttachmentDao;


    @Override
    public void add(Integer lessonId, Attachment attachment) {

        if (attachmentDao.getByName(attachment.getName()) == null) {

            attachmentDao.insert(attachment);
        }
        link(lessonId, attachmentDao.getByName(attachment.getName()).getId());
    }

    @Override
    public void add(Integer lessonId, String url, String name, Integer trainerId, String description) {
        System.out.println("Add method 2 used");
        Attachment attachment = new Attachment(url, name, trainerId, description);
        add(lessonId, attachment);
    }

    @Override
    public void delete(Integer id) {
        File file = new File(attachmentDao.getEntityById(id).getUrl());
        if (file.delete()){
            log.info("File removed from directory");
        }
        else{
            log.trace("File not found");
        }
        lessonAttachmentDao.deleteByAttachmentId(id);
        attachmentDao.delete(id);
    }

    @Override
    public void uploadFile(Integer lessonId, Integer trainerId, String description, MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                String name = file.getOriginalFilename();

                String rootPath = "src/main/resources";
                File dir = new File(rootPath + File.separator + "attachments"+ File.separator + trainerId.toString());

                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String filePath = dir.getAbsolutePath() + File.separator + name;

                if (attachmentDao.getByName(filePath) == null) {

                    log.info("File is not in base");
                    File uploadedFile = new File(filePath);
                    try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
                    ) {
                        stream.write(bytes);
                    }
                }
                add(lessonId,filePath, name, trainerId, description);
            } catch (Exception e) {
                log.trace(e);
            }
        }
    }

    @Override
    public FileInputStream downloadFile(Integer id) {
        Attachment attachment = attachmentDao.getEntityById(id);
        String path = attachment.getUrl();
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            log.trace(e);
        }
        return null;
    }

    @Override
    public void link(Integer lessonId, Integer attachmentId) {
        LessonAttachment lessonAttachment = new LessonAttachment(attachmentId, lessonId);
        lessonAttachmentDao.insert(lessonAttachment);
    }

    @Override
    public void unlink(Integer lessonId, Integer attachmentId) {
        lessonAttachmentDao.unlink(lessonId,attachmentId);
    }

}
