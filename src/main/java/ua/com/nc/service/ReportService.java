package ua.com.nc.service;

import ua.com.nc.domain.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ReportService {

    ByteArrayInputStream getAttendanceExcel() throws IOException;

    ByteArrayInputStream getAttendanceExcel(User user) throws IOException;

    ByteArrayInputStream getAttendanceExcel(Integer groupId) throws IOException;

    ByteArrayInputStream getDashboardExcel() throws IOException;


}
