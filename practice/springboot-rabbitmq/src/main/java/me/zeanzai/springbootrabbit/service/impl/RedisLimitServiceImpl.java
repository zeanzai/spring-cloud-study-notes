package me.zeanzai.springbootrabbit.service.impl;

import me.zeanzai.springbootrabbit.service.RedisLimitService;
import me.zeanzai.springbootrabbit.utils.RedisLimitByUseLua;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/23
 */
@Service
public class RedisLimitServiceImpl implements RedisLimitService {

    @Override
    public void limit() {
        try {
            RedisLimitByUseLua.accquire();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
}
