package me.zeanzai.springbootrabbit.controller;

import me.zeanzai.springbootrabbit.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/11
 */
@RestController
@RequestMapping("/command")
public class CommandController {

    @Autowired
    private CommandService commandService;

    @GetMapping("/genDevice")
    public Long genDevice(@RequestParam("count") int count) {
        return commandService.genDevice(count);
    }

    @GetMapping("/produceOneMsg")
    public void produceOneMsg() {
        commandService.sendSingleMsg();
    }

    @GetMapping("/syncChaobiao")
    public void syncChaobiao() {
        commandService.syncChaobiao();
    }

    @GetMapping("/syncChaobiao1")
    public void syncChaobiao1() {
        commandService.syncChaobiao1();
    }

    @GetMapping("/getsyncChaobiao1")
    public void getsyncChaobiao1() {
        commandService.syncChaobiao1();
    }


}
