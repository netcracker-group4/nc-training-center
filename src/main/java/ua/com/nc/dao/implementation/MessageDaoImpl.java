package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.MessageDao;
import ua.com.nc.domain.Message;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
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
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer chat_id = rs.getInt("chat_id");
            Integer senderId = rs.getInt("user_id");
            Timestamp dateTime = rs.getTimestamp("time_date");
            String text = rs.getString("text");
            Message message = new Message(id, chat_id, senderId, dateTime, text);
            messages.add(message);
        }
        return messages;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Message entity) throws SQLException {
        statement.setInt(1, entity.getChatId());
        statement.setInt(2, entity.getSenderId());
        statement.setTimestamp(3, entity.getDateTime());
        statement.setString(4, entity.getText());
    }

    @Override
    protected String getInsertQuery() {
        return messageInsert;
    }


    @Override
    public List<Message> getMessagesByChatId(Integer chatId) {
        String sql = selectByChatId;
        return getFromSqlById(sql, chatId);
    }


    @Override
    public Integer insertMessageReturningId(Message message) {
        Integer messageId = null;
        String sql = messageInsert;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, message.getChatId());
            statement.setInt(2, message.getSenderId());
            statement.setTimestamp(3, message.getDateTime());
            statement.setString(4, message.getText());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            messageId = resultSet.getInt("id");
        } catch (SQLException e) {
            log.error(e);
        }
        return messageId;
    }
}
