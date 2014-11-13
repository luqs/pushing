package com.skysea.pushing.xmpp;

import org.junit.Test;

import static org.junit.Assert.*;

public class XMPPPushInfrastructureTest {

    @Test
    public void testGetEventPublisher() throws Exception {
        // Arrange
        String xmppDomain = "skysea.com";
        String gatewayUrl = "http://skysea.com/push/packet";
        XMPPPushInfrastructure factory = new XMPPPushInfrastructure(xmppDomain, gatewayUrl);

        // Act
        XMPPEventPublisher publisher = (XMPPEventPublisher)factory.getEventPublisher();

        // Assert
        assertEquals(xmppDomain, ((HttpPacketSender)publisher.getSender()).getXmppDomain());
        assertEquals(gatewayUrl, ((HttpPacketSender)publisher.getSender()).getPushGatewayUrl().toString());
    }
}