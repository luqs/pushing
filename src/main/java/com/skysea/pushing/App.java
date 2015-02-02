package com.skysea.pushing;

import com.skysea.pushing.api.EventPublisher;
import com.skysea.pushing.api.MessagePublisher;
import com.skysea.pushing.api.PublishException;
import com.skysea.pushing.api.PushInfrastructure;
import com.skysea.pushing.xmpp.MessageKind;
import com.skysea.pushing.xmpp.XMPPPushInfrastructure;

import java.util.HashMap;


public class App {

    public static void main(String[] args) throws PublishException {
        /* 获得发布器工厂*/
        PushInfrastructure factory = new XMPPPushInfrastructure(
        		"skysea.com",    /* xmppdomain*/
        		"http://192.168.100.104:9090/plugins/push/packet"     /* pushGatewayUrl */);

        
        /* 获得事件发布器(可以单例保存) */
        MessagePublisher messagePublisher = factory.getMessagePublisher();
        HashMap<String, String> arti = new HashMap<String, String>();
        arti.put("title", "中华人民共和国万岁1");
        arti.put("content", "今天明天都会成为昨天简单的生活 +关注 私信 =超过500万人正在使用... 我刚更新了新浪微博KJAVA客户端2.7.0版,推荐你也一起来体验,好东西一定要...");
        arti.put("articleId", "1");
        arti.put("articlImg", "http://i7.baidu.com/it/u=475759650,3718589085&fm=96&s=B9D22BD04E1079CC60B4077C030050F4");
        arti.put("articleUrl", "http://i7.baidu.com/it/u=475759650,3718589085&fm=96&s=B9D22BD04E1079CC60B4077C030050F4");
        //arti.put("userName", "admin");  delete from map
        messagePublisher.publish("admin", "8000",MessageKind.ARTICLE, arti);
        
//        EventPublisher eventPublisher = factory.getEventPublisher();
//        HashMap<String, String> sche = new HashMap<String, String>();
//        sche.put("type", "modify");
//        sche.put("scheduleId", "41");
//       // sche.put("userName", "luqs"); delete from map
//        /* 发布事件 */
//        eventPublisher.publish(null/* null：所有用户 */ ,"schedule", sche);
        
        
        System.out.println("publish success.");

    }
}
