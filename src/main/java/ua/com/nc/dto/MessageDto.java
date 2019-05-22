package ua.com.nc.dto;

import lombok.Data;
import ua.com.nc.domain.Message;

import java.sql.Timestamp;

@Data
public class MessageDto extends Message {
    private String senderFirstName;
    private String senderLastName;

    public MessageDto(Integer id, Integer chatId, Integer senderId, Timestamp dateTime, String text,
                      String senderFirstName, String senderLastName) {
        super(id, chatId, senderId, dateTime, text);
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
    }
}
