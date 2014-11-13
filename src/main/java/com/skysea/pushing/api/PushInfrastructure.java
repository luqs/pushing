package com.skysea.pushing.api;

/**
 * 用于消息推送的基础设施。
 * Created by zhangzhi on 2014/11/12.
 */
public interface PushInfrastructure {

    /**
     * 获得事件发布器对象实例。
     * @return 事件发布器。
     */
    EventPublisher getEventPublisher();
}
