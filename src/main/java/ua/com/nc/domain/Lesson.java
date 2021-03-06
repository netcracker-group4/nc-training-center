package ua.com.nc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ToString
public class Lesson extends Entity {
    private Integer groupId;
    private String topic;
    private Integer trainerId;
    private Date timeDate;
    private Timestamp time;
    private String duration;
    private boolean isCanceled;
    private String absenceReason;
    private String absenceStatus;
    private boolean isPerformed;
    private boolean isArchived;

    public Lesson(Integer groupId, String topic, Integer trainerId, Date timeDate, Timestamp time,
                  String duration, boolean isCanceled, boolean isPerformed, boolean isArchived) {
        this.groupId = groupId;
        this.topic = topic;
        this.trainerId = trainerId;
        this.timeDate = timeDate;
        this.duration = duration;
        this.time = time;
        this.isCanceled = isCanceled;
        this.isPerformed = isPerformed;
        this.isArchived = isArchived;
    }

    public Lesson(Integer id, Integer groupId, String topic, Integer trainerId, Date timeDate, Timestamp time,
                  String duration, boolean isCanceled, boolean isPerformed, boolean isArchived) {
        super(id);
        this.groupId = groupId;
        this.topic = topic;
        this.trainerId = trainerId;
        this.timeDate = timeDate;
        this.time = time;
        this.duration = duration;
        this.isCanceled = isCanceled;
        this.isPerformed = isPerformed;
        this.isArchived = isArchived;
    }

    public Lesson(Integer id, Integer groupId, String topic, Integer trainerId, Date timeDate,
                  String absenceReason, String absenceStatus) {
        super(id);
        this.groupId = groupId;
        this.topic = topic;
        this.trainerId = trainerId;
        this.timeDate = timeDate;
        this.absenceReason = absenceReason;
        this.absenceStatus = absenceStatus;
    }

    public String getIntervalString() {
        String[] intervalElements = duration.split(":");
        return intervalElements[0] + "h " + intervalElements[1] + "m";
    }

}
