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
//        MessagePublisher messagePublisher = factory.getMessagePublisher();
//        HashMap<String, String> arti = new HashMap<String, String>();
//        arti.put("title", "专家解读周永康等搞非组织政治活动:有“反党”属性");
//        arti.put("content", "新华社电（记者 罗沙）最高人民法院18日发布的《人民法院工作年度报告（2014）》（白皮书）提出...");
//        arti.put("articleId", "1");
//        arti.put("articleImg", "http://g.hiphotos.baidu.com/image/pic/item/0df3d7ca7bcb0a464c493d246963f6246b60af40.jpg");
//        arti.put("articleUrl", "http://xinwen.ynet.com/3.1/1503/19/9919178.html");
//        //arti.put("userName", "admin");  delete from map
//        messagePublisher.publish("skysea_public", "183",MessageKind.ARTICLE, arti);
        
        EventPublisher eventPublisher = factory.getEventPublisher();
        HashMap<String, String> sche = new HashMap<String, String>();
        sche.put("type", "modify");
        sche.put("scheduleId", "41");
       // sche.put("userName", "luqs"); delete from map
        /* 发布事件 */
        eventPublisher.publish(null/* null：所有用户 */ ,"schedule", sche);
        
        
        System.out.println("publish success.");
        
    }
}
