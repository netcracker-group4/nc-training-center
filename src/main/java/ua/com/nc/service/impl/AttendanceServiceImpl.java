package ua.com.nc.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.AttendanceDao;
import ua.com.nc.dao.interfaces.GroupDao;
import ua.com.nc.dao.interfaces.LessonDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.Attendance;
import ua.com.nc.domain.Group;
import ua.com.nc.domain.Lesson;
import ua.com.nc.domain.User;
import ua.com.nc.dto.AttendanceDto;
import ua.com.nc.security.CustomSecurityService;
import ua.com.nc.service.AttendanceService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

//import ua.com.nc.dao.interfaces.AttendanceDao;

@Log4j2
@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private UserDao userDao;


    @Override
    public List<Attendance> getAttendance(Integer userId, Integer courseId, Integer groupId, Integer lessonId) {
        if (userId != null && courseId != null) {
            return getAttendanceByStudentIdAndCourseId(userId, courseId);
        }
        if (lessonId != null) {
            return  getAttendanceByLessonId(lessonId);
        }
        if (userId != null && groupId != null) {
           return getAttendanceByStudentIdAndGroupId(userId, groupId);
        }
        else return null;
    }

    @Override
    public List<Attendance> getAttendanceByStudentIdAndCourseId(Integer studentId, Integer courseId) {
        return attendanceDao.getAttendanceByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public List<Attendance> getAttendanceByStudentIdAndGroupId(Integer studentId, Integer groupId) {
        return attendanceDao.getAttendanceByStudentIdAndGroupId(studentId, groupId);
    }

    @Override
    public List<Attendance> getAttendanceByLessonId(Integer lessonId) {
        return attendanceDao.getAttendanceByLessonId(lessonId);
    }

    @Override
    public void attendanceInsert(Integer lessonId) {
        attendanceDao.attendanceInsert(lessonId, userDao.getStudentsByLessonId(lessonId));

    }

    @Override
    public void attendanceUpdate(Integer attendanceId, Integer statusId, Integer reasonId) {
        attendanceDao.attendanceUpdate(attendanceId, statusId, reasonId);
    }

}