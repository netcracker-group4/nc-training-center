package ua.com.nc.dto;

import lombok.Data;
import ua.com.nc.domain.Attachment;
import ua.com.nc.domain.Lesson;
import ua.com.nc.domain.LessonAttachment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Data
public class DtoLesson {
    private Integer id;
    private String topic;
    private Integer groupId;
    private Integer trainerId;
    private String trainerName;
    private Timestamp timeDate;
    private String duration;
    private boolean isCanceled;
    private List<Attachment> attachments;

    public DtoLesson(Lesson lesson, String trainerName, List<Attachment> attachments) {
        this.id = lesson.getId();
        this.topic = lesson.getTopic();
        this.groupId = lesson.getGroupId();
        this.trainerId = lesson.getTrainerId();
        this.isCanceled = lesson.isCanceled();
        this.trainerName = trainerName;
        this.timeDate = lesson.getTime();
        this.duration = lesson.getDuration();
        this.attachments = attachments;
    }

    public Lesson getDomainLesson() {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.Z");
        String dateString = timeDate.toString().substring(0, 10);
        java.sql.Date sqlDate = java.sql.Date.valueOf(dateString);
        return new Lesson(id, groupId, topic, trainerId, sqlDate, timeDate, duration, isCanceled);
    }


    public List<LessonAttachment> getAttachments() {
        List<LessonAttachment> domainAttachments = new ArrayList<>();
        for (Attachment attachment : attachments) {
            domainAttachments.add(new LessonAttachment(attachment.getId(), id));
        }
        return domainAttachments;
    }

    public List<LessonAttachment> getAttachmentsForNewLesson(int id) {
        List<LessonAttachment> domainAttachments = new ArrayList<>();
        for (Attachment attachment : attachments) {
            domainAttachments.add(new LessonAttachment(attachment.getId(), id));
        }
        return domainAttachments;
    }

}
