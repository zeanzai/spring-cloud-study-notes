package me.zeanzai.springbootrabbit.controller;

import me.zeanzai.springbootrabbit.service.RedisLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/23
 */
@RestController
@RequestMapping("/test")
public class RedisLimitController {

    @Autowired
    private RedisLimitService redisLimitService;

    @RequestMapping("/redislimit")
    public void redislimit(){
        redisLimitService.limit();
    }
}
