package ua.com.nc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class AttendanceStatus extends Entity {
    private String title;

    public AttendanceStatus(String title) {
        this.title = title;
    }

    public AttendanceStatus(Integer id, String title) {
        super(id);
        this.title = title;
    }

}
