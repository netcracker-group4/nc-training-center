package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.MessageDao;
import ua.com.nc.domain.Chat;
import ua.com.nc.domain.Message;
import ua.com.nc.service.ChatService;
import ua.com.nc.service.MessageService;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private ChatService chatService;

    @Override
    public List<Message> getMessagesByChatId(Integer chatId) {
        return messageDao.getMessagesByChatId(chatId);
    }

    @Override
    public List<Message> getPageOfMessagesByChatId(Integer chatId, Integer page) {
        if(page != null && page >= 0){
            return messageDao.getPageOfMessagesByChatId(chatId, page);
        }else{
            return getMessagesByChatId(chatId);
        }
    }

    @Override
    public Message getById(Integer id) {
        return messageDao.getEntityById(id);
    }

    @Override
    public List<Message> getMessages(Integer userId, Integer chatId, Integer page) {
        Chat chat = chatService.getByUserIdAndChatId(userId, chatId);
        if(chat != null){
            return getPageOfMessagesByChatId(chatId, page);
        }
        else return null;
    }
}
