package ua.com.nc.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, of = {"id"})
public class Attendance extends Entity<Integer> {
    @SerializedName("attendanceId")
    private Integer id;
    private Integer lessonId;
    private Integer userId;
    private String reason;
    private String status;

    public Attendance(Integer id, Integer lessonId, Integer userId, String reason, String status) {
        this.id = id;
        this.lessonId = lessonId;
        this.userId = userId;
        this.reason = reason;
        this.status = status;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", lessonId=" + lessonId +
                ", userId=" + userId +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'';
    }
}
