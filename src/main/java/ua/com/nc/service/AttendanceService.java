package ua.com.nc.service;

import ua.com.nc.domain.Attendance;
import ua.com.nc.dto.AttendanceDto;

import java.util.List;

public interface AttendanceService {
    AttendanceDto getAttendance();

    List<Attendance> getAttendanceByStudentIdAndCourseId(Integer studentId, Integer courseId);
}
