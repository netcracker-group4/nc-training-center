package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.interfaces.AttachmentDao;
import ua.com.nc.dao.interfaces.LessonAttachmentDao;
import ua.com.nc.domain.Attachment;
import ua.com.nc.domain.LessonAttachment;
import ua.com.nc.dto.DtoAttachment;
import ua.com.nc.exceptions.LogicException;
import ua.com.nc.service.AttachmentService;

import java.io.*;

@SuppressWarnings("Duplicates")
@Log4j2
@Service
public class AttachmentServiceImpl implements AttachmentService {
    @Autowired
    private AttachmentDao attachmentDao;
    @Autowired
    private LessonAttachmentDao lessonAttachmentDao;
    @Autowired
    private FiletransferServiceImpl fileService;

    @Override
    public void add(Integer lessonId, Attachment attachment) {

        if (attachmentDao.getByName(attachment.getName()) == null) {

            attachmentDao.insert(attachment);
        }
        link(lessonId, attachmentDao.getByName(attachment.getName()).getId());
    }

    @Override
    public void add(Integer lessonId, String url, String name, Integer trainerId, String description) {
        Attachment attachment = new Attachment(url, name, trainerId, description);
        add(lessonId, attachment);
    }

    @Override
    public void delete(Integer id) {
        File file = new File(attachmentDao.getEntityById(id).getUrl());
        if (file.delete()) {
            log.info("File removed from directory");
        } else {
            log.error("File not found");
        }
        lessonAttachmentDao.deleteByAttachmentId(id);
        attachmentDao.delete(id);
    }

    @Override
    public Attachment uploadFile(Integer trainerId, DtoAttachment dtoAttachment) {
        String rootDir = "/attachments";
        if (!dtoAttachment.getFile().isEmpty()) {
            try {
                MultipartFile multipartFile = dtoAttachment.getFile();
                String name = multipartFile.getOriginalFilename();
                String filePath = rootDir + "/" + trainerId.toString();
                saveToDisk(multipartFile, filePath);
                return saveToDatabase(trainerId, dtoAttachment, name, filePath);
            } catch (Exception e) {
                log.error(e);
                throw new LogicException("Error while uploading file");
            }
        }
        throw new LogicException("The file is empty!");
    }

    private Attachment saveToDatabase(Integer trainerId, DtoAttachment dtoAttachment, String name, String filePath) {
        Attachment newAttachment = new Attachment(filePath, name,
                trainerId, dtoAttachment.getDescription());
        attachmentDao.insert(newAttachment);
        return newAttachment;
    }

    private String getFilePath(String name) {
        String rootPath = "src/main/resources";
        File dir = new File(rootPath + File.separator + "attachments" );
        if (!dir.exists()) {
            if(!dir.mkdirs()){
                throw new LogicException("Error, could not upload file" );
            }
        }
        return dir.getAbsolutePath() + File.separator + name;
    }

    private void saveToDisk(MultipartFile multipartFile, String filePath) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        if (attachmentDao.getByName(filePath) == null) {
            String fileName = multipartFile.getOriginalFilename();
            log.info("File is not in base");
            StringBuilder name = new StringBuilder(fileName);
            int dot = name.lastIndexOf(".");
            String format = name.substring(dot - 1);
            String tmpFilePath = getFilePath("tmp." + format);
            File uploadedFile = new File(tmpFilePath);
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile))) {
                stream.write(bytes);
            }
            try(FileInputStream stream = new FileInputStream(tmpFilePath)) {
                fileService.uploadFileToServer(filePath,fileName,stream);
            }
            catch (FileNotFoundException e){
                log.error("Error while sending file to server");
                log.error(e);
            }
            File file = new File(tmpFilePath);
            file.delete();
        }
    }

    @Override
    public InputStream downloadFile(Integer id) {
        Attachment attachment = attachmentDao.getEntityById(id);
        String path = attachment.getUrl() + "/" + attachment.getName();

        return fileService.downloadFileFromServer(path);
    }

    @Override
    public void link(Integer lessonId, Integer attachmentId) {
        unlink(lessonId,attachmentId);
        LessonAttachment lessonAttachment = new LessonAttachment(attachmentId, lessonId);
        lessonAttachmentDao.insert(lessonAttachment);
    }

    @Override
    public void unlink(Integer lessonId, Integer attachmentId) {
        lessonAttachmentDao.unlink(lessonId, attachmentId);
    }

}
