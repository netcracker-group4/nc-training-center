package ua.com.nc.domain;

import lombok.Data;

@Data
public class Level extends Entity<Integer> {
    private String title;

    public Level(String title) {
        this.title = title;
    }

    public Level(Integer id, String title) {
        super(id);
        this.title = title;
    }

}
