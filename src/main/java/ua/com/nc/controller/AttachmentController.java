package ua.com.nc.controller;


import com.google.gson.Gson;
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


    private final Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.GET, value = "/lesson/{id}")
    @ResponseBody
    public String getLessonAttachments(@PathVariable Integer id) {
        return gson.toJson(attachmentDao.getByLessonId(id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    @ResponseBody
    public String getAllAttachments(@AuthenticationPrincipal User user) {
        if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.ADMIN)){
            return gson.toJson(attachmentDao.getAll());
        }
        else if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.TRAINER)){
            return gson.toJson(attachmentDao.getByTrainerId(user.getId()));
        }
        return null;
    }

    /**
     * returns all attachments that are now attached to lesson plus all attachments
     * that are owned by the trainer that is editing the lesson at the moment
     * @param user logged in user
     * @param lessonId lesson that is edited
     * @return list of available attachments
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all/{lessonId}")
    @ResponseBody
    public String getAllAttachmentsForLessonAndTrainer(@AuthenticationPrincipal User user, @PathVariable Integer lessonId) {
        List<Attachment> attachmentList = new ArrayList<>();
        if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.ADMIN)) {
            attachmentList.addAll(attachmentDao.getAll());
        } else if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.TRAINER)) {
            attachmentList.addAll(attachmentDao.getByTrainerId(user.getId()));
        }
        attachmentList.addAll(attachmentDao.getByLessonId(lessonId));
        return gson.toJson(attachmentList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public String getAttachment(@PathVariable Integer id) {
        return gson.toJson(attachmentDao.getEntityById(id));
    }

    /**
     * uploads the file sent from the front-end
     * @param dtoAttachment object with file MultipartFile, lessonId and description
     * @param user authenticated user to whom profile upload the file
     * @return saved into database object that represents the file
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/lesson/upload-file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String uploadFile(@ModelAttribute  DtoAttachment dtoAttachment,
                             @AuthenticationPrincipal User user) {
        log.debug(dtoAttachment);
        if(user != null ){
            List<Role> roles = roleService.findAllByUserId(user.getId());
            if (roles.contains(Role.TRAINER) || roles.contains(Role.ADMIN)) {
                return gson.toJson(service.uploadFile(user.getId(), dtoAttachment));
            }
            else
                throw new LogicException("Could not upload file");
        }
        else
            throw new LogicException("Could not upload file");
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteAttachment(@PathVariable Integer id) {
        service.delete(id);
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/unlink")
    @ResponseBody
    public void unlink(@RequestBody LessonAttachment lessonAttachment) {
        service.unlink(lessonAttachment.getLessonId(),lessonAttachment.getAttachmentId());
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/link")
    public void linkFile(@RequestParam("lessonId") Integer lessonId, @RequestParam("attachmentId") Integer attachmentId) {
        service.link(lessonId, attachmentId);
    }

    @ResponseBody
    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Integer id) {
        InputStream in = service.downloadFile(id);

        HttpHeaders headers = new HttpHeaders();
        Attachment attachment = attachmentDao.getEntityById(id);
        String headerValue = "attachment; filename = " + attachment.getName();
        headers.add("Content-Disposition",
                headerValue);

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }
}
