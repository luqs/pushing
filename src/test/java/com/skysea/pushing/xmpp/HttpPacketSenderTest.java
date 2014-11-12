package com.skysea.pushing.xmpp;

import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.junit.Before;
import org.junit.Test;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

import static org.junit.Assert.*;

public class HttpPacketSenderTest {
    private HttpPacketSender sender;
    @Before
    public void initialize() {
        sender = new HttpPacketSender(
                "skysea.com",
                "http://skysea.com/plugins/push/packet");
    }

    @Test
    public void testNewJidForUser() throws Exception {
        assertEquals(new JID("user@skysea.com"), sender.newJidForUser("user"));
    }

    @Test
    public void testNewJidForSystem() throws Exception {
        assertEquals(new JID("skysea.com/event"), sender.newJidForSystem("event"));
    }

    @Test
    public void testSend() throws Exception {
        // Arrange
        final Message msg = new Message();
        msg.setFrom("skysea.com");
        msg.setTo("user@skysea.com");
        msg.setBody("test");
        new NonStrictExpectations(sender){
            {
                sender.sendRawPacket(msg.toXML());
                result = "ok";
                times = 1;
            }
        };

        // Act
        sender.send(msg);
    }


    @Test(expected = IllegalStateException.class)
    public void testSend_When_Response_Is_Not_OK() throws Exception {
        // Arrange
        final Message msg = new Message();
        msg.setFrom("skysea.com");
        msg.setTo("user@skysea.com");
        msg.setBody("test");

        new NonStrictExpectations(sender){
            {
                sender.sendRawPacket(msg.toXML());
                result = "fail";
                times = 1;
            }
        };

        // Act
        sender.send(msg);
    }
}