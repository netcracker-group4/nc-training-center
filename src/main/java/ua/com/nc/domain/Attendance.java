package ua.com.nc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
public class Attendance extends Entity<Integer> {
    private Integer lessonId;
    private Integer userId;
    private String reason;
    private String status;

    public Attendance(Integer id, Integer lessonId, Integer userId, String reason, String status){
        super(id);
        this.lessonId = lessonId;
        this.userId = userId;
        this.reason = reason;
        this.status = status;
    }
}
