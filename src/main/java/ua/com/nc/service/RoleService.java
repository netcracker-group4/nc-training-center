package ua.com.nc.service;

import ua.com.nc.domain.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAllByUserId(Integer id);
}
