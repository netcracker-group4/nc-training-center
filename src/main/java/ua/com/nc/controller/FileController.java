package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.domain.Course;
import ua.com.nc.service.CourseService;
import ua.com.nc.service.FileTransferService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Log4j2
@Controller
@Validated
public class FileController {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseService courseService;
    @Autowired
    private FileTransferService fileTransferService;


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/img/{imageName}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String imgName) {
        InputStream in = courseService.getImage(imgName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "image; filename = "+imgName);

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/files-img")
    public ResponseEntity<?> getImgFromFtp(final HttpServletResponse response,
                                    @RequestParam String url){

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        byte[] media = fileTransferService.getImage(response, url);
        return new ResponseEntity<>(media, headers, HttpStatus.OK);
    }
}
