package ua.com.nc.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AbsenceReason extends Entity {
    private String title;

    public AbsenceReason() {
    }

    public AbsenceReason(String title) {
        this.title = title;
    }

    public AbsenceReason(Integer id, String title) {
        super(id);
        this.title = title;
    }

}
