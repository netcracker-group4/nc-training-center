package ua.com.nc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.interfaces.IGroupDao;
import ua.com.nc.domain.schedule.GroupSchedule;
import ua.com.nc.service.GroupsService;

import java.io.IOException;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private IGroupDao groupDao;
    @Autowired
    private GroupsService  groupsService;
    private final Gson gson = new Gson();



    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String  addGroup(@RequestBody GroupSchedule groupSchedule) {
        int newId;
        if(groupSchedule.getId() == 0){
            newId = groupsService.add(groupSchedule);
        }else {
            newId = groupsService.update(groupSchedule);
        }
        return Integer.toString(newId);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public String  deleteGroup(@PathVariable String id) {
        groupsService.delete(Integer.parseInt(id));
        return "Group deleted";
    }

    @RequestMapping(value = "/get-all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(groupsService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-groups/{id}")
    @ResponseBody
    public String getGroupsInCourse(@PathVariable String id){
        return gson.toJson(groupDao.getAllGroupsOfCourse(Integer.parseInt(id)));
    }

}
