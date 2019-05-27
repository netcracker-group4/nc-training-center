package ua.com.nc.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.interfaces.AttachmentDao;
import ua.com.nc.domain.Attachment;
import ua.com.nc.domain.LessonAttachment;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoAttachment;
import ua.com.nc.exceptions.LogicException;
import ua.com.nc.exceptions.NotFoundException;
import ua.com.nc.service.AttachmentService;
import ua.com.nc.service.RoleService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Log4j2
@Controller
@RequestMapping("/api/attachments")
public class AttachmentController {
    @Autowired
    private AttachmentDao attachmentDao;
    @Autowired
    private AttachmentService service;
    @Autowired
    private RoleService roleService;

    /**
     * returns all attachments that are linked to the lesson at the moment
     *
     * @return list of available attachments
     */

    @RequestMapping(method = RequestMethod.GET, value = "/lesson/{id}")
    public ResponseEntity<List<Attachment>> getLessonAttachments(@PathVariable Integer id) {
        return ResponseEntity.ok(attachmentDao.getByLessonId(id));
    }

    /**
     * returns all attachments that are owned by the trainer
     *
     * @param user     logged in user
     * @return list of available attachments
     */

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity<List<Attachment>> getAllAttachments(@AuthenticationPrincipal User user) {
        if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.ADMIN)) {
            return ResponseEntity.ok(attachmentDao.getAll());
        } else if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.TRAINER)) {
            return ResponseEntity.ok(attachmentDao.getByTrainerId(user.getId()));
        }
        return null;
    }

    /**
     * returns all attachments that are now attached to lesson plus all attachments
     * that are owned by the trainer that is editing the lesson at the moment
     *
     * @param user     logged in user
     * @param lessonId lesson that is edited
     * @return list of available attachments
     */

    @RequestMapping(method = RequestMethod.GET, value = "/all/{lessonId}")
    public ResponseEntity<List<Attachment>> getAllAttachmentsForLessonAndTrainer(@AuthenticationPrincipal User user,
                                                                                 @PathVariable Integer lessonId) {
        List<Attachment> attachmentList = new ArrayList<>();
        if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.ADMIN)) {
            attachmentList.addAll(attachmentDao.getAll());
        } else if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.TRAINER)) {
            attachmentList.addAll(attachmentDao.getByTrainerId(user.getId()));
        }
        attachmentList.addAll(attachmentDao.getByLessonId(lessonId));
        return ResponseEntity.ok(attachmentList);
    }

    /**
     * returns attachment by given id
     *
     * @param id file id
     * @return response entity with json representing file
     */

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<?> getAttachment(@PathVariable Integer id) {
        return ResponseEntity.ok(attachmentDao.getEntityById(id));
    }

    /**
     * uploads the file sent from the front-end
     *
     * @param dtoAttachment object with file MultipartFile, lessonId and description
     * @param user          authenticated user to whom profile upload the file
     * @return saved into database object that represents the file
     */

    @RequestMapping(method = RequestMethod.POST, value = "/lesson/upload-file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Attachment> uploadFile(@ModelAttribute DtoAttachment dtoAttachment,
                                                 @AuthenticationPrincipal User user) {
        log.debug(dtoAttachment);
        if (user != null) {
            List<Role> roles = roleService.findAllByUserId(user.getId());
            if (roles.contains(Role.TRAINER) || roles.contains(Role.ADMIN)) {
                return ResponseEntity.ok(
                        service.uploadFile(user.getId(), dtoAttachment));
            } else
                throw new LogicException("Could not upload file");
        } else
            throw new LogicException("Could not upload file");
    }
    /**
     * deletes link between attachment with given id and lesson with given id
     *
     * @param lessonAttachment object with attachmentId, lessonId
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/unlink")
    public void unlink(@RequestBody LessonAttachment lessonAttachment) {
        service.unlink(lessonAttachment.getLessonId(), lessonAttachment.getAttachmentId());
    }

    /**
     * creates link between attachment with given id and lesson with given id
     *
     * @param lessonId id of lesson
     * @param attachmentId id of attachment
     */

    @RequestMapping(method = RequestMethod.POST, value = "/link")
    public void linkFile(@RequestParam("lessonId") Integer lessonId,
                         @RequestParam("attachmentId") Integer attachmentId) {
        service.link(lessonId, attachmentId);
    }

    /**
     * uploads the file sent from the front-end
     *
     * @param id attachment id
     * @return response entity containing file which id was given
     */

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Integer id) {
        InputStream in = service.downloadFile(id);
        HttpHeaders headers = new HttpHeaders();
        Attachment attachment = attachmentDao.getEntityById(id);
        if (attachment == null) {
            throw new NotFoundException("Attachment not found");
        }
        String headerValue = "attachment; filename = " + attachment.getName();
        headers.add("Content-Disposition",
                headerValue);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }
}
