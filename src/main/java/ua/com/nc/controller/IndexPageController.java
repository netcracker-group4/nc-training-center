package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.service.RoleService;

import java.util.List;

@Log4j
@Controller
@RequestMapping("/")
//@CrossOrigin(origins = "http://localhost:8000")
public class IndexPageController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String showIndexPage(@AuthenticationPrincipal User user, Model model){
        boolean isAuthorized = false;
        List<Role> userRoles = null;
        if(user != null){
            isAuthorized = true;
            userRoles = roleService.findAllByUserId(user.getId());
        }
        model.addAttribute("user", user);
        model.addAttribute("isAuthorized", isAuthorized);
        model.addAttribute("userRoles", userRoles);
        return "index";
    }
}
