package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.domain.User;


@Log4j
@Controller
@RequestMapping("/")
public class IndexPageController {

    @Autowired
    private IUserDao userDao;

    @GetMapping
    public String showIndexPage(@AuthenticationPrincipal User user, Model model){
        boolean isAuthorized = false;
        if(user != null){
            isAuthorized = true;
        }
        model.addAttribute("user", user);
        model.addAttribute("isAuthorized", isAuthorized);
        return "index";
    }
}
