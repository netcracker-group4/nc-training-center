package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.nc.domain.Role;
import ua.com.nc.domain.User;
import ua.com.nc.service.RoleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Log4j2
@Controller
@RequestMapping("/")
public class IndexPageController {

    @Autowired
    private RoleService roleService;


    @RequestMapping(method = RequestMethod.GET)
    public String showIndexPage(@AuthenticationPrincipal User user, Model model, HttpServletRequest request) {
        boolean isAuthorized = false;
        List<Role> userRoles = null;
        if (user != null) {
            isAuthorized = true;
            userRoles = roleService.findAllByUserId(user.getId());
        }
        model.addAttribute("user", user);
        model.addAttribute("isAuthorized", isAuthorized);
        model.addAttribute("userRoles", userRoles);
        return "index";
    }
}
