package ua.com.nc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.domain.Chat;
import ua.com.nc.domain.User;
import ua.com.nc.service.ChatService;
import ua.com.nc.service.SerializationService;

import java.util.List;

@Controller
@RequestMapping("/api/chats")
public class ChatController {

    @Value("${chat.page-size}")
    private Integer pageSize;

    @Autowired
    private ChatService chatService;

    @Autowired
    private SerializationService serializationService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@AuthenticationPrincipal User user){
        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").serializeNulls().create();
        List<Chat> chats = chatService.getChatsByUserId(user.getId());
        String jsonChats = gson.toJson(chats);
        return new ResponseEntity<>(jsonChats, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/page-size")
    public ResponseEntity<?> getPageSize(){
        return ResponseEntity.ok().body(serializationService.serializeWithDateFormat(pageSize));
    }

}
