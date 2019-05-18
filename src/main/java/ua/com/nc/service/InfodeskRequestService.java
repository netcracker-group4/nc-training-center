package ua.com.nc.service;

import ua.com.nc.domain.Problem;

import java.util.List;

public interface InfodeskRequestService {

    int createRequest (int id, String description, String message);

    List<Problem> getAllRequestsOfType (String requestType);

    void updateRequestType (int requestId, String requestType);
}
