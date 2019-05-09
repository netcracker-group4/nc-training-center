package ua.com.nc.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.interfaces.IAttachmentDao;
import ua.com.nc.dao.interfaces.ILessonAttachmentDao;
import ua.com.nc.domain.Attachment;
import ua.com.nc.domain.LessonAttachment;
import ua.com.nc.service.AttachmentService;

import java.io.*;

@Service
public class AttachmentServiceImpl implements AttachmentService {
    private static final Logger log = Logger.getLogger(AttachmentServiceImpl.class);
    @Autowired
    private IAttachmentDao attachmentDao;
    @Autowired
    private ILessonAttachmentDao lessonAttachmentDao;


    @Override
    public void add(Integer lessonId, Attachment attachment) {

        if (attachmentDao.getByUrl(attachment.getUrl()) == null) {

            attachmentDao.insert(attachment);
            attachmentDao.commit();
        }
        link(lessonId,attachmentDao.getByUrl(attachment.getUrl()).getId());
    }

    @Override
    public void add(Integer lessonId, String url, String description) {
        System.out.println("Add method 2 used");
        Attachment attachment = new Attachment(url,description);
        add(lessonId, attachment);
    }

    @Override
    public void delete(Integer id) {
        attachmentDao.delete(id);
        attachmentDao.commit();
        lessonAttachmentDao.deleteByAttachmentId(id);
        lessonAttachmentDao.commit();
    }

    @Override
    public void uploadFile(Integer lessonId, MultipartFile file) {
        if(!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();

                String name = file.getOriginalFilename();

                String rootPath = "src/main/resources";
                File dir = new File(rootPath + File.separator + "attachments");

                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String filePath = dir.getAbsolutePath() + File.separator + name;

                if (attachmentDao.getByUrl(filePath) == null) {

                    log.info("File is not in base");
                    File uploadedFile = new File(filePath);
                    try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
                    ){
                        stream.write(bytes);
                    }
                }
                add(lessonId, name, null);
            } catch (Exception e) {
                log.trace(e);
            }
        }
    }

    @Override
    public FileInputStream downloadFile(Integer id) {
        System.out.println("Service called");
        String path = "src/main/resources/attachments/";
        Attachment attachment = attachmentDao.getEntityById(id);
        path = path + attachment.getUrl();
        try {
            FileInputStream fin = new FileInputStream(path);
            return fin;
        } catch (FileNotFoundException e) {
            log.trace(e);
        }
        return null;
    }

    @Override
    public void link(Integer lessonId, Integer attachmentId) {
        LessonAttachment lessonAttachment = new LessonAttachment(attachmentId ,lessonId);
        lessonAttachmentDao.insertAttachment(lessonAttachment);
        lessonAttachmentDao.commit();
    }

}
