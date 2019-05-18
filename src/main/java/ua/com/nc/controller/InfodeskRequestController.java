package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.service.InfodeskRequestService;

@Log4j
@Controller
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/requests")
public class InfodeskRequestController {

    @Autowired
    private InfodeskRequestService infodeskRequestService;

    private Gson gson = new Gson ();

    @RequestMapping (value={"/create-request"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> createRequest (@RequestParam (name = "userId") String userId,
                                            @RequestParam (name = "description") String description,
                                            @RequestParam (name = "message") String message) {
        int id = Integer.parseInt (userId);
        infodeskRequestService.createRequest(id, description, message);
        return ResponseEntity.ok().body("Your request is created");
    }

    @RequestMapping(value = {"/statuses"}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getStatuses() {
        return ResponseEntity.ok().body(infodeskRequestService.getStatuses());
    }




}
