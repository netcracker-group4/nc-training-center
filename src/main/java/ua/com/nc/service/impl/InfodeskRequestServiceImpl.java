package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.ProblemDao;
import ua.com.nc.dao.interfaces.ProblemStatusDao;
import ua.com.nc.domain.Problem;
import ua.com.nc.domain.ProblemStatus;
import ua.com.nc.service.InfodeskRequestService;

import java.util.List;

@Log4j2
@Service
public class InfodeskRequestServiceImpl implements InfodeskRequestService {

    @Autowired
    private ProblemDao problemDao;
    @Autowired
    private ProblemStatusDao problemStatusDao;

    @Override
    public void createRequest (int userId, String description, String message, String requestType) {
        Problem problem = new Problem (userId, description, message, 2);
        List<ProblemStatus> ps = problemStatusDao.getAll();
        for (ProblemStatus status : ps) {
            if (status.getTitle().equals(requestType)) {
                problem.setStatus (status.getId());
                problemDao.insert (problem);
            }
        }
    }

    @Override
    public List<Problem> getAllRequests () {
        return problemDao.getAll ();
    }

    @Override
    public void updateRequestType (int requestId, String requestType) {
        Problem problem = problemDao.getEntityById(requestId);
        List<ProblemStatus> ps = problemStatusDao.getAll();
        for (ProblemStatus status : ps) {
            if (status.getTitle().equals(requestType)) {
                problem.setStatus (status.getId());
                problemDao.update (problem);
            }
        }

    }

    @Override
    public List<ProblemStatus> getStatuses() {
        return problemStatusDao.getAll();
    }

    @Override
    public List <Problem> getRequestsByUserId (int userId) {
        return problemDao.getRequestsByUserId (userId);
    }

}
