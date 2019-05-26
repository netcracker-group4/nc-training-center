package ua.com.nc.mapper;

import org.springframework.stereotype.Component;
import ua.com.nc.domain.Feedback;
import ua.com.nc.dto.DtoFeedback;

import java.sql.Timestamp;

@Component
public class FeedbackMapper {
    public Feedback toModel(final DtoFeedback dtoFeedback) {
        Feedback feedback = new Feedback();
        feedback.setStudentId(dtoFeedback.getStudentId());
        feedback.setTrainerId(dtoFeedback.getTeacher().getId());
        feedback.setCourseId(dtoFeedback.getCourse().getId());
        feedback.setText(dtoFeedback.getText());
        feedback.setTime(new Timestamp(System.currentTimeMillis()));

        return feedback;
    }
}
