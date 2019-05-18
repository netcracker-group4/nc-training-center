package ua.com.nc.domain;

import lombok.Data;

@Data
public class ProblemStatus extends Entity {

    private String title;
    private String description;

    public ProblemStatus(Integer id, String title, String description) {
        super(id);
        this.title = title;
        this.description = description;
    }
}
