package ua.com.nc.service;

import ua.com.nc.domain.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesByChatId(Integer chatId);

    List<Message> getPageOfMessagesByChatId(Integer chatId, Integer page);

    Message getById(Integer id);

    List<Message> getMessages(Integer userId, Integer chatId, Integer page);
}
