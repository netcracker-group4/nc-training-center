package ua.com.nc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ToString
public class Attachment extends Entity<Integer> {
    private String url;
    private String description;

    public Attachment(Integer id, String url, String description) {
        super(id);
        this.url = url;
        this.description = description;
    }
}
