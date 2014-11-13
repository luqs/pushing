package com.skysea.pushing;

import com.skysea.pushing.api.EventPublisher;
import com.skysea.pushing.api.PublishException;
import com.skysea.pushing.api.PushInfrastructure;
import com.skysea.pushing.xmpp.XMPPPushInfrastructure;

import java.util.HashMap;


public class App {

    public static void main(String[] args) throws PublishException {
        /* 获得发布器工厂*/
        PushInfrastructure factory = new XMPPPushInfrastructure(
                args[0],    /* xmppdomain*/
                args[1]     /* pushGatewayUrl */);

        /* 获得事件发布器(可以单例保存) */
        EventPublisher eventPublisher = factory.getEventPublisher();

        HashMap<String, String> eventArgs = new HashMap<String, String>();
        eventArgs.put("arg1", "value1");
        eventArgs.put("arg2", "value2");

        /* 发布事件 */
        eventPublisher.publish(null/* null：所有用户 */ ,"event_name", eventArgs);
        System.out.println("publish success.");

    }
}
