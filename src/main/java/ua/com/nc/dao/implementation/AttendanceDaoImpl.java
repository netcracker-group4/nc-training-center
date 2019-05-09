package ua.com.nc.dao.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.AttendanceDao;
import ua.com.nc.domain.Attendance;
import ua.com.nc.dto.UserAttendanceDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:sql_queries.properties")
public class AttendanceDaoImpl extends GenericAbstractDao<Attendance> implements AttendanceDao {

    @Value("${attendance.select-by-student-id-and-group-id}")
    private String selectAttendanceByStudentIdAndGroupId;

    @Value("${attendance.select-by-student-id-and-course-id}")
    private String selectAttendanceByStudentIdAndCourseId;

    @Value("${attendance.update}")
    private String attendanceUpdate;

    public AttendanceDaoImpl(@Value("${spring.datasource.url}") String DATABASE_URL,
                             @Value("${spring.datasource.username}") String DATABASE_USER,
                             @Value("${spring.datasource.password}") String DATABASE_PASSWORD) throws PersistException {
        super(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    @Override
    protected Integer parseId(ResultSet rs) {
        return null;
    }

    @Override
    protected String getSelectByIdQuery() {
        return null;
    }

    @Override
    protected String getSelectQuery() {
        return null;
    }

    @Override
    protected String getInsertQuery() {
        return null;
    }

    @Override
    protected String getDeleteQuery() {
        return null;
    }

    @Override
    protected String getUpdateQuery() {
        return null;
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
        List<Attendance> list;
        String sql = selectAttendanceByStudentIdAndCourseId;
        log.debug(selectAttendanceByStudentIdAndCourseId);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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

    @Override
    public List<Attendance> getAttendanceByStudentIdAndGroupId(Integer studentId, Integer groupId) {
        List<Attendance> list;
        String sql = selectAttendanceByStudentIdAndGroupId;
        log.debug(selectAttendanceByStudentIdAndGroupId);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, groupId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            log.trace(e);
            throw new PersistException(e);
        }
        return list;
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
            e.printStackTrace();
            throw new PersistException(e);
        }

    }
}
