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

    @RequestMapping (value={"/create-request"}, method = RequestMethod.POST)
    @ResponseBody
    public String createRequest (@RequestParam (name = "userId") Integer userId,
                                 @RequestParam (name = "description") String description,
                                 @RequestParam (name = "message") String message) {
        return gson.toJson(infodeskRequestService.createRequest(userId, description, message));
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
    public ResponseEntity<?> updateRequestType (@RequestParam (name = "requestId") Integer requestId,
                                                @RequestParam (name = "requestType") String requestType) {
        infodeskRequestService.updateRequestType(requestId, requestType);
        return ResponseEntity.ok().body("Request is updated");
    }
}
