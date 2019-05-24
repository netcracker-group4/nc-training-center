package ua.com.nc.service;

import ua.com.nc.dto.schedule.DesiredToSave;
import ua.com.nc.dto.schedule.GroupSchedule;
import ua.com.nc.dto.schedule.ScheduleForUser;

import java.util.List;

public interface DesiredScheduleService {

    /**
     * updates the group and its employees
     *
     * @param groupSchedule the DTO object from the front-end
     * @return id of the group
     */
    int update(GroupSchedule groupSchedule);

    /**
     * deletes the group
     *
     * @param groupId the id of group to be deleted
     * @return true in case of success
     */
    boolean delete(int groupId);

    /**
     * inserts the new group into the database and inserts all employees of it
     *
     * @param groupSchedule DTO object from the front-end
     * @return the id of just inserted group
     */
    int add(GroupSchedule groupSchedule);

    /**
     * sets that the employee attends the course to true if it is false and true if false
     *
     * @param userGroupId the id of the connection-object of the employee and the course
     *                    (that includes the group id and whether emp attends the course )
     */
    void invertAttending(Integer userGroupId);

    /**
     * returns analyzed desired schedule for users of the course, that have not been grouped yet
     *
     * @param courseId the id of the course
     * @return list of ScheduleForUser, for every ungrouped user
     * @throws Exception in case of error
     */
    List<ScheduleForUser> getDesiredScheduleForUngroupedStudentsOfCourse(int courseId) throws Exception;

    /**
     * returns analyzed desired schedule for users of the course,
     * that have already been grouped and the information about the group itself
     *
     * @param courseId the id of the course
     * @return list of GroupSchedule, for every group of the course
     * @throws Exception in case of error
     */
    List<GroupSchedule> getDesiredScheduleForFormedGroupsForCourse(int courseId) throws Exception;

    /**
     * @return list of the day intervals for the desired schedule (from hour - to hour)
     */
    List<String> getDayIntervals();

    /**
     * returns analyzed desired schedule for users of the group and the information about the group itself
     *
     * @param groupId the id of the group
     * @return an instance of GroupSchedule
     * @throws Exception in case of error
     */
    List<ScheduleForUser> getDesiredScheduleForGroup(int groupId) throws Exception;

    /**
     * saves to the database the desired schedule that the employee filled on the "join course" page
     * @param userId the id of the employee that wants to join the course
     * @param desiredToSave information about his schedule
     */
    void saveDesired(Integer userId, DesiredToSave desiredToSave);
}
