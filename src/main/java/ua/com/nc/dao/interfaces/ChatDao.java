package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Chat;
import ua.com.nc.domain.User;

import java.util.List;

public interface ChatDao extends GenericDao<Chat> {
    Chat getChatBySenderIdAndReceiverId(Integer senderId, Integer receiverId);

    Integer addChatReturningId(Chat chat);

    void addUserToChat(Integer chatId, Integer userId);

    List<Chat> getChatsByUserId(Integer userId);

    Chat getByUserIdAndChatId(Integer userId, Integer chatId);
}
