package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.implementation.AttendanceStatusDao;
import ua.com.nc.domain.AttendanceStatus;
import ua.com.nc.service.AttendanceStatusService;

import java.util.List;

@Service
public class AttendanceStatusServiceImpl implements AttendanceStatusService {

    @Autowired
    private AttendanceStatusDao attendanceStatusDao;

    @Override
    public List<AttendanceStatus> getAll() {
        return attendanceStatusDao.getAll();
    }
}
