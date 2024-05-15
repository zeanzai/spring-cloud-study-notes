package me.zeanzai.designpattern.event.config;

import me.zeanzai.designpattern.event.EventBroadcaster;
import me.zeanzai.designpattern.event.EventListener;
import me.zeanzai.designpattern.event.SimpleBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class EventConfig {
    @Bean
    @Autowired(required = false)
    public EventBroadcaster eventBroadcaster(List<EventListener> eventListeners) { //@1
        EventBroadcaster eventPublisher = new SimpleBroadcaster();
        if (eventListeners != null) {
            eventListeners.forEach(eventPublisher::addEventListener);
        }
        return eventPublisher;
    }

    /**
     * 注册一个bean：用户注册服务
     *
     * @param eventMulticaster
     * @return
     */
}
