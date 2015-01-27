package com.skysea.pushing;

import com.skysea.pushing.api.EventPublisher;
import com.skysea.pushing.api.MessagePublisher;
import com.skysea.pushing.api.PublishException;
import com.skysea.pushing.api.PushInfrastructure;
import com.skysea.pushing.xmpp.XMPPPushInfrastructure;

import java.util.HashMap;


public class App {

    public static void main(String[] args) throws PublishException {
        /* 获得发布器工厂*/
        PushInfrastructure factory = new XMPPPushInfrastructure(
        		"skysea.com",    /* xmppdomain*/
        		"http://192.168.100.104:9090/plugins/push/packet"     /* pushGatewayUrl */);

        
        /* 获得事件发布器(可以单例保存) */
//        MessagePublisher messagePublisher = factory.getMessagePublisher();
//        HashMap<String, String> arti = new HashMap<String, String>();
//        arti.put("title", "value1");
//        arti.put("content", "value2");
//        arti.put("articleId", "value1");
//        arti.put("articlImg", "value2");
//        arti.put("articleUrl", "value1");
//        arti.put("userName", "admin");
//        messagePublisher.publish("admin", "183", arti);
        
        EventPublisher eventPublisher = factory.getEventPublisher();
        HashMap<String, String> sche = new HashMap<String, String>();
        sche.put("type", "modify");
        sche.put("scheduleId", "41");
        sche.put("userName", "luqs");
        /* 发布事件 */
        eventPublisher.publish("183"/* null：所有用户 */ ,"schedule", sche);
        
        
        System.out.println("publish success.");

    }
}
