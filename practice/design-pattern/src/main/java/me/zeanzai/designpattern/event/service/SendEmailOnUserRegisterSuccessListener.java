package me.zeanzai.designpattern.event.service;


import me.zeanzai.designpattern.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 用户注册成功事件监听器->负责给用户发送邮件
 */
@Component
public class SendEmailOnUserRegisterSuccessListener implements EventListener<UserRegisterSuccessEvent> {
    @Override
    public void onEvent(UserRegisterSuccessEvent event) {
        System.out.println(
                String.format("给用户【%s】发送注册成功邮件!", event.getUserName()));
    }
}
