package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Role;

import java.util.List;

public interface IRoleDao {
    List<Role> findAllByUserId(Integer id);
}
