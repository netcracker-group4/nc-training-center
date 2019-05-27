package ua.com.nc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.nc.domain.User;

@Data
@NoArgsConstructor
public class DtoTeacherAndManager {
    private Integer id;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private boolean isActive;
    private String email;


    public DtoTeacherAndManager(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.imageUrl = user.getImageUrl();
        this.isActive = user.isActive();
        this.email = user.getEmail();
    }



    public DtoTeacherAndManager(Integer id, String firstName, String lastName, String photoUrl, boolean isActive, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = photoUrl;
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
