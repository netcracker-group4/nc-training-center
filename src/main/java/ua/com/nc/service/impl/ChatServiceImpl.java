package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.ChatDao;
import ua.com.nc.dao.interfaces.MessageDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.Chat;
import ua.com.nc.domain.Message;
import ua.com.nc.domain.User;
import ua.com.nc.service.ChatService;
import ua.com.nc.service.GroupsService;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private UserDao userDao;

    @Override
    public Integer addMessageToGroupChat(Message message, Integer groupId) {
        Chat chat = chatDao.getByGroupId(groupId);
        if(chat != null){
            message.setChatId(chat.getId());
            return addMessageToExistingChat(message);
        }else {
            String groupName = groupsService.getGroupById(groupId).getTitle();
            String chatName = groupName + " chat";
            List<User> studentsOfGroup = userDao.getByGroupId(groupId);
            Chat newChat = new Chat(chatName, message.getDateTime(), groupId);
            User trainer = userDao.getTrainerByCourseId(groupId);

            Integer newChatId = chatDao.addChatReturningId(newChat);

            for(User student : studentsOfGroup){
                chatDao.addUserToChat(newChatId, student.getId());
            }

            chatDao.addUserToChat(newChatId, trainer.getId());

            message.setChatId(newChatId);

            return messageDao.insertMessageReturningId(message);
        }

    }

    @Override
    public Integer addMessageToChat(Message message, Integer receiverId) {
        Chat chat = chatDao.getChatBySenderIdAndReceiverId(message.getSenderId(), receiverId);
        if(chat != null){
            message.setChatId(chat.getId());
            return addMessageToExistingChat(message);
        }else{
            String senderName = userDao.getEntityById(message.getSenderId()).getLastName();
            String receiverName = userDao.getEntityById(receiverId).getLastName();
            String chatName = senderName + " " + receiverName;

            Chat newChat = new Chat(chatName, message.getDateTime(), null);

            Integer newChatId = chatDao.addChatReturningId(newChat);

            chatDao.addUserToChat(newChatId, message.getSenderId());
            chatDao.addUserToChat(newChatId, receiverId);

            message.setChatId(newChatId);

            return messageDao.insertMessageReturningId(message);

        }
    }

    @Override
    public Integer addMessageToExistingChat(Message message) {
        return messageDao.insertMessageReturningId(message);
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
