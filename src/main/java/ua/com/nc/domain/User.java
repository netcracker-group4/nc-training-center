package ua.com.nc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class User extends Entity implements UserDetails {
    private String email;
    private transient String password;
    private String firstName;
    private String lastName;
    private String token;
    private OffsetDateTime created;
    private Integer managerId;
    private String imageUrl;
    private boolean isActive;
    private boolean isOnLandingPage;
    private String description;
    private Set<Role> roles;

    public User() {
    }

    public User(Integer id, String email, String password, String firstName, String lastName, String token, OffsetDateTime created, Integer managerId, String image, boolean isActive) {
        super(id);
        this.setEmail(email);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setToken(token);
        this.setCreated(created);
        this.setManagerId(managerId);
        this.setImageUrl(image);
        this.setActive(isActive);
    }

    public User(String email, String password, String firstName, String lastName, String token, OffsetDateTime created, Integer managerId, String image, boolean isActive) {
        this.setEmail(email);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setToken(token);
        this.setCreated(created);
        this.setManagerId(managerId);
        this.setImageUrl(image);
        this.setActive(isActive);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (this.roles != null && !this.roles.isEmpty()) {
            for (Role role : this.roles) {
                authorities.add(new SimpleGrantedAuthority(role.name()));
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }


}
