package me.zeanzai.designpattern.event;

/**
 * 事件广播器：
 * 1.负责事件监听器的管理（注册监听器&移除监听器，将事件和监听器关联起来）
 * 2.负责事件的广播（将事件广播给所有的监听器，对该事件感兴趣的监听器会处理该事件）
 */
public interface EventBroadcaster {

    /**
     * 广播事件给所有的监听器，对该事件感兴趣的监听器会处理该事件
     *
     * @param event
     */
    void multicastEvent(AbstractEvent event);

    /**
     * 添加一个事件监听器（监听器中包含了监听器中能够处理的事件）
     *
     * @param listener 需要添加监听器
     */
    void addEventListener(EventListener<?> listener);


    /**
     * 将事件监听器移除
     *
     * @param listener 需要移除的监听器
     */
    void removeEventListener(EventListener<?> listener);
}
