package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ChatDao;
import ua.com.nc.domain.Chat;
import ua.com.nc.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@PropertySource("classpath:sql_queries.properties")
public class ChatDaoImpl extends AbstractDaoImpl<Chat> implements ChatDao {

    @Value("${chat.select-chat-by-sender-id-and-receiver-id}")
    private String selectChatBySenderIdAndReceiverId;

    @Value("${chat.insert}")
    private String insertChat;

    @Value("${chat.insert-user}")
    private String insertUser;

    @Value("${chat.select-by-user-id}")
    private String chatSelectByUserId;

    @Value("${chat.select-by-user-id-and-chat-id}")
    private String chatSelectByUserIdAndChatId;

    @Value("${chat.select-by-group-id}")
    private String chatSelectByGroupId;

    @Autowired
    public ChatDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
    }


    @Override
    protected List<Chat> parseResultSet(ResultSet rs) throws SQLException {
        List<Chat> list = new ArrayList<>();
        while (rs.next()){
            Integer id = rs.getInt("id");
            String name = rs.getString("name");
            Timestamp timeDate = rs.getTimestamp("time_date");
            Integer groupId = rs.getInt("group_id");
            Chat chat = new Chat(id, name, timeDate, groupId);
            list.add(chat);
        }
        return list;
    }

    @Override
    public Chat getChatBySenderIdAndReceiverId(Integer senderId, Integer receiverId) {
        List<Chat> list;
        String sql = selectChatBySenderIdAndReceiverId;
        log.info(sql + " select chat by sender and receiver");
        list = getFromSqlByTwoId(selectChatBySenderIdAndReceiverId, senderId, receiverId);
        if(list.size() != 1){
            return null;
        }else{
            return list.iterator().next();
        }
    }

    @Override
    public Integer addChatReturningId(Chat chat) {
        Integer id;
        String sql = insertChat;
        log.info(sql + " insert chat");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertChat)) {
            statement.setString(1, chat.getName());
            statement.setTimestamp(2, chat.getTimeDate());
            if(chat.getGroupId() != null){
                statement.setInt(3, chat.getGroupId());
            }else{
                statement.setNull(3, Types.INTEGER);
            }
            ResultSet rs = statement.executeQuery();
            rs.next();
            id = rs.getInt(1);
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
        return id;
    }

    @Override
    public void addUserToChat(Integer chatId, Integer userId) {
        String sql = insertUser;
        log.info(sql + " insert user to chat");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertUser)) {
            statement.setInt(1, chatId);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public List<Chat> getChatsByUserId(Integer userId) {
        List<Chat> chats = null;
        String sql = chatSelectByUserId;
        log.info(sql + "get chat by user id");
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(chatSelectByUserId)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            chats = parseResultSet(resultSet);
        } catch (SQLException e) {
            log.trace(e);
        }
        return chats;
    }

    @Override
    public Chat getByUserIdAndChatId(Integer userId, Integer chatId) {
        List<Chat> chats;
        String sql = chatSelectByUserIdAndChatId;
        log.info(sql + "get chat by user id and chat id");
        chats = getFromSqlByTwoId(sql, userId, chatId);
        if(chats != null && chats.size() > 0){
            return chats.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Chat getByGroupId(Integer groupId) {
        List<Chat> chats = null;
        String sql = chatSelectByGroupId;
        log.info(sql + " get chat by group id");
        try(PreparedStatement statement = connection.prepareStatement(chatSelectByGroupId)) {
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            chats = parseResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }if(chats != null && chats.size() > 0){
            return chats.get(0);
        }else{
            return null;
        }
    }

}
