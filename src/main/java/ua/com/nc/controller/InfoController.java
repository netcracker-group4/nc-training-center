package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.nc.dao.interfaces.CourseStatusDao;
import ua.com.nc.dao.interfaces.LevelDao;
import ua.com.nc.domain.CourseStatus;
import ua.com.nc.domain.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Controller
@RequestMapping(value = "/api/getInfo")
public class InfoController {
    @Autowired
    private LevelDao levelDao;
    @Autowired
    private CourseStatusDao status;

    @RequestMapping(value = "/getLevels", method = RequestMethod.GET)
    public ResponseEntity<List<Level>> getLevels() {
        return ResponseEntity.ok(levelDao.getAll());
    }

    @RequestMapping(value = "/getLevel/{id}", method = RequestMethod.GET)
    public ResponseEntity<Level> getLevel(@PathVariable Integer id) {
        return ResponseEntity.ok(levelDao.getEntityById(id));
    }


    @RequestMapping(value = "/getStatus/{id}", method = RequestMethod.GET)
    public ResponseEntity<CourseStatus> getStatus(@PathVariable Integer id) {
        return ResponseEntity.ok(status.getCourseStatusById(id));
    }

    @RequestMapping(value = "/getStatuses", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAllStatuses() {
        ua.com.nc.domain.CourseStatus[] statuses = ua.com.nc.domain.CourseStatus.values();
        List<String> list = new ArrayList<>();
        Arrays.stream(statuses).forEach(s -> list.add(s.getName()));
        return ResponseEntity.ok(list);
    }
}
