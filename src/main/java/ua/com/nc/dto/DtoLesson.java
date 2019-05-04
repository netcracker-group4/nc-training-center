package ua.com.nc.dto;

import lombok.Data;
import ua.com.nc.domain.Attachment;
import ua.com.nc.domain.Lesson;
import ua.com.nc.domain.LessonAttachment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class DtoLesson {
    private Integer id;
    private String topic;
    private Integer groupId;
    private Integer trainerId;
    private Date timeDate;
    private List<Attachment> attachments;

    public DtoLesson(Lesson lesson, List<Attachment> attachments) {
        this.id = lesson.getId();
        this.topic = lesson.getTopic();
        this.groupId = lesson.getGroupId();
        this.trainerId = lesson.getTrainerId();
        this.timeDate = lesson.getTimeDate();
        this.attachments = attachments;
    }

    public Lesson getDomainLesson() {
        java.sql.Date sqlDate = java.sql.Date.valueOf( timeDate.toString() );
        return new Lesson(id, groupId, topic, trainerId, sqlDate);
    }


    public List<LessonAttachment> getAttachments(){
        List<LessonAttachment> domainAttachments = new ArrayList<>();
        for (Attachment attachment : attachments) {
            domainAttachments.add(new LessonAttachment(attachment.getId(),id));
        }
        return domainAttachments;
    }

}
