package ua.com.nc.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.implementation.LevelDaoImpl;
import ua.com.nc.dao.interfaces.CourseStatus;
import ua.com.nc.dao.interfaces.LevelDao;
import ua.com.nc.domain.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping(value = "/getInfo")
public class InfoController {
    private final Gson gson = new Gson();
    @Autowired
    private LevelDao levelDao;
    @Autowired
    private CourseStatus status;

    @RequestMapping(value = "/getLevels", method = RequestMethod.GET)
    @ResponseBody
    public String getLevels() {
        return gson.toJson(levelDao.getAll());
    }

    @RequestMapping(value = "/getLevel/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Level getLevel(@PathVariable String id) {
        return levelDao.getEntityById(Integer.parseInt(id));
    }


    @RequestMapping(value = "/getStatus/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStatus(@PathVariable String id) {
        return gson.toJson(status.getCourseStatusById(Integer.parseInt(id)));
    }

    @RequestMapping(value = "/getStatuses", method = RequestMethod.GET)
    public String getAllStatuses() {
        ua.com.nc.domain.CourseStatus[] statuses = ua.com.nc.domain.CourseStatus.values();
        List<String> list = new ArrayList<>();
        Arrays.stream(statuses).forEach(s -> list.add(s.getName()));
        return gson.toJson(list);
    }
    /*@RequestMapping(value = "courses/src/main/resources/img/{imageName}", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String imageName) throws IOException {
        InputStream in = getClass()
                .getResourceAsStream("/src/main/resources/img/"+imageName);
        return IOUtils.toByteArray(in);
    }*/
}
