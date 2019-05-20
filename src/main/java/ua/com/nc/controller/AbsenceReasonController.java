package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.interfaces.AbsenceReasonDao;
import ua.com.nc.domain.AbsenceReason;
import ua.com.nc.service.AbsenceReasonService;

@Log4j2
@Controller
@RequestMapping("/api/absence-reason")
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
    @ResponseBody
    public void deleteReason(@PathVariable String id) {
        absenceReasonDao.delete(Integer.parseInt(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Integer add(@RequestBody AbsenceReason absenceReason) {
        log.info("absenceReason from RequestBody  " + absenceReason);
        return service.add(absenceReason);
    }

}
