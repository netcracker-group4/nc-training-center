package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.IRoleDao;
import ua.com.nc.domain.Role;
import ua.com.nc.service.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private IRoleDao roleDao;

    @Override
    public List<Role> findAllByUserId(Integer id) {
        return roleDao.findAllByUserId(id);
    }
}
