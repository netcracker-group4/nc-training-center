package ua.com.nc.domain;

import java.util.Objects;


public class AttendanceStatus extends Entity<Integer> {
    private String title;

    public AttendanceStatus(String title) {
        this.title = title;
    }

    public AttendanceStatus(Integer id, String title) {
        super(id);
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        if (!super.equals(o)) return false;
        AttendanceStatus status = (AttendanceStatus) o;
        return title.equals(status.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
