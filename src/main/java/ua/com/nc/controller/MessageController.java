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
import java.sql.Timestamp;
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
    public String getMessage(String json){
        Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd.HH.mm.ss").serializeNulls().create();
        //log.debug(chatId + " " + senderId + " " + text);
        //Message message = new Message(chatId, senderId, new Timestamp(System.currentTimeMillis()), text);
        Message message = gson.fromJson(json, Message.class);
        message.setDateTime(new Timestamp(System.currentTimeMillis()));
        Integer messageId = chatService.addMessage(message, null);
        message.setId(messageId);
        log.debug(message);
        return gson.toJson(message);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/messages")
    public ResponseEntity<?> getAll(@RequestParam(name="chatId") Integer chatId){
        Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd.HH.mm.ss").serializeNulls().create();
        List<Message> messages = messageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok().body(gson.toJson(messages));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/messages")
    public ResponseEntity<?> addMessage(@RequestParam(name="text") String text,
                                        @RequestParam(name="senderId") Integer senderId,
                                        @RequestParam(required = false, name="receiverId") Integer receiverId){

        Message message = new Message(null, senderId, new Timestamp(System.currentTimeMillis()), text);
        chatService.addMessage(message, receiverId);
        return ResponseEntity.ok().body("Message added");
    }

}
