package ua.com.nc.dto;

import lombok.Data;
import ua.com.nc.domain.User;

@Data
public class DtoTeacherAndManager {
    private boolean isActive;
    private Integer id;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private String email;


    DtoTeacherAndManager() {

    }

    public DtoTeacherAndManager(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.photoUrl = user.getImageUrl();
        this.isActive = user.isActive();
        this.email = user.getEmail();
    }



    public DtoTeacherAndManager(Integer id, String firstName, String lastName, String photoUrl, boolean isActive, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUrl = photoUrl;
        this.isActive = isActive;
        this.email = email;
    }


    public DtoTeacherAndManager(Integer id, String firstName, String lastName, boolean isActive, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.email = email;
    }

}
