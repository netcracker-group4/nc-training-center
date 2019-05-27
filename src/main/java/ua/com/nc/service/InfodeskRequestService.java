package ua.com.nc.service;

import ua.com.nc.domain.ProblemStatus;

import java.util.List;

import ua.com.nc.domain.Problem;

import java.util.List;

public interface InfodeskRequestService {

    void createRequest (Integer userId, String description, String message, String requestType);

    List<ProblemStatus> getStatuses();

    List<Problem> getAllRequests ();

    void updateRequestType (Integer requestId, String requestType);

    List <Problem> getRequestsByUserId (Integer userId);
}
