package ua.com.nc.service;

import ua.com.nc.domain.Chat;
import ua.com.nc.domain.Message;

import java.util.List;

public interface ChatService {
    Integer addMessage(Message message, Integer receiverId);

    List<Chat> getChatsByUserId(Integer userId);
}
