package ua.com.nc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@ToString
public class LessonAttachment extends Entity {
    private Integer attachmentId;
    private Integer lessonId;

    public LessonAttachment(){

    }

    public LessonAttachment(Integer id, Integer attachmentId, Integer lessonId) {
        super(id);
        this.attachmentId = attachmentId;
        this.lessonId = lessonId;
    }

}
