package ua.com.nc.domain;

import lombok.Data;

@Data
public class Entity {
    private Integer id;

    Entity() {
    }

    Entity(Integer id) {
        this.id = id;
    }
}
