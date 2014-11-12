package com.skysea.pushing.api;

import java.util.Map;

/**
 * 事件发布器，提供向用户客户端发送分布式事件通知的功能。
 * Created by zhangzhi on 2014/11/12.
 */
public interface EventPublisher {

    /**
     * 发布一个事件
     *
     * @param user      事件关联的特定用户名，如果为空则表示事件是一个广播事件，
     *                  广播事件将会发布至所有用户。
     * @param event     事件名称。
     * @param eventArgs 事件参数列表。
     */
    void publish(String user, String event, Map<String, String> eventArgs) throws PublishException;
}
