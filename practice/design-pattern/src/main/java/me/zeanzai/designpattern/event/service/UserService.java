package me.zeanzai.designpattern.event.service;

import me.zeanzai.designpattern.event.EventBroadcaster;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private EventBroadcaster eventBroadcaster;


    public void registerUser(@PathVariable String username) {
        System.out.println(String.format("用户【%s】注册成功", username));
        eventBroadcaster.multicastEvent(new UserRegisterSuccessEvent(this, username));
    }

}
