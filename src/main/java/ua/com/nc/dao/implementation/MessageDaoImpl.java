package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.MessageDao;
import ua.com.nc.domain.Message;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
@PropertySource("classpath:sql_queries.properties")
public class MessageDaoImpl extends AbstractDaoImpl<Message> implements MessageDao {

    @Value("${message.insert}")
    private String messageInsert;

    @Value("${message.select-by-chat-id}")
    private String selectByChatId;

    @Autowired
    public MessageDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
    }

    @Override
    protected List<Message> parseResultSet(ResultSet rs) throws SQLException {
        List<Message> messages = new ArrayList<>();
        while (rs.next()){
            Integer id = rs.getInt("id");
            Integer chat_id = rs.getInt("chat_id");
            Integer senderId = rs.getInt("user_id");
            Date dateTime = rs.getDate("time_date");
            String text = rs.getString("text");
            Message message = new Message(id, chat_id, senderId, dateTime, text);
            messages.add(message);
        }
        return messages;
    }


    @Override
    public void insert(Message message) throws PersistException {
        String sql = messageInsert;
        log.info(sql + " insert message " + message);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, message.getChatId());
            statement.setInt(2, message.getSenderId());
            statement.setDate(3, message.getDateTime());
            statement.setString(4, message.getText());
            statement.executeUpdate();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public List<Message> getMessagesByChatId(Integer chatId) {
        List<Message> messages = null;
        String sql = selectByChatId;
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, chatId);
            ResultSet resultSet = statement.executeQuery();
            messages = parseResultSet(resultSet);
        } catch (SQLException e) {
            log.trace(e);
            e.printStackTrace();
        }
        return messages;
    }
}
