package ua.com.nc.service;

import ua.com.nc.dto.DtoLesson;

public interface LessonsService {
    String getAllForGroup(int groupId);

    String updateLesson(DtoLesson toUpdate);

    String addLesson(DtoLesson toAdd);

    String deleteLesson(int toDelete);

    String cancelLesson(int lessonId);

    String getAllForEmployee(int userId);

    String getAllForETrainer(Integer userId);
}
