package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Message;

import java.util.List;

public interface MessageDao extends GenericDao<Message>{

    List<Message> getPageOfMessagesByChatId(Integer chatId, Integer page);

    List<Message> getMessagesByChatId(Integer chatId);

    Integer insertMessageReturningId(Message message);

}
