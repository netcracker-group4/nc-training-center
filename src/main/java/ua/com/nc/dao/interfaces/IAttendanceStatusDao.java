package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.AttendanceStatus;

import java.util.List;

public interface IAttendanceStatusDao {
    List<AttendanceStatus> getAll();
}
