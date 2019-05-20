package ua.com.nc.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.service.InfodeskRequestService;

@Log4j2
@Controller
@RequestMapping("/api/requests")
public class InfodeskRequestController {

    @Autowired
    private InfodeskRequestService infodeskRequestService;

    private Gson gson = new Gson ();

//    @RequestMapping (value={"/create-request"}, method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity<?> createRequest (@RequestParam (name = "userId") String userId,
//                                            @RequestParam (name = "description") String description,
//                                            @RequestParam (name = "message") String message) {
//        int id = Integer.parseInt (userId);
//        infodeskRequestService.createRequest(id, description, message);
//        return ResponseEntity.ok().body("Your request is created");
//    }

    @RequestMapping (value={"/create-request"}, method = RequestMethod.POST)
    @ResponseBody
    public String createRequest (@RequestParam (name = "userId") String userId,
                                 @RequestParam (name = "description") String description,
                                 @RequestParam (name = "message") String message) {
        int id = Integer.parseInt (userId);
        return gson.toJson(infodeskRequestService.createRequest(id, description, message));
    }

    @RequestMapping (value={"/get-requests"}, method = RequestMethod.GET)
    @ResponseBody
    public String getAllRequestsOfType
            (@RequestParam (name = "requestType") String requestType) {
        return gson.toJson(infodeskRequestService.getAllRequestsOfType (requestType));
    }

    @RequestMapping(value = {"/statuses"}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getStatuses() {
        return ResponseEntity.ok().body(infodeskRequestService.getStatuses());
    }


    @RequestMapping (value={"/change-request-type"}, method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<?> updateRequestType (@RequestParam (name = "requestId") String requestId,
                                                @RequestParam (name = "requestType") String requestType) {
        int id = Integer.parseInt(requestId);
        infodeskRequestService.updateRequestType(id, requestType);
        return ResponseEntity.ok().body("Request is updated");
    }
}
