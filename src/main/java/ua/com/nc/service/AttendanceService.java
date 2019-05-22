package ua.com.nc.service;

import ua.com.nc.domain.Attendance;
import ua.com.nc.dto.AttendanceDto;

import java.util.List;

public interface AttendanceService {
    AttendanceDto getAttendance();

    List<Attendance> getAttendanceByStudentIdAndCourseId(Integer studentId, Integer courseId);

    List<Attendance> getAttendanceByStudentIdAndGroupId(Integer studentId, Integer groupId);

    List<Attendance> getAttendanceByLessonId(Integer lessonId);

    void attendanceInsert(Integer lessonId);

    void attendanceUpdate(Integer attendanceId, Integer statusId, Integer reasonId);
}
