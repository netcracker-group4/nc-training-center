package ua.com.nc.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ReportService {

    ByteArrayInputStream getAttendanceExcel() throws IOException;

    ByteArrayInputStream getDashboardExcel() throws IOException;


}
