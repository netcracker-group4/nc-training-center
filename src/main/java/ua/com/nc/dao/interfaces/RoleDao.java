package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Role;

import java.util.List;

public interface RoleDao {
    List<Role> findAllByUserId(Integer id);
}
