package ua.com.nc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserGroup extends Entity<Integer> {
    private int userId;
    private int groupId;
    private boolean attending;

    public UserGroup() {
    }

    public UserGroup(int userId, int groupId, boolean attending) {
        this.userId = userId;
        this.groupId = groupId;
        this.attending = attending;
    }

    public UserGroup(Integer id, int userId, int groupId, boolean attending) {
        super(id);
        this.userId = userId;
        this.groupId = groupId;
        this.attending = attending;
    }
}
