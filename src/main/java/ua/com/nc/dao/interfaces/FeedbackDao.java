package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Feedback;

import java.util.List;

public interface FeedbackDao extends GenericDao<Feedback> {
    List<Feedback> getAllByUserId(Integer userId);
}
