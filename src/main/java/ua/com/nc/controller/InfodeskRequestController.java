package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.nc.domain.Problem;
import ua.com.nc.service.InfodeskRequestService;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/api/requests")
public class InfodeskRequestController {

    @Autowired
    private InfodeskRequestService infodeskRequestService;

    @RequestMapping(value = {"/create-request"}, method = RequestMethod.POST)
    public ResponseEntity<?> createRequest(@RequestParam(name = "userId") String userId,
                                           @RequestParam(name = "description") String description,
                                           @RequestParam(name = "message") String message,
                                           @RequestParam(name = "requestType") String requestType) {
        int id = Integer.parseInt(userId);
        infodeskRequestService.createRequest(id, description, message, requestType);
        return ResponseEntity.ok().body("Request is created");
    }

    @RequestMapping(value = {"/get-all-requests"}, method = RequestMethod.GET)
    public ResponseEntity<List<Problem>> getAllRequests() {
        return ResponseEntity.ok(infodeskRequestService.getAllRequests());
    }

    @RequestMapping(value = {"/statuses"}, method = RequestMethod.GET)
    public ResponseEntity<?> getStatuses() {
        return ResponseEntity.ok().body(infodeskRequestService.getStatuses());
    }


    @RequestMapping(value = {"/update-request-type"}, method = RequestMethod.PUT)
    public ResponseEntity<?> updateRequestType(@RequestParam(name = "requestId") String requestId,
                                               @RequestParam(name = "requestType") String requestType) {
        int id = Integer.parseInt(requestId);
        infodeskRequestService.updateRequestType(id, requestType);
        return ResponseEntity.ok("Request is updated");
    }

    @RequestMapping(value = {"get-requests-by-user"}, method = RequestMethod.GET)
    public ResponseEntity<List<Problem>> getRequestsByUserId(@RequestParam(name = "userId") Integer userId) {
        return ResponseEntity.ok(infodeskRequestService.getRequestsByUserId(userId));
    }
}
