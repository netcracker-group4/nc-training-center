package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.RoleDao;
import ua.com.nc.domain.Role;
import ua.com.nc.service.RoleService;

import java.util.List;

@Log4j2
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> findAllByUserId(Integer id) {
        return roleDao.findAllByUserId(id);
    }
}
