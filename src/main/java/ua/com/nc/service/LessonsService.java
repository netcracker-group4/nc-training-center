package ua.com.nc.service;

import ua.com.nc.dto.DtoLesson;

import java.util.List;

public interface LessonsService {
    List<DtoLesson> getAllForGroup(int groupId);

    String updateLesson(DtoLesson toUpdate);

    String addLesson(DtoLesson toAdd);

    String deleteLesson(int toDelete);

    String cancelLesson(int lessonId);

    List<DtoLesson> getAllForEmployee(int userId);

    List<DtoLesson> getAllForETrainer(Integer userId);
}
