package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Lesson;

import java.util.List;

public interface ILessonDao extends GenericDao<Lesson, Integer>{
    List<Lesson> getByGroupIdAndUserId(Integer groupId, Integer userId);
}
