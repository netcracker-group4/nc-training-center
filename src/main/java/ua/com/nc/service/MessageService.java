package ua.com.nc.service;

import ua.com.nc.domain.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesByChatId(Integer chatId);

    List<Message> getPageOfMessagesByChatId(Integer chatId, Integer page);
}
