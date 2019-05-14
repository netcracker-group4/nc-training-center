package ua.com.nc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.sql.Date;

@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
public class Message extends Entity {
    private Integer chatId;
    private Integer senderId;
    private Date dateTime;
    private String text;

    public Message(Integer id, Integer chatId, Integer senderId, Date dateTime, String text) {
        super(id);
        this.chatId = chatId;
        this.senderId = senderId;
        this.dateTime = dateTime;
        this.text = text;
    }
}
