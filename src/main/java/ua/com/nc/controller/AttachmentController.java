package ua.com.nc.controller;


import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.implementation.AttachmentDao;
import ua.com.nc.service.AttachmentService;


@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/attachments")
public class AttachmentController {
    @Autowired
    AttachmentDao attachmentDao;
    @Autowired
    AttachmentService service;
    private final Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.GET,value="/lesson/{id}")
    @ResponseBody
    public String getLessonAttachments(@PathVariable String id){
        return gson.toJson(attachmentDao.getByLessonId(Integer.parseInt(id)));
    }

    @RequestMapping(method = RequestMethod.GET,value="/all")
    @ResponseBody
    public String getAllAttachments() {
        return gson.toJson(attachmentDao.getAll());
    }

    @RequestMapping(method = RequestMethod.GET,value="/{id}")
    @ResponseBody
    public String getReason(@PathVariable String id){
        return gson.toJson(attachmentDao.getEntityById(Integer.parseInt(id)));
    }

    @RequestMapping(method = RequestMethod.POST,value = "/upload-file")
    public void uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("lessonId") String lessonId){
        service.uploadFile(Integer.parseInt(lessonId),file);
    }
}
