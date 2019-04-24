package ua.com.nc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@ToString
public class Lesson extends Entity<Integer> {
    private Integer groupId;
    private String topic;
    private Integer trainerId;
    private OffsetDateTime timeDate;
    private String absenceReason;
    private String absenceStatus;

    public Lesson(Integer id, Integer groupId, String topic, Integer trainerId, OffsetDateTime timeDate) {
        super(id);
        this.groupId = groupId;
        this.topic = topic;
        this.trainerId = trainerId;
        this.timeDate = timeDate;
    }

    public Lesson(Integer id, Integer groupId, String topic, Integer trainerId, OffsetDateTime timeDate, String absenceReason, String absenceStatus) {
        super(id);
        this.groupId = groupId;
        this.topic = topic;
        this.trainerId = trainerId;
        this.timeDate = timeDate;
        this.absenceReason = absenceReason;
        this.absenceStatus = absenceStatus;
    }
}
