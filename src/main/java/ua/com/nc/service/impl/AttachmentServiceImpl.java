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

    /**
     * saves to database information (given by entity) about uploaded file and its link to lesson with given id
     *
     * @param lessonId id of lesson to which this attachment should be linked
     * @param attachment  object that represents the file
     */
    @Override
    public void add(Integer lessonId, Attachment attachment) {

        if (attachmentDao.getByUrl(attachment.getUrl()) == null) {

            attachmentDao.insert(attachment);
        }
        link(lessonId, attachmentDao.getByUrl(attachment.getUrl()).getId());
    }

    /**
     * saves to database information (given by params) about uploaded file and its link to lesson with given id
     *
     * @param lessonId id of lesson to which this attachment should be linked
     * @param url path to the file on the server
     * @param name original name of uploaded file
     * @param description description given to the attachment
     * @param trainerId id of trainer who is uploading the file
     */

    @Override
    public void add(Integer lessonId, String url, String name, Integer trainerId, String description) {
        Attachment attachment = new Attachment(url, name, trainerId, description);
        add(lessonId, attachment);
    }

    /**
     * returns all attachments that are now attached to lesson plus all attachments
     * that are owned by the trainer that is editing the lesson at the moment
     *
     * @param trainerId id of trainer who is uploading the file
     * @param dtoAttachment  object with file MultipartFile, lessonId and description
     * @return saved into database object that represents the file
     */

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

    /**
     * saves information about uploaded file to the database
     *
     * @param trainerId id of trainer who is uploading the file
     * @param dtoAttachment  object with file MultipartFile, lessonId and description
     * @param name original name of uploaded file
     * @param filePath  path to the file on the server
     * @return saved into database object that represents the file
     */

    private Attachment saveToDatabase(Integer trainerId, DtoAttachment dtoAttachment, String name, String filePath) {
        Attachment newAttachment = new Attachment(filePath, name,
                trainerId, dtoAttachment.getDescription());
        attachmentDao.insert(newAttachment);
        return newAttachment;
    }

    /**
     * returns path to the local file
     *
     * @param name name of the file in local repository
     * @return path to the file
     */

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

    /**
     * saves file to the repository on the FTP server
     *
     * @param multipartFile file that is being saved
     * @param filePath path to the location where file should be saved
     */

    private void saveToDisk(MultipartFile multipartFile, String filePath) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        if (attachmentDao.getByUrl(filePath+ "/" + multipartFile.getOriginalFilename()) == null) {
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
            catch (FileNotFoundException e) {
                log.error("Error while sending file to server");
                log.error(e);
            }
            File file = new File(tmpFilePath);
            file.delete();
        }
    }
    /**
     * returns input stream from the FTP server with the file of given id
     *
     * @param id id of of file to download
     * @return stream containing file that is downloaded
     */

    @Override
    public InputStream downloadFile(Integer id) {
        Attachment attachment = attachmentDao.getEntityById(id);
        String path = attachment.getUrl() + "/" + attachment.getName();

        return fileService.downloadFileFromServer(path);
    }

    /**
     * creates link between attachment with given id and lesson with given id
     *
     * @param lessonId id of lesson
     * @param attachmentId id of attachment
     */

    @Override
    public void link(Integer lessonId, Integer attachmentId) {
        unlink(lessonId,attachmentId);
        LessonAttachment lessonAttachment = new LessonAttachment(attachmentId, lessonId);
        lessonAttachmentDao.insert(lessonAttachment);
    }

    /**
     * deletes link between attachment with given id and lesson with given id
     *
     * @param lessonId id of lesson
     * @param attachmentId id of attachment
     */

    @Override
    public void unlink(Integer lessonId, Integer attachmentId) {
        lessonAttachmentDao.unlink(lessonId, attachmentId);
    }

}
