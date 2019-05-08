package ua.com.nc.dto;

import lombok.Data;
import ua.com.nc.domain.Role;

import java.util.List;

@Data
public class DtoUserProfiles {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    private List<Role> roles;
    private boolean isActive;
    private DtoTeacherAndManager dtoManager;
    private List<DtoTeacherAndManager> dtoTeachers;
    private List<DtoGroup> groups;

    DtoUserProfiles(){
    }

    public DtoUserProfiles(Integer id, String firstName, String lastName, String email, String image, List<Role> roles, boolean isActive, DtoTeacherAndManager dtoManager, List<DtoTeacherAndManager> dtoTeachers, List<DtoGroup> groups) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
        this.roles = roles;
        this.isActive = isActive;
        this.dtoManager = dtoManager;
        this.dtoTeachers = dtoTeachers;
        this.groups = groups;
    }

}
