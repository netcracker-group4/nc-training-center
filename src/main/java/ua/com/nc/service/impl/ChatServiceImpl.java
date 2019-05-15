package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ChatDao;
import ua.com.nc.dao.interfaces.MessageDao;
import ua.com.nc.domain.Chat;
import ua.com.nc.domain.Message;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoUserProfiles;
import ua.com.nc.service.ChatService;
import ua.com.nc.service.UserService;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserService userService;

    @Override
    public Integer addMessage(Message message, Integer receiverId) {
        Integer messageId;
        Chat chat = null;
        if(receiverId != null){
            chat = chatDao.getChatBySenderIdAndReceiverId(message.getSenderId(), receiverId);
        }
        if(message.getChatId() != null){
            //message.setChatId(chat.getId());
            messageId = messageDao.insertMessage(message);
        }
        else{
            if(receiverId != null) {
                DtoUserProfiles receiver = userService.getById(receiverId);
                DtoUserProfiles sender = userService.getById(message.getSenderId());
                String chatName = sender.getLastName() + " " + receiver.getLastName();
                Integer chatId = chatDao.addChatReturningId(new Chat(chatName, message.getDateTime(), null));
                chatDao.addUserToChat(chatId, receiver.getId());
                chatDao.addUserToChat(chatId, sender.getId());
                message.setChatId(chatId);
                messageId = messageDao.insertMessage(message);
            }else{
                throw new PersistException("Receiver id should be not null");
            }
        }
        return messageId;
    }

    @Override
    public List<Chat> getChatsByUserId(Integer userId) {
        return chatDao.getChatsByUserId(userId);
    }

    @Override
    public Chat getByUserIdAndChatId(Integer userId, Integer chatId) {
        return chatDao.getByUserIdAndChatId(userId, chatId);
    }


}
