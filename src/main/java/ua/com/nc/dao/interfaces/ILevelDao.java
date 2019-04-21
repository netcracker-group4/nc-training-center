package ua.com.nc.dao.interfaces;


import ua.com.nc.domain.Level;

import java.util.List;

public interface ILevelDao extends GenericDao<Level, Integer> {

    List<Level> getAllByTrainer(int trainerId);


}
