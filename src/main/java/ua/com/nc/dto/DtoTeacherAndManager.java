package ua.com.nc.dto;

public class DtoTeacherAndManager {
    private Integer id;
    private String firstName;
    private String lastName;
    private String imageUrl;

    public DtoTeacherAndManager() {
    }

    public DtoTeacherAndManager(Integer id, String firstName, String lastName, String imageUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
