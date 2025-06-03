package me.zeanzai.springbootrabbit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/11
 */
@Configuration
@Slf4j
public class RabbitmqConfig {

    @Resource
    private CachingConnectionFactory connectionFactory;

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("deviceCmdExchange");
    }

    @Bean
    public Queue deviceCmdQueue(){
        return new Queue("deviceCmdQueue", true);
    }

    @Bean
    public Binding deviceCommandBinding(){
        return BindingBuilder.bind(deviceCmdQueue())
                .to(directExchange())
                .with("deviceBinding");
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        //设置“发送消息后进行确认”
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        //设置“发送消息后返回确认信息”
        connectionFactory.setPublisherReturns(true);
        //构造发送消息组件实例对象
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        //发送消息后，如果发送成功，则输出“消息发送成功”的反馈信息
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData,ack,cause));
        //发送消息后，如果发送失败，则输出“消息发送失败-消息丢失”的反馈信息
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message));
        //定义消息传输的格式为JSON字符串格式
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //最终返回RabbitMQ的操作组件实例RabbitTemplate
        return rabbitTemplate;
    }


}
