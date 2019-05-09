package ua.com.nc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Level extends Entity {
    private String title;

    public Level(String title) {
        this.title = title;
    }

    public Level(Integer id, String title) {
        super(id);
        this.title = title;
    }

}
