package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.AttendanceDao;
import ua.com.nc.domain.Attendance;
import ua.com.nc.domain.User;
import ua.com.nc.dto.UserAttendanceDto;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Repository
@PropertySource("classpath:sql_queries.properties")
public class AttendanceDaoImpl extends AbstractDaoImpl<Attendance> implements AttendanceDao {

    @Value("${attendance.select-by-student-id-and-group-id}")
    private String selectAttendanceByStudentIdAndGroupId;

    @Value("${attendance.select-by-student-id-and-course-id}")
    private String selectAttendanceByStudentIdAndCourseId;

    @Value("${attendance.select-by-lesson-id}")
    private String selectAttendanceByLessonId;

    @Value("${attendance.select-by-group-id}")
    private String selectByGroupId;

    @Value("${attendance.insert}")
    private String attendanceInsert;

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
        log.debug("Retrieved attendances from database   " + attendances);
        return attendances;
    }

    @Override
    public List<Attendance> getAttendanceByStudentIdAndCourseId(Integer studentId, Integer courseId) {
        String sql = this.selectAttendanceByStudentIdAndCourseId;
        log.debug("Select attendance by student " + studentId + " and course " + courseId + " " + sql);
        return getFromSqlByTwoId(sql, studentId, courseId);
    }

    @Override
    public List<Attendance> getAttendanceByStudentIdAndGroupId(Integer studentId, Integer groupId) {
        String sql = this.selectAttendanceByStudentIdAndGroupId;
        log.debug("Select attendance by student " + studentId + " and group " + groupId + " " + sql);
        return getFromSqlByTwoId(sql, studentId, groupId);
    }

    @Override
    public List<Attendance> getAttendanceByLessonId(Integer lessonId) {
        String sql = this.selectAttendanceByLessonId;
        log.debug("Select attendance for lesson by lesson id  " + lessonId + " " + sql);
        return getFromSqlById(sql, lessonId);
    }

    @Override
    public List<Attendance> getAttendanceByGroupId(Integer groupId){
        String sql = this.selectByGroupId;
        log.debug("Select attendance for group by groupId  " + groupId + " " + sql);
        return getFromSqlById(sql, groupId);
    }

    @Override
    public void attendanceInsert(Integer lessonId, List<User> employees) {
        String sql = attendanceInsert;
        log.debug("attendance insert with parameters lessonId " + lessonId +
                ",  employees " + employees.toString() + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for(User employee: employees){
                statement.setInt(1, lessonId);
                statement.setInt(2, employee.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (Exception e) {
            log.error(e);
            throw new PersistException(e);
        }
    }

    @Override
    public void attendanceUpdate(Integer attendanceId, Integer statusId, Integer reasonId) {
        String sql = attendanceUpdate;
        log.debug("attendance update with parameters attendanceId " + attendanceId +
                ",  statusId " + statusId + ",  reasonId " + reasonId + " " + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
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
            log.error(e);
            throw new PersistException(e);
        }
    }

}
