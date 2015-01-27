package com.skysea.pushing.xmpp;

import com.skysea.pushing.api.EventPublisher;
import com.skysea.pushing.api.MessagePublisher;
import com.skysea.pushing.api.PushInfrastructure;


/**
 * XMPP消息推送设施。
 * Created by zhangzhi on 2014/11/12.
 */
public final class XMPPPushInfrastructure implements PushInfrastructure {
    private final XMPPEventPublisher eventPublisher;
    private final XMPPMessagePublisher messagePublisher;

    /**
     * 实例化XMPP消息推送设施。
     * @param xmppDomain XMPP域。
     * @param pushGatewayUrl 推送网关Url地址。
     */
    public XMPPPushInfrastructure(String xmppDomain, String pushGatewayUrl) {
        HttpPacketSender sender = new HttpPacketSender(xmppDomain, pushGatewayUrl);
        eventPublisher = new XMPPEventPublisher(sender);
        messagePublisher = new XMPPMessagePublisher(sender);
    }

    /**
     * 获得事件发布器实例。
     * @return
     */
    @Override
    public EventPublisher getEventPublisher() {
        return eventPublisher;
    }
    /**
     * 获得事件发布器实例。
     * @return
     */
    @Override
    public MessagePublisher getMessagePublisher() {
    	return messagePublisher;
    }
}
