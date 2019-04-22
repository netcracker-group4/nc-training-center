package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.implementation.CourseDao;
import ua.com.nc.dao.implementation.LevelDao;
import ua.com.nc.domain.Level;

import java.util.List;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/getcourses")
public class CourseController {
    @Autowired
    private CourseDao courseDao;

    private final Gson gson = new Gson();


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getCourses(){
       return gson.toJson(courseDao.getAll());
    }
    @RequestMapping(method = RequestMethod.GET,value="/{id}")
    @ResponseBody
    public String getCourse(@PathVariable String id){
       return gson.toJson(courseDao.getEntityById(Integer.parseInt(id)));
    }

    @RequestMapping(method = RequestMethod.DELETE,value="/{id}")
    public void deleteCourse(@PathVariable String id){
        courseDao.delete(Integer.parseInt(id));
    }




}
