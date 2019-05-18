package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Problem;
import ua.com.nc.domain.ProblemStatus;

import java.util.List;

public interface ProblemDao {

    int createRequest (int studentId, String description, String message);

    List<Problem> getAllRequestsOfType (String requestType);

    void updateRequestType (int requestId, String requestType);
}
