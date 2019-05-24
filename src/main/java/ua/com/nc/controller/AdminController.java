package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.nc.service.UserService;

@Log4j2
@Controller
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority(T(ua.com.nc.domain.Role).ADMIN.name())")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }
}
