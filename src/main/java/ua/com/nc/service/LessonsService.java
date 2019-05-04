package ua.com.nc.service;

import ua.com.nc.dto.DtoLesson;

public interface LessonsService {
    String getAllForGroup(int groupId);
    String updateLesson(DtoLesson toUpdate);
    String addLesson(DtoLesson toAdd);
    String deleteLesson(int toDelete);

}
