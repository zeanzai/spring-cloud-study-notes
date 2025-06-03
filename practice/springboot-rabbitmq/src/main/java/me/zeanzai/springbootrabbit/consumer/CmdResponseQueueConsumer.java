package me.zeanzai.springbootrabbit.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import me.zeanzai.springbootrabbit.entity.EquipmentEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CmdResponseQueueConsumer {
    @RabbitListener(queues = "cmdresponse")
    public void onMessage(@Payload EquipmentEntity equipment, Message message, Channel channel) throws Exception {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag,true);
        log.info("监听到： cmdresponse msg: {}", equipment.toString());
    }


}