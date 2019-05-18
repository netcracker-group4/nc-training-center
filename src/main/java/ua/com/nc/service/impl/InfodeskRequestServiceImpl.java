package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.ProblemDao;
import ua.com.nc.dao.interfaces.ProblemStatusDao;
import ua.com.nc.domain.ProblemStatus;
import ua.com.nc.service.InfodeskRequestService;

import java.util.List;

@Log4j
@Service
public class InfodeskRequestServiceImpl implements InfodeskRequestService {

    @Autowired
    private ProblemDao problemDao;
    @Autowired
    private ProblemStatusDao problemStatusDao;

    @Override
    public void createRequest (int id, String description, String message) {
        problemDao.createRequest(id, description, message);
    }

    @Override
    public List<ProblemStatus> getStatuses() {
        return problemStatusDao.getAll();
    }


}
