package ua.com.nc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ToString
public class Attachment extends Entity {
    private String url;
    private String name;
    private Integer trainerId;
    private String description;

    public Attachment(Integer id, String url, String name,Integer trainer_id, String description) {
        super(id);
        this.url = url;
        this.name = name;
        this.trainerId = trainer_id;
        this.description = description;
    }
}
