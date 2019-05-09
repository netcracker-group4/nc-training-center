package ua.com.nc.service;


import ua.com.nc.dto.CourseAndGroups;
import ua.com.nc.dto.DTOLevel;
import ua.com.nc.dto.DTOTrainer;

import java.util.List;

public interface DashBoardService {
    List<DTOLevel> getLevelAndQuantity();

    List<DTOTrainer> getLevelAndTrainers();

    List<CourseAndGroups> getTrainingAndQuantity();

}
