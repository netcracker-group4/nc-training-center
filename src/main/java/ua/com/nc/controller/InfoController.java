package ua.com.nc.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.implementation.LevelDao;
import ua.com.nc.dao.interfaces.ICourseStatus;

@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping(value = "/getInfo")
public class InfoController {
    private final Gson gson = new Gson();
    @Autowired
    private LevelDao levelDao;
    @Autowired
    private ICourseStatus status;

    @RequestMapping(value="/getLevels",method = RequestMethod.GET)
    @ResponseBody
    public String getLevels(){
        return gson.toJson(levelDao.getAll());
    }

    @RequestMapping(value = "/getStatus/{id}")
    @ResponseBody
    public String getStatus(@PathVariable String id){
        String res =gson.toJson(status.getCourseStatusById(Integer.parseInt(id)));
        System.err.println(res);
        return res;
    }
}
