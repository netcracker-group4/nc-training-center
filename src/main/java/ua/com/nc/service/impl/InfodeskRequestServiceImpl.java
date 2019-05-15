package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.ProblemDao;
import ua.com.nc.service.InfodeskRequestService;

@Log4j
@Service
public class InfodeskRequestServiceImpl implements InfodeskRequestService {

    @Autowired
    private ProblemDao problemDao;

    @Override
    public void createRequest (int id, String description, String message) {
        problemDao.createRequest(id, description, message);
    }


}
