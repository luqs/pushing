package com.skysea.pushing.xmpp;

import org.junit.Test;

import static org.junit.Assert.*;

public class XMPPPublisherFactoryTest {

    @Test
    public void testGetEventPublisher() throws Exception {
        // Arrange
        String xmppDomain = "skysea.com";
        String gatewayUrl = "http://skysea.com/push/packet";
        XMPPPublisherFactory factory = new XMPPPublisherFactory(xmppDomain, gatewayUrl);

        // Act
        XMPPEventPublisher publisher = (XMPPEventPublisher)factory.getEventPublisher();

        // Assert
        assertEquals(xmppDomain, ((HttpPacketSender)publisher.getSender()).getXmppDomain());
        assertEquals(gatewayUrl, ((HttpPacketSender)publisher.getSender()).getPushGatewayUrl().toString());
    }
}