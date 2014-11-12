package com.skysea.pushing.api;

/**
 * 发布器抽象工厂。
 * Created by zhangzhi on 2014/11/12.
 */
public interface PublisherFactory {

    /**
     * 获得事件发布器对象实例。
     * @return 事件发布器。
     */
    EventPublisher getEventPublisher();
}
