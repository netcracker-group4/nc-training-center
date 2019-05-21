package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.MessageDao;
import ua.com.nc.domain.Message;
import ua.com.nc.service.MessageService;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

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
}
