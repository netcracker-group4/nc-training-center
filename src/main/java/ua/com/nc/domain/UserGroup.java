package ua.com.nc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserGroup extends Entity {
    private Integer userId;
    private Integer groupId;
    private Integer courseId;
    private boolean attending;

    public UserGroup() {
    }

    public UserGroup(Integer userId, Integer groupId, boolean attending) {
        this.userId = userId;
        this.groupId = groupId;
        this.attending = attending;
    }

    public UserGroup(Integer id, Integer userId, Integer groupId, Integer courseId, boolean attending) {
        super(id);
        this.userId = userId;
        this.groupId = groupId;
        this.courseId = courseId;
        this.attending = attending;
    }

    public UserGroup(Integer userId, Integer groupId, Integer courseId, boolean attending) {
        this.userId = userId;
        this.groupId = groupId;
        this.courseId = courseId;
        this.attending = attending;
    }

}
