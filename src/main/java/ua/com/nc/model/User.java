package ua.com.nc.model;

import java.util.Objects;

public class User extends Entity<Integer>{
    private String email;
    private String passwordHash;
    private String firstname;
    private String lastname;
    private Integer managerId;
    private boolean isActive;

    public User() {
    }

    public User(Integer id, String email, String passwordHash, String firstname, String lastname, Integer managerId, boolean isActive) {
        super(id);
        this.setEmail(email);
        this.setPasswordHash(passwordHash);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setManagerId(managerId);
        this.setActive(isActive);
    }

    public User(String email, String passwordHash, String firstname, String lastname, Integer managerId, boolean isActive) {
        this.setEmail(email);
        this.setPasswordHash(passwordHash);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setManagerId(managerId);
        this.setActive(isActive);
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return isActive() == user.isActive() &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getPasswordHash(), user.getPasswordHash()) &&
                Objects.equals(getFirstname(), user.getFirstname()) &&
                Objects.equals(getLastname(), user.getLastname()) &&
                Objects.equals(getManagerId(), user.getManagerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getPasswordHash(), getFirstname(), getLastname(), getManagerId(), isActive());
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + getEmail() + '\'' +
                ", passwordHash='" + getPasswordHash() + '\'' +
                ", firstname='" + getFirstname() + '\'' +
                ", lastname='" + getLastname() + '\'' +
                ", managerId=" + getManagerId() +
                ", isActive=" + isActive() +
                '}';
    }

    private void setEmail(String email) {
        this.email = email.trim();
    }

    private void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash.trim();
    }

    private void setFirstname(String firstname) {
        this.firstname = firstname.trim();
    }

    private void setLastname(String lastname) {
        this.lastname = lastname.trim();
    }

    private void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    private void setActive(boolean active) {
        isActive = active;
    }
}
