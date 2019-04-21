package ua.com.nc.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public interface DashBoardService {
    String getLevelAndQuantity();

    String getLevelAndTrainers();

    String getTrainingAndQuantity();

}
