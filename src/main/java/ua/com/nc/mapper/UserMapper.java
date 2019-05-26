package ua.com.nc.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoUserSave;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Component
public class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User toModel(final DtoUserSave dtoUserSave) {
        User user = new User();

        user.setId(dtoUserSave.getId());
        user.setFirstName(dtoUserSave.getFirstName());
        user.setLastName(dtoUserSave.getLastName());
        user.setPassword(passwordEncoder.encode(dtoUserSave.getPassword()));
        user.setEmail(dtoUserSave.getEmail());
        user.setCreated(new Timestamp(System.currentTimeMillis()));

        return user;
    }
}
