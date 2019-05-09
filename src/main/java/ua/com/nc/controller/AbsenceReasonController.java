package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.implementation.AbsenceReasonDao;
import ua.com.nc.service.AbsenceReasonService;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/absence-reason")
public class AbsenceReasonController {
    @Autowired
    private AbsenceReasonDao absenceReasonDao;
    @Autowired
    private AbsenceReasonService service;

    private final Gson gson = new Gson();

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getReasons() {
        return gson.toJson(absenceReasonDao.getAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public String getReason(@PathVariable String id) {
        return gson.toJson(absenceReasonDao.getEntityById(Integer.parseInt(id)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteReason(@PathVariable String id) {
        absenceReasonDao.delete(Integer.parseInt(id));
        absenceReasonDao.commit();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void add(@RequestParam(name = "title") String title) {
        service.add(title);
    }

}
