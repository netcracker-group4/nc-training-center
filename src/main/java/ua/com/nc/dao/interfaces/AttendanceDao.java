package ua.com.nc.dao.interfaces;

import ua.com.nc.domain.Attendance;
import ua.com.nc.domain.User;

import java.util.List;

public interface AttendanceDao extends GenericDao<Attendance> {
    List<Attendance> getAttendanceByStudentIdAndCourseId(Integer studentId, Integer courseId);

    List<Attendance> getAttendanceByStudentIdAndGroupId(Integer studentId, Integer groupId);

    List<Attendance> getAttendanceByLessonId(Integer lessonId);

    List<Attendance> getAttendanceByGroupId(Integer groupId);

    void attendanceInsert(Integer lessonId, List<User> employees);

    void attendanceUpdate(Integer attendanceId, Integer statusId, Integer reasonId);

}

