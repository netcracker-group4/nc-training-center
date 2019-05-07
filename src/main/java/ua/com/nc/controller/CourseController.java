package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.implementation.CourseDao;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.domain.Course;
import ua.com.nc.service.CourseService;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/getcourses")
public class CourseController {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseService service;
    @Autowired
    private IUserDao userDao;

    private final Gson gson = new Gson();


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getCourses() {
        return gson.toJson(courseDao.getAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public String getCourse(@PathVariable String id) {
        return gson.toJson(courseDao.getEntityById(Integer.parseInt(id)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteCourse(@PathVariable String id) {
        courseDao.delete(Integer.parseInt(id));
        courseDao.commit();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    @ResponseBody
    public void add(@RequestParam(name = "name") String name, @RequestParam(name = "level") String level,
                    @RequestParam(name = "courseStatus") String courseStatus, @RequestParam(name = "imageUrl") String imageUrl,
                    @RequestParam(name = "isOnLandingPage") String isOnLandingPage, @RequestParam(name = "description") String desc,
                    @RequestParam(name = "startDay") String startDay, @RequestParam(name = "endDay") String endDay,
                    @RequestParam(name = "image") MultipartFile image) {
        imageUrl = service.uploadImage(image);
        service.add(service.stringToObjCourse(name, "1", level, courseStatus,
                imageUrl, isOnLandingPage, desc, startDay, endDay));
    }


    @RequestMapping(method = RequestMethod.PUT, value = "{id}/create")
    @ResponseBody
    public void update(@RequestParam(name = "name") String name, @RequestParam(name = "level") String level,
                       @RequestParam(name = "courseStatus") String courseStatus, @RequestParam(name = "imageUrl") String imageUrl,
                       @RequestParam(name = "image") MultipartFile image,
                       @RequestParam(name = "isOnLandingPage") String isOnLandingPage, @RequestParam(name = "description") String desc,
                       @RequestParam(name = "startDay") String startDay, @RequestParam(name = "endDay") String endDay,
                       @PathVariable int id) {
        imageUrl = service.uploadImage(image);
        Course course = service.stringToObjCourse(name, "1", level, courseStatus,
                imageUrl, isOnLandingPage, desc, startDay, endDay);
        course.setId(id);
        courseDao.update(course);
    }

    @RequestMapping(value = {"/{id}/desired/ungrouped"}, method = RequestMethod.GET)
    @ResponseBody
    public String getDesiredScheduleForUngroupedStudentsForCourse(@PathVariable("id") String id) throws Exception {
        return service.getDesiredScheduleForUngroupedStudentsOfCourse(Integer.parseInt(id));
    }

    @RequestMapping(value = {"/{id}/desired/grouped"}, method = RequestMethod.GET)
    @ResponseBody
    public String getDesiredScheduleForFormedGroupsForCourse(@PathVariable("id") String id) throws Exception {
        return service.getDesiredScheduleForFormedGroupsForCourse(Integer.parseInt(id));
    }

    @RequestMapping(value = {"/desired/{groupId}"}, method = RequestMethod.GET)
    @ResponseBody
    public String getDesiredScheduleForGroup(@PathVariable("groupId") String groupId) throws Exception {
        return service.getDesiredScheduleForGroup(Integer.parseInt(groupId));
    }

    @RequestMapping(value = {"/desired/day-intervals"}, method = RequestMethod.GET)
    @ResponseBody
    public String getDayIntervals() {
        return service.getDayIntervals();
    }

    @RequestMapping(value = "/{id}/trainer", method = RequestMethod.GET)
    @ResponseBody
    public String getTrainer(@PathVariable String id) {
        return gson.toJson(userDao.getTrainersOnCourse(Integer.parseInt(id)));
    }

}
