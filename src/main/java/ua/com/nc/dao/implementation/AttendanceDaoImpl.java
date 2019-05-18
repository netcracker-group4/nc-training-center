package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.AttendanceDao;
import ua.com.nc.domain.Attendance;
import ua.com.nc.dto.UserAttendanceDto;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
@PropertySource("classpath:sql_queries.properties")
public class AttendanceDaoImpl extends AbstractDaoImpl<Attendance> implements AttendanceDao {

    @Value("${attendance.select-by-student-id-and-group-id}")
    private String selectAttendanceByStudentIdAndGroupId;

    @Value("${attendance.select-by-student-id-and-course-id}")
    private String selectAttendanceByStudentIdAndCourseId;

    @Value("${attendance.select-by-lesson-id}")
    private String selectAttendanceByLessonId;

    @Value("${attendance.update}")
    private String attendanceUpdate;

    @Autowired
    public AttendanceDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Attendance entity) {
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Attendance entity) {
    }


    @Override
    protected List<Attendance> parseResultSet(ResultSet rs) throws SQLException {
        List<Attendance> attendances = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer lessonId = rs.getInt("lesson_id");
            Integer userId = rs.getInt("student_id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            Integer courseId = rs.getInt("course_id");
            String courseName = rs.getString("course_name");
            Date timeDate = rs.getDate("time_date");
            String topic = rs.getString("topic");
            String status = rs.getString("status");
            String reason = rs.getString("reason");
            attendances.add(new UserAttendanceDto(id, lessonId, userId, reason, status, firstName,
                    lastName, courseId, courseName, timeDate, topic));

        }
        return attendances;
    }

    @Override
    public List<Attendance> getAttendanceByStudentIdAndCourseId(Integer studentId, Integer courseId) {
        return getAttendances(studentId, courseId, selectAttendanceByStudentIdAndCourseId);
    }

    private List<Attendance> getAttendances(Integer studentId, Integer courseId, String selectAttendanceByStudentIdAndCourseId) {
        List<Attendance> list;
        log.info(selectAttendanceByStudentIdAndCourseId);
        try (PreparedStatement statement = connection.prepareStatement(selectAttendanceByStudentIdAndCourseId)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
        return list;
    }

    private List<Attendance> getAttendances(Integer lessonId, String selectAttendanceByLessonId) {
        log.info(selectAttendanceByLessonId);
        try (PreparedStatement statement = connection.prepareStatement(selectAttendanceByLessonId)) {
            statement.setInt(1, lessonId);
            return parseResultSet(statement.executeQuery());
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

    @Override
    public List<Attendance> getAttendanceByStudentIdAndGroupId(Integer studentId, Integer groupId) {
        return getAttendances(studentId, groupId, selectAttendanceByStudentIdAndGroupId);
    }

    @Override
    public List<Attendance> getAttendanceByLessonId(Integer lessonId) {
        return getAttendances(lessonId, selectAttendanceByLessonId);
    }


    @Override
    public void attendanceUpdate(Integer attendanceId, Integer statusId, Integer reasonId) {
        String sql = attendanceUpdate;
        log.info(sql + " attendance update");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (statusId == null) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setInt(1, statusId);
            }
            if (reasonId == null) {
                statement.setNull(2, Types.INTEGER);
            } else {
                statement.setInt(2, reasonId);
            }
            statement.setInt(3, attendanceId);

            statement.executeUpdate();
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
    }

}
