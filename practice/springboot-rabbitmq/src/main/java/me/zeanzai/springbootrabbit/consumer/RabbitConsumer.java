package me.zeanzai.springbootrabbit.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import me.zeanzai.springbootrabbit.entity.DeviceCommondInfoEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/11
 */
@Component
@Slf4j
public class RabbitConsumer {

    @RabbitListener(queues = "deviceCmdQueue")
    public void onMessage(@Payload DeviceCommondInfoEntity deviceCommondInfoEntity, Channel channel) throws Exception {
        log.info("get msg: {}", deviceCommondInfoEntity.toString());
    }
}
