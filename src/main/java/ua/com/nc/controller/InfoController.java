package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.dao.interfaces.CourseStatusDao;
import ua.com.nc.dao.interfaces.LevelDao;
import ua.com.nc.domain.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Controller
@RequestMapping(value = "/api/getInfo")
public class InfoController {
    private final Gson gson = new Gson();
    @Autowired
    private LevelDao levelDao;
    @Autowired
    private CourseStatusDao status;

    @RequestMapping(value = "/getLevels", method = RequestMethod.GET)
    @ResponseBody
    public String getLevels() {
        return gson.toJson(levelDao.getAll());
    }

    @RequestMapping(value = "/getLevel/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Level getLevel(@PathVariable Integer id) {
        return levelDao.getEntityById(id);
    }


    @RequestMapping(value = "/getStatus/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStatus(@PathVariable Integer id) {
        return gson.toJson(status.getCourseStatusById(id));
    }

    @RequestMapping(value = "/getStatuses", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAllStatuses() {
        ua.com.nc.domain.CourseStatus[] statuses = ua.com.nc.domain.CourseStatus.values();
        List<String> list = new ArrayList<>();
        Arrays.stream(statuses).forEach(s -> list.add(s.getName()));
        return list;
    }
}
