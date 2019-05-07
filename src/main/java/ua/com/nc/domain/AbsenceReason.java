package ua.com.nc.domain;


import lombok.Data;

import java.util.Objects;

@Data
public class AbsenceReason extends Entity<Integer> {
    private String title;

    public AbsenceReason(String title){
        this.title = title;
    }

    public AbsenceReason(Integer id, String title){
        super(id);
        this.title = title;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        if (!super.equals(o)) return false;
        AbsenceReason reason = (AbsenceReason) o;
        return title.equals( reason.title);
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title);    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
