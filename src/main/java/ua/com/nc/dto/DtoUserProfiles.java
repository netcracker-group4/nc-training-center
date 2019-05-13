package ua.com.nc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;

import java.util.List;

@Data
@NoArgsConstructor
public class DtoUserProfiles {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    private List<Role> roles;
    private boolean isActive;
    private DtoTeacherAndManager dtoManager;

    public DtoUserProfiles(Integer id, String firstName, String lastName, String email, String image,
                           List<Role> roles, boolean isActive, DtoTeacherAndManager dtoManager) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
        this.roles = roles;
        this.isActive = isActive;
        this.dtoManager = dtoManager;
    }

    public DtoUserProfiles(User user, List<Role> roles, boolean isActive, DtoTeacherAndManager dtoManager) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.image = user.getImageUrl();
        this.roles = roles;
        this.isActive = isActive;
        this.dtoManager = dtoManager;
    }
}
