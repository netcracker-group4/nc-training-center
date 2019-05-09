package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Attendance;

import java.util.List;

public interface AttendanceDao extends GenericDao<Attendance, Integer> {
    List<Attendance> getAttendanceByStudentIdAndCourseId(Integer studentId, Integer courseId);

    List<Attendance> getAttendanceByStudentIdAndGroupId(Integer studentId, Integer groupId);

    void attendanceUpdate(Integer attendanceId, Integer statusId, Integer reasonId);
}

