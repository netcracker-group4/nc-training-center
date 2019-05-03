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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
@Service
public class AttachmentServiceImpl implements AttachmentService {
    private static final Logger log = Logger.getLogger(AttachmentServiceImpl.class);
    @Autowired
    private IAttachmentDao iAttachmentDao;
    @Autowired
    private ILessonAttachmentDao iLessonAttachmentDao;


    @Override
    public void add(Integer lessonId, Attachment attachment) {
        if (iAttachmentDao.getByUrl(attachment.getUrl()) == null) {
            iAttachmentDao.insert(attachment);
            iAttachmentDao.commit();
        }
        LessonAttachment lessonAttachment = new LessonAttachment(iAttachmentDao.getByUrl(attachment.getUrl()).getId() ,lessonId);
        iLessonAttachmentDao.insert(lessonAttachment);
        iLessonAttachmentDao.commit();
    }

    @Override
    public void add(Integer lessonId, String url, String description) {
        Attachment attachment = new Attachment(url,description);
    }

    @Override
    public void delete(Integer id) {
        iAttachmentDao.delete(id);
        iAttachmentDao.commit();
        iLessonAttachmentDao.deleteByAttachmentId(id);
        iLessonAttachmentDao.commit();
    }

    @Override
    public void uploadFile(Integer lessonId, MultipartFile file) {
        String name = null;
        if(!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();

                name = file.getOriginalFilename();

                String rootPath = "src/main/resources";
                File dir = new File(rootPath + File.separator + "attachments");

                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String filePath = dir.getAbsolutePath() + File.separator + name;

                if (iAttachmentDao.getByUrl(filePath) == null) {
                    log.info("File is not in base");
                    File uploadedFile = new File(filePath);

                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
                    stream.write(bytes);
                    stream.flush();
                    stream.close();
                }
 //               add(lessonId,filePath,null);
            } catch (Exception e) {
                log.trace(e);
            }
        }
    }
}
