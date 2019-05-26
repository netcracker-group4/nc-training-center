package ua.com.nc.service;

import ua.com.nc.dto.DtoLesson;
import ua.com.nc.dto.DtoUser;

import java.util.List;

public interface LessonsService {
    List<DtoLesson> getAllForGroup(Integer groupId);

    String updateLesson(DtoLesson toUpdate);

    String addLesson(DtoLesson toAdd);

    void deleteLesson(int toDelete);

    String invertIsCanceledForLesson(Integer lessonId);

    List<DtoLesson> getAllForEmployee(Integer userId);

    List<DtoLesson> getAllForTrainer(Integer userId);

    DtoLesson getLessonById(Integer lessonId);

    String invertIsPerformedForLesson(Integer lessonId);
}
