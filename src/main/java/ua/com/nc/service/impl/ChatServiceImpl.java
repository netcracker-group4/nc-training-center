package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.ChatDao;
import ua.com.nc.dao.interfaces.GroupDao;
import ua.com.nc.dao.interfaces.MessageDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.Chat;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.Message;
import ua.com.nc.domain.User;
import ua.com.nc.service.ChatService;
import ua.com.nc.service.GroupsService;

import java.sql.Timestamp;
import java.util.List;

@Log4j2
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

    @Autowired
    private GroupDao groupDao;

    /**
     * Add message to group or simple chat depending on parameters
     *
     * @param text text of message for adding in group chat
     * @param senderId id of sender
     * @param receiverId id of receiver
     * @param groupId id og group where message should add
     * @return id of added message
     */
    @Override
    public Integer addMessage(String text, Integer senderId, Integer receiverId, Integer groupId) {
        Message message = new Message(null, senderId, new Timestamp(System.currentTimeMillis()), text);

        if(receiverId != null & groupId == null){
            return addMessageToChat(message, receiverId);
        }
        if(groupId != null && receiverId == null){
            if(canWriteToGroupChat(senderId, groupId)){
                return addMessageToGroupChat(message, groupId);
            }else{
                log.debug("This user can't add message to this chat " + message + " group id " + groupId);
                return null;
            }
        }else {
            log.debug("Not full data to add message to chat" + message);
            return null;
        }

    }

    /**
     * Add message to group chat
     *
     * @param message message for adding in group chat
     * @param groupId id og group where message should add
     * @return id of added message
     */
    private Integer addMessageToGroupChat(Message message, Integer groupId) {
        Chat chat = chatDao.getByGroupId(groupId);
        if(chat != null){
            message.setChatId(chat.getId());
            return addMessageToExistingChat(message);
        }else {
            String groupName = groupsService.getGroupById(groupId).getTitle();
            String chatName = groupName + " chat";
            List<User> studentsOfGroup = userDao.getByGroupId(groupId);
            Chat newChat = new Chat(chatName, message.getDateTime(), groupId);

            User trainer = userDao.getTrainerByGroupId(groupId);

            Integer newChatId = chatDao.addChatReturningId(newChat);

            for(User student : studentsOfGroup){
                chatDao.addUserToChat(newChatId, student.getId());
            }

            chatDao.addUserToChat(newChatId, trainer.getId());

            message.setChatId(newChatId);

            return messageDao.insertMessageReturningId(message);
        }

    }

    /**
     * Add message to chat with two users
     *
     * @param message message for adding in group chat
     * @param receiverId id of receiver
     * @return id of added message
     */
    private Integer addMessageToChat(Message message, Integer receiverId) {
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

    private boolean canWriteToGroupChat(Integer userId, Integer groupId){
        Group group = groupDao.getByUserIdAndGroupId(userId, groupId);
        User trainer = userDao.getTrainerByGroupId(groupId);
        return group != null || trainer.getId().equals(userId);
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
