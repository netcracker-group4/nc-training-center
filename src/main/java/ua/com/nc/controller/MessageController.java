package ua.com.nc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.domain.Chat;
import ua.com.nc.domain.Message;
import ua.com.nc.domain.User;
import ua.com.nc.security.CustomSecurityService;
import ua.com.nc.service.ChatService;
import ua.com.nc.service.MessageService;
import ua.com.nc.service.SerializationService;

import java.sql.Timestamp;
import java.util.List;

@Log4j2
@Controller
public class MessageController {

    @Value("${chat.page-size}")
    private Integer pageSize;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CustomSecurityService customSecurityService;

    @Autowired
    private SerializationService serializationService;

    @MessageMapping("/message")
    @SendTo("/topic/msg")
    public String addMessageAndReturnToChatPage(String jsonMessage){
        Message message = (Message) serializationService.deserialize(jsonMessage, Message.class);
        System.out.println(message);
        Chat chat = chatService.getByUserIdAndChatId(message.getSenderId(), message.getChatId());
        if(chat != null){
            message.setDateTime(new Timestamp(System.currentTimeMillis()));
            Integer messageId = chatService.addMessageToExistingChat(message);
            Message messageFromDb = messageService.getById(messageId);
            return serializationService.serializationWithDateTimeFormat(messageFromDb);
        }else{
            throw new AccessDeniedException("Access denied");
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/api/messages")
    @PreAuthorize("@customSecuritySecurity.hasPermissionToGetChatMessages(authentication, #chatId)")
    public ResponseEntity<?> getAll(@AuthenticationPrincipal User user,
                                    @RequestParam(name = "chatId") Integer chatId,
                                    @RequestParam(name = "page") Integer page){

        Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd.HH.mm.ss").serializeNulls().create();
        List<Message> messages = messageService.getMessages(user.getId(), chatId, page);
        JsonObject jsonResponseBody = new JsonObject();
        if(messages != null){
            page++;
            JsonElement jsonMessages = gson.toJsonTree(messages);
            if(messages.size() < pageSize){
                page = -1;
            }
            JsonElement jsonPage = gson.toJsonTree(page);
            jsonResponseBody.add("messages", jsonMessages);
            jsonResponseBody.add("page", jsonPage);
            return ResponseEntity.ok().body(gson.toJson(jsonResponseBody));
        }else{
            JsonElement jsonPage = gson.toJsonTree(-1);
            jsonResponseBody.add("page", jsonPage);
            return ResponseEntity.ok().body(gson.toJson(jsonResponseBody));
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/api/messages")
    public ResponseEntity<?> addMessage(@RequestParam(name="text") String text,
                                        @RequestParam(name="senderId") Integer senderId,
                                        @RequestParam(required = false, name="receiverId") Integer receiverId,
                                        @RequestParam(required = false, name="groupId") Integer groupId ){

            Integer messageId = chatService.addMessage(text, senderId, receiverId, groupId);
            if(messageId != null){
                return ResponseEntity.ok().body("Message added");
            }else{
                return ResponseEntity.badRequest().body("Bad request");
            }
    }

}
