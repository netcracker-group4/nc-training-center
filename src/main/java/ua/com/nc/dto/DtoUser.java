package ua.com.nc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.com.nc.domain.Role;

import java.util.List;

@Data
public class DtoUser {
    private Integer id;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private List<Role> roles;
    private boolean isActive;


    public DtoUser() {
    }

    public DtoUser(Integer id, String firstName, String lastName, List<Role> roles, boolean isActive, String photoUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.isActive = isActive;
        this.photoUrl = photoUrl;
    }

    public DtoUser(Integer id) {
        this.id = id;
    }


}
