package ua.com.nc.service;

import ua.com.nc.domain.ProblemStatus;

import java.util.List;

public interface InfodeskRequestService {

    void createRequest (int id, String description, String message);

    List<ProblemStatus> getStatuses();

//    void updateRequest (int id, ProblemStatus status);
}
