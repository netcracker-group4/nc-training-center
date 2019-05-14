package ua.com.nc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class Chat extends Entity {
    private String name;
    private Date timeDate;
    private Integer groupId;

    public Chat(Integer id, String name, Date timeDate, Integer groupId) {
        super(id);
        this.name = name;
        this.timeDate = timeDate;
        this.groupId = groupId;
    }
}
