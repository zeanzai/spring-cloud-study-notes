package me.zeanzai.jvmtest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/2
 */
@RestController
public class JvmThreadController {

    List<byte[]> memoryList = new ArrayList<>();

    @GetMapping("/memoryTest")
    public String memoryTest(int c) {
        byte[] b = new byte[c * 1024 * 1024];
        memoryList.add(b);
        return "success";
    }
}
