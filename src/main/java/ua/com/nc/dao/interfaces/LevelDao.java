package ua.com.nc.dao.interfaces;


import ua.com.nc.domain.Level;

import java.util.List;

public interface LevelDao extends GenericDao<Level, Integer> {

    List<Level> getAllByTrainer(int trainerId);

    int getIdByName(String name);


}
