package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Message;

import java.util.List;

public interface MessageDao extends GenericDao<Message>{
    List<Message> getMessagesByChatId(Integer chatId);
}
