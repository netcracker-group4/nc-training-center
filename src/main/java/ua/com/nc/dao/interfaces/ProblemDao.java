package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.ProblemStatus;

public interface ProblemDao {

    void createRequest (int studentId, String description, String message);

   //  TODO
//    void updateRequest (int id, ProblemStatus status);

}
