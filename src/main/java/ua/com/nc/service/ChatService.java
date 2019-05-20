package ua.com.nc.service;

import ua.com.nc.domain.Chat;
import ua.com.nc.domain.Message;

import java.util.List;

public interface ChatService {

    Integer addMessageToGroupChat(Message message, Integer groupId);

    Integer addMessageToChat(Message message, Integer receiverId);

    Integer addMessageToExistingChat(Message message);

    List<Chat> getChatsByUserId(Integer userId);

    Chat getByUserIdAndChatId(Integer userId, Integer chatId);
}
