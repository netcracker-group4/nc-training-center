package ua.com.nc.controller;


import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.interfaces.AttachmentDao;
import ua.com.nc.domain.Attachment;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.service.AttachmentService;
import ua.com.nc.service.RoleService;

import java.io.FileInputStream;


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
    public String getLessonAttachments(@PathVariable String id) {
        return gson.toJson(attachmentDao.getByLessonId(Integer.parseInt(id)));
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

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public String getAttachment(@PathVariable String id) {
        return gson.toJson(attachmentDao.getEntityById(Integer.parseInt(id)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload-file")
    public void uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("lessonId") String lessonId, @RequestParam("descr") String description,
                           @AuthenticationPrincipal User user) {
        if (user != null && roleService.findAllByUserId(user.getId()).contains(Role.TRAINER)){
            service.uploadFile(Integer.parseInt(lessonId),user.getId(), description, file);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteAttachment(@PathVariable String id) {
        service.delete(Integer.parseInt(id));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/unlink")
    public void unlink(@RequestParam("attachmentId") String attachmentId,@RequestParam("lessonId") String lessonId) {
        service.unlink(Integer.parseInt(lessonId),Integer.parseInt(attachmentId));
    }


    @RequestMapping(method = RequestMethod.POST, value = "/link")
    public void linkFile(@RequestParam("lessonId") String lessonId, @RequestParam("attachmentId") String attachmentId) {
        service.link(Integer.parseInt(lessonId), Integer.parseInt(attachmentId));
    }

    @RequestMapping(value = "/download/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileId) {
        Integer attachmentId = Integer.parseInt(fileId);
        FileInputStream in = service.downloadFile(attachmentId);

        HttpHeaders headers = new HttpHeaders();
        Attachment attachment = attachmentDao.getEntityById(attachmentId);
        String headerValue = "attachment; filename = " + attachment.getUrl();
        headers.add("Content-Disposition",
                headerValue);

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }
}
