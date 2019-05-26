package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.domain.Course;
import ua.com.nc.service.CourseService;

import java.io.InputStream;

@Log4j2
@Controller
@Validated
public class FileController {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseService courseService;


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/img/{imageName}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String imgName) {
        InputStream in = courseService.getImage(imgName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "image; filename = "+imgName);

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }
}
