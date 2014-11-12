package com.skysea.pushing.xmpp;

import com.skysea.pushing.api.EventPublisher;
import com.skysea.pushing.api.PublisherFactory;

/**
 * XMPP发布器工厂实现。
 * Created by zhangzhi on 2014/11/12.
 */
public final class XMPPPublisherFactory implements PublisherFactory {
    private final HttpPacketSender sender;
    private final XMPPEventPublisher eventPublisher;

    /**
     * 实例化XMPP发布器工厂。
     * @param xmppDomain XMPP域。
     * @param pushGatewayUrl 推送网关Url地址。
     */
    public XMPPPublisherFactory(String xmppDomain, String pushGatewayUrl) {
        sender = new HttpPacketSender(xmppDomain, pushGatewayUrl);
        eventPublisher = new XMPPEventPublisher(sender);
    }

    /**
     * 获得事件发布器实例。
     * @return
     */
    @Override
    public EventPublisher getEventPublisher() {
        return eventPublisher;
    }
}
