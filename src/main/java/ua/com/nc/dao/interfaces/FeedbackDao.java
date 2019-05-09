package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Feedback;

import java.util.List;

public interface FeedbackDao extends GenericDao<Feedback, Integer> {
    List<Feedback> getAllByUserId(Integer userId);
}
