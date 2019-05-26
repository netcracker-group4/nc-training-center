package ua.com.nc.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.nc.exceptions.NoSuchUserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Log4j2
@Controller
public class AuthorizationController {

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String loginPage(@RequestParam(required = false, name = "error") String error) {
        if (error != null) {
            log.warn("Error while logging in");
            throw new NoSuchUserException("There is no user with such email and password");
        }
        log.info("returning index page");
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.info("log outing");
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        log.info("redirecting to login page after logout");
        return "redirect:/login";
    }

}
