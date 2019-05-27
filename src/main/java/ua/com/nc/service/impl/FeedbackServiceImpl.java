package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.dao.interfaces.FeedbackDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.Course;
import ua.com.nc.domain.Feedback;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoCourse;
import ua.com.nc.dto.DtoFeedback;
import ua.com.nc.dto.DtoMailSender;
import ua.com.nc.dto.DtoTeacherAndManager;
import ua.com.nc.mapper.FeedbackMapper;
import ua.com.nc.service.FeedbackService;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FeedbackDao feedbackDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public void add(DtoFeedback dtoFeedback) {
        Feedback feedback = feedbackMapper.toModel(dtoFeedback);
        feedbackDao.insert(feedback);
    }

    @Override
    public void delete(Integer id) {
        feedbackDao.delete(id);
    }

    @Override
    public List<DtoFeedback> getAllByUserId(Integer id) {
        List<Feedback> feedbacks = feedbackDao.getAllByUserId(id);
        return getDtoFeedback(feedbacks, id);
    }

    @Override
    public List<DtoFeedback> getAllByTrainerIdAndUserId(Integer userId, Integer trainerId) {
        List<Feedback> feedbacks = feedbackDao.getAllByTrainerIdAndUserId(userId, trainerId);
        return getDtoFeedback(feedbacks, userId);
    }

    private List<DtoFeedback> getDtoFeedback(List<Feedback> feedbacks, Integer id) {
        List<DtoFeedback> dtoFeedbacks = new ArrayList<>();
        if (feedbacks != null && feedbacks.size() != 0) {
            for (Feedback feedback : feedbacks) {
                User teacher = userDao.getTrainerByFeedback(feedback.getId());
                Course course = courseDao.getCourseByFeedback(feedback.getId());

                DtoTeacherAndManager dtoTeacher = new DtoTeacherAndManager(
                        teacher.getId(),
                        teacher.getFirstName(),
                        teacher.getLastName(),
                        teacher.isActive(),
                        teacher.getImageUrl()
                );

                DtoCourse dtoCourse = new DtoCourse();
                if (course != null) {
                    dtoCourse.setId(course.getId());
                    dtoCourse.setName(course.getName());
                }

                dtoFeedbacks.add(new DtoFeedback(
                        feedback.getId(),
                        id,
                        dtoTeacher,
                        dtoCourse,
                        feedback.getText(),
                        feedback.getTime()
                ));
            }
        }
        return dtoFeedbacks;
    }
}
