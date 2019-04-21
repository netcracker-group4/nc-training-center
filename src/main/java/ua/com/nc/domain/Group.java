package ua.com.nc.domain;

import lombok.Data;

import java.util.Objects;

@Data
public class Group extends Entity<Integer> {
    private int courseId;
    private String title;

    public Group(int id, int courseId, String title) {
        super(id);
        this.courseId = courseId;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Group group = (Group) o;
        return getCourseId() == group.getCourseId() &&
                Objects.equals(getTitle(), group.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCourseId(), getTitle());
    }
}
