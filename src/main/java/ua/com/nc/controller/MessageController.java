package ua.com.nc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.nc.dao.interfaces.ChatDao;
import ua.com.nc.domain.Message;
import ua.com.nc.service.ChatService;
import ua.com.nc.service.MessageService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j
@Controller
public class MessageController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/message")
    @SendTo("/topic/msg")
    public String getMessage(String text){

        return text;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/messages")
    public ResponseEntity<?> getAll(@RequestParam(name="chatId") Integer chatId){
        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").serializeNulls().create();
        List<Message> messages = messageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok().body(gson.toJson(messages));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/messages")
    public ResponseEntity<?> addMessage(@RequestParam(name="text") String text,
                                        @RequestParam(name="senderId") Integer senderId,
                                        @RequestParam(required = false, name="receiverId") Integer receiverId){

        Message message = new Message(null, senderId, new Date(System.currentTimeMillis()), text);
        chatService.addMessage(message, receiverId);
        return ResponseEntity.ok().body("Message added");
    }

}
