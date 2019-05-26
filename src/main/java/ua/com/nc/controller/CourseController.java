package ua.com.nc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.dao.interfaces.UserGroupDao;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.User;
import ua.com.nc.dto.CourseAndGroups;
import ua.com.nc.service.CourseService;
import ua.com.nc.service.DashBoardService;
import ua.com.nc.service.FileTransferService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Log4j2
@Controller
@Validated
@RequestMapping("/api/getcourses")
public class CourseController {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FileTransferService fileTransferService;
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
    public Course getCourse(@PathVariable Integer id) {
        return courseDao.getEntityById(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @ResponseBody
    public void deleteCourse(@PathVariable Integer id) {
        courseDao.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public ResponseEntity add(@RequestParam(name = "name") String name, @RequestParam(name = "level") String level,
                    @RequestParam(name = "courseStatus") String courseStatus, @RequestParam(name = "trainer") String trainer,
                    @RequestParam(name = "isOnLandingPage") String isOnLandingPage, @RequestParam(name = "description") String desc,
                    @RequestParam(name = "startDay") String startDay, @RequestParam(name = "endDay") String endDay,
                    @RequestParam(name = "image") MultipartFile image) {
        String imageUrl = courseService.uploadImage(image);
        if(imageUrl == null){
            return ResponseEntity.status(500).body("Error while creating temp file.");
        }
        courseService.add(courseService.stringToObjCourse(name, trainer, level, courseStatus,
                imageUrl, isOnLandingPage, desc, startDay, endDay));
        return ResponseEntity.ok().body("Course saved");
    }
    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(method = RequestMethod.POST, value = "/upload-img")
    public String uploadFile(@RequestParam("file") MultipartFile img) {
        return courseService.uploadImage(img);
    }

    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/upload-img")
    public String uploadFile(@RequestParam("img") MultipartFile img,@PathVariable String id) {
        return courseService.uploadImage(img, Integer.parseInt(id));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/create")
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    @ResponseBody
    public void update(@RequestParam(name = "name") String name, @RequestParam(name = "level") String level,
                       @RequestParam(name = "courseStatus") String courseStatus, @RequestParam(name = "imageUrl") String imageUrl,
                       @RequestParam(name = "image") MultipartFile image, @RequestParam(name = "trainer") String trainer,
                       @RequestParam(name = "isOnLandingPage") String isOnLandingPage, @RequestParam(name = "description") String desc,
                       @RequestParam(name = "startDay") String startDay, @RequestParam(name = "endDay") String endDay,
                       @PathVariable int id) {
        imageUrl = courseService.uploadImage(image);
        Course course = courseService.stringToObjCourse(name, trainer, level, courseStatus,
                imageUrl, isOnLandingPage, desc, startDay, endDay);
        course.setId(id);
        courseDao.update(course);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/edit")
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
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

    @RequestMapping(value = "/{courseId}/can-join", method = RequestMethod.GET)
    @ResponseBody
    public String getCourseUser(@AuthenticationPrincipal User user,@PathVariable Integer courseId) {
        return Boolean.toString(courseService.canJoinCourse(user, courseId));
    }


    @RequestMapping(value = "/trainer/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getTrainersCourses(@PathVariable Integer id) {
        //this particular response needs this particular date format
        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();
        return gson.toJson(courseDao.getAllByTrainer(id));
    }

    @RequestMapping(value = "/get-all-courses-by-trainer-and-employee", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCoursesByTrainerAndByEmployee(@RequestParam Integer trainerId,
                                                                 @RequestParam Integer employeeId) {
        return new ResponseEntity<>(courseService.getAllByTrainerAndEmployee(trainerId, employeeId), HttpStatus.OK);
    }

    @RequestMapping(value = "/img/{imageName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName,final HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try (InputStream inputStream = fileTransferService.downloadFileFromServer("/img/"+imageName)) {
            if (inputStream != null) {
                byte[] media = IOUtils.toByteArray(inputStream);
                return new ResponseEntity<>(media, headers, HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
    }


}
