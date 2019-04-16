package ua.com.nc.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.nc.dao.UserDao;
import ua.com.nc.domain.User;

@Log4j
@Controller
@RequestMapping("/")
public class IndexPageController {

    @Autowired
    private UserDao userDao;

    @GetMapping
    public String showIndexPage(){
       //log.debug(userDao.findByUsername("fobar@foobar.com"));
        return "index";
    }
}
