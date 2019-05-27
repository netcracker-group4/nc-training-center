package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Level;
import ua.com.nc.domain.Problem;

import java.util.List;

public interface ProblemDao extends GenericDao<Problem> {

    List<Problem> getAll ();

    List <Problem> getRequestsByUserId (Integer userId);

}
