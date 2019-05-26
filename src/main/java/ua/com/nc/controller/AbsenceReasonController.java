package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.nc.dao.interfaces.AbsenceReasonDao;
import ua.com.nc.domain.AbsenceReason;
import ua.com.nc.service.AbsenceReasonService;
import ua.com.nc.service.SerializationService;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/api/absence-reason")
public class AbsenceReasonController {
    @Autowired
    private AbsenceReasonDao absenceReasonDao;
    @Autowired
    private AbsenceReasonService service;
    @Autowired
    private SerializationService serializationService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<AbsenceReason>> getReasons() {
        return ResponseEntity.ok(absenceReasonDao.getAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<AbsenceReason> getReason(@PathVariable Integer id) {
        AbsenceReason absenceReason = absenceReasonDao.getEntityById(id);
        return ResponseEntity.ok(absenceReason);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> deleteReason(@PathVariable Integer id) {
        absenceReasonDao.delete(id);
        return ResponseEntity.ok("Deleted");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody AbsenceReason absenceReason) {
        log.info("absenceReason from RequestBody  " + absenceReason);
        return ResponseEntity.ok(service.add(absenceReason));
    }

}
