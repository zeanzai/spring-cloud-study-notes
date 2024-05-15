package me.zeanzai.designpattern.event.controller;

import me.zeanzai.designpattern.event.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/reg/{username}")
    public void registerUser(@PathVariable String username) {
        userService.registerUser(username);

    }
}
