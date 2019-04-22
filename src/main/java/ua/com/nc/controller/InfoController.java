package ua.com.nc.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.dao.implementation.LevelDao;

@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping(value = "/getInfo")
public class InfoController {
    @Autowired
    private LevelDao levelDao;

    private final Gson gson = new Gson();

    @RequestMapping(value="/getLevels",method = RequestMethod.GET)
    @ResponseBody
    public String getLevels(){
        return gson.toJson(levelDao.getAll());
    }
}
