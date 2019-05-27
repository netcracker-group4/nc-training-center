package ua.com.nc.service;

import ua.com.nc.dto.DtoLesson;

import java.util.List;

public interface LessonsService {
    List<DtoLesson> getAllForGroup(Integer groupId);

    Integer updateLesson(DtoLesson toUpdate);

    Integer addLesson(DtoLesson toAdd);

    void deleteLesson(int toDelete);

    boolean invertIsCanceledForLesson(Integer lessonId);

    List<DtoLesson> getAllForEmployee(Integer userId);

    List<DtoLesson> getAllForTrainer(Integer userId);

    DtoLesson getLessonById(Integer lessonId);

    boolean invertIsPerformedForLesson(Integer lessonId);
}
