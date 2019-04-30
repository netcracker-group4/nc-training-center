package ua.com.nc.dto;

import ua.com.nc.domain.Role;

import java.util.List;

public class DtoUser {
    private Integer id;
    private String firstName;
    private String lastName;
    private List<Role> roles;
    private boolean isActive;

    public DtoUser(Integer id, String firstName, String lastName, List<Role> roles, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.isActive = isActive;
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

    public List<Role> getRoles() {
        return roles;
    }

    public boolean isActive() {
        return isActive;
    }
}
