package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Attendance;

import java.util.List;

public interface IAttendanceDao {
    List<Attendance> getAttendanceByStudentIdAndCourseId(Integer studentId, Integer courseId);

    List<Attendance> getAttendanceByStudentIdAndGroupId(Integer studentId, Integer groupId);
}

