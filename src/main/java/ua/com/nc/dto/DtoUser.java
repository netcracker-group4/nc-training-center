package ua.com.nc.dto;

import ua.com.nc.domain.Role;

import java.util.List;

public class DtoUser {
    private Integer id;
    private String firstname;
    private String lastname;
    private List<Role> roles;
    private boolean isActive;

    public DtoUser(Integer id, String firstname, String lastname, List<Role> roles, boolean isActive) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.roles = roles;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public boolean isActive() {
        return isActive;
    }
}
