package ua.com.nc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.dao.implementation.MessageDaoImpl;
import ua.com.nc.domain.Chat;
import ua.com.nc.domain.Message;
import ua.com.nc.domain.User;
import ua.com.nc.security.CustomSecurityService;
import ua.com.nc.service.ChatService;
import ua.com.nc.service.MessageService;

import java.sql.Timestamp;
import java.util.List;

@Log4j2
@Controller
public class MessageController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CustomSecurityService customSecurityService;

    @MessageMapping("/message")
    @SendTo("/topic/msg")
    public String addMessageAndReturnToChatPage(String jsonMessage){
        Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd.HH.mm.ss").serializeNulls().create();
        Message message = gson.fromJson(jsonMessage, Message.class);
        Chat chat = chatService.getByUserIdAndChatId(message.getSenderId(), message.getChatId());
        if(chat != null){
            message.setDateTime(new Timestamp(System.currentTimeMillis()));
            Integer messageId = chatService.addMessageToExistingChat(message);
            Message messageFromDb = messageService.getById(messageId);
            return gson.toJson(messageFromDb);
        }else{
            throw new AccessDeniedException("Access denied");
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/api/messages")
    public ResponseEntity<?> getAll(@AuthenticationPrincipal User user,
                                    @RequestParam(name = "chatId") Integer chatId,
                                    @RequestParam(name = "page") Integer page){

        Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd.HH.mm.ss").serializeNulls().create();
        Chat chat = chatService.getByUserIdAndChatId(user.getId(), chatId);

        if(chat != null){
            List<Message> messages = messageService.getPageOfMessagesByChatId(chatId, page);
            JsonObject jsonResponseBody = new JsonObject();
            if(messages != null){
                page++;
                JsonElement jsonMessages = gson.toJsonTree(messages);
                if(messages.size() < MessageDaoImpl.PAGE_SIZE){
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
        }else{
            return ResponseEntity.status(403).body("Access denied");
        }

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/api/messages")
    public ResponseEntity<?> addMessage(@RequestParam(name="text") String text,
                                        @RequestParam(name="senderId") Integer senderId,
                                        @RequestParam(required = false, name="receiverId") Integer receiverId,
                                        @RequestParam(required = false, name="groupId") Integer groupId ){


            Message message = new Message(null, senderId, new Timestamp(System.currentTimeMillis()), text);

            if(receiverId != null & groupId == null){
                chatService.addMessageToChat(message, receiverId);
                return ResponseEntity.ok().body("Message added");
            }
            if(groupId != null && receiverId == null){
                if(customSecurityService.canWriteToGroupChat(senderId, groupId)){
                    chatService.addMessageToGroupChat(message, groupId);
                    return ResponseEntity.ok().body("Message added");
                }else{
                    return ResponseEntity.status(403).body("Access denied");
                }
            }
            if(receiverId == null & senderId == null){
                return ResponseEntity.badRequest().body("Incorrect data");
            }else{
                return ResponseEntity.badRequest().body("Bad request");
            }

    }

}
