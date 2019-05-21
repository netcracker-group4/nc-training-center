package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Lesson;

import java.util.List;

public interface LessonDao extends GenericDao<Lesson> {
    List<Lesson> getByGroupIdAndUserId(Integer groupId, Integer userId);

    List<Lesson> getByGroupId(Integer groupId);

//    void archiveLesson(Integer lessonId);

    List<Lesson> getByEmployee(int userId);

    List<Lesson> getByTrainer(Integer userId);
}
