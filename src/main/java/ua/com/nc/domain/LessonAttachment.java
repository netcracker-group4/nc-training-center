package ua.com.nc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@ToString
public class LessonAttachment extends Entity<Integer> {
    private Integer attachmentId;
    private Integer lessonId;

    public LessonAttachment(Integer id, Integer attachmentId, Integer lessonId){
        super(id);
        this.attachmentId = attachmentId;
        this.lessonId = lessonId;
    }

}
