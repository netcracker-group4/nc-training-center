package ua.com.nc.dto;

public class DtoTeacherAndManager {
    private String firstname;
    private String lastname;

    public DtoTeacherAndManager(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
