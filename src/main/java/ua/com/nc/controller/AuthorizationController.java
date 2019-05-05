package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
//import ua.com.nc.exceptions.NoSuchUserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j
@Controller
//@CrossOrigin(origins = "http://localhost:8000")
public class AuthorizationController {

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String login(@RequestParam(required = false, name="error") String error){
        if(error != null){
           // throw new NoSuchUserException("There is no user with such email and password");
        }
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public String logout(   HttpServletRequest request,
                            HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
}
