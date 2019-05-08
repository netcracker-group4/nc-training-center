package ua.com.nc.dto;

public class DtoGroup {
    private Integer id;
    private Integer courseId;
    private String title;
    private String courseName;

    public DtoGroup() {
    }

    public DtoGroup(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public DtoGroup(Integer id, String title, int courseId, String courseName) {
        this.id = id;
        this.title = title;
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }
}
