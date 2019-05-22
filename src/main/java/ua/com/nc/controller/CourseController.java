package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.dao.interfaces.UserGroupDao;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.dto.CourseAndGroups;
import ua.com.nc.service.CourseService;
import ua.com.nc.service.DashBoardService;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/api/getcourses")
public class CourseController {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserDao userDao;
    /*@Autowired
    private SuitabilityDao suitabilityDao;*/

    private final Gson gson = new Gson();
    @Autowired
    private UserGroupDao userGroupDao;

    @Autowired
    private DashBoardService dashBoardService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<CourseAndGroups> getCourses() {
        return dashBoardService.getTrainingAndQuantity();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public Course getCourse(@PathVariable String id) {
        return courseDao.getEntityById(Integer.parseInt(id));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public void deleteCourse(@PathVariable String id) {
        courseDao.delete(Integer.parseInt(id));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public void add(@RequestParam(name = "name") String name, @RequestParam(name = "level") String level,
                    @RequestParam(name = "courseStatus") String courseStatus, @RequestParam(name = "imageUrl") String imageUrl,
                    @RequestParam(name = "isOnLandingPage") String isOnLandingPage, @RequestParam(name = "description") String desc,
                    @RequestParam(name = "startDay") String startDay, @RequestParam(name = "endDay") String endDay,
                    @RequestParam(name = "image") MultipartFile image) {
        imageUrl = courseService.uploadImage(image);
        System.err.println(imageUrl);
        courseService.add(courseService.stringToObjCourse(name, "1", level, courseStatus,
                imageUrl, isOnLandingPage, desc, startDay, endDay));
    }
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.POST, value = "/upload-img")
    public String uploadFile(@RequestParam("file") MultipartFile img) {
        return courseService.uploadImage(img);
    }
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/upload-img")
    public String uploadFile(@RequestParam("img") MultipartFile img,@PathVariable String id) {
        return courseService.uploadImage(img, Integer.parseInt(id));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public void update(@RequestParam(name = "name") String name, @RequestParam(name = "level") String level,
                       @RequestParam(name = "courseStatus") String courseStatus, @RequestParam(name = "imageUrl") String imageUrl,
                       @RequestParam(name = "image") MultipartFile image,
                       @RequestParam(name = "isOnLandingPage") String isOnLandingPage, @RequestParam(name = "description") String desc,
                       @RequestParam(name = "startDay") String startDay, @RequestParam(name = "endDay") String endDay,
                       @PathVariable int id) {
        imageUrl = courseService.uploadImage(image);
        Course course = courseService.stringToObjCourse(name, "1", level, courseStatus,
                imageUrl, isOnLandingPage, desc, startDay, endDay);
        course.setId(id);
        courseDao.update(course);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public void edit(@RequestParam(name = "name") String name, @RequestParam(name = "level") String level,
                       @RequestParam(name = "courseStatus") String courseStatus,
                       @RequestParam(name = "isOnLandingPage") String isOnLandingPage, @RequestParam(name = "description") String desc,
                       @RequestParam(name = "startDay") String startDay, @RequestParam(name = "endDay") String endDay,
                       @PathVariable int id) {
        courseService.edit(id,name, level, courseStatus,
                isOnLandingPage, desc, startDay, endDay);
    }



    @RequestMapping(value = "/{id}/trainer", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getTrainer(@PathVariable Integer id) {
        return userDao.getTrainersOnCourse(id);
    }

    @RequestMapping(value = "/{id}/course-group", method = RequestMethod.GET)
    @ResponseBody
    public String getCourseUser(@AuthenticationPrincipal User user,@PathVariable Integer id) {
        return Boolean.toString(userGroupDao.getByUserAndCourse(user.getId(), id) != null);
    }

    @RequestMapping(value = "/trainer/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getTrainersCourses(@PathVariable Integer id) {
        return gson.toJson(courseDao.getAllByTrainer(id));
    }

    @RequestMapping(value = "/get-all-courses-by-trainer-and-employee", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCoursesByTrainerAndByEmployee(@RequestParam Integer trainerId,
                                                                 @RequestParam Integer employeeId) {
        return new ResponseEntity<>(courseService.getAllByTrainerAndEmployee(trainerId, employeeId), HttpStatus.OK);
    }




}
