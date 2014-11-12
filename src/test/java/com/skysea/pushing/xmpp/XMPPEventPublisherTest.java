package com.skysea.pushing.xmpp;

import com.skysea.pushing.api.PublishException;
import mockit.Delegate;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.junit.Before;
import org.junit.Test;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

import java.util.HashMap;

import static org.junit.Assert.*;

public class XMPPEventPublisherTest {
    @Mocked PacketSender sender;
    XMPPEventPublisher publisher;


    @Before
    public void initialize() {
        final JID jid = new JID("user@skysea.com");
        final JID eventChannelJid = new JID("skysea.com/event");
        new NonStrictExpectations() {
            {
                sender.newJidForUser(jid.getNode());
                result = jid;

                sender.newJidForSystem("event");
                result = eventChannelJid;
            }
        };
        publisher = new XMPPEventPublisher(sender);
    }

    @Test
    public void testPublish() throws Exception {
        // Arrange
        String eventName = "activity_deleted";
        HashMap<String, String> eventArgs = new HashMap<String, String>();
        eventArgs.put("arg1", "value1");
        eventArgs.put("arg2", "value2");
        eventArgs.put("arg3", "value3");



        // Act
        publisher.publish("user", eventName, eventArgs);

        // Assert
        new Verifications() {
            {
                sender.send(with(new Delegate<Packet>() {
                    public void validate(Packet packet) {
                        assertEquals(
                                "<message from=\"skysea.com/event\" to=\"user@skysea.com\" type=\"headline\">" +
                                        "<x xmlns=\"http://skysea.com/protocol/event\">" +
                                        "<x xmlns=\"jabber:x:data\" type=\"result\">" +
                                        "<field var=\"EVENT_NAME\"><value>activity_deleted</value></field>" +
                                        "<field var=\"arg3\"><value>value3</value></field>" +
                                        "<field var=\"arg2\"><value>value2</value></field>" +
                                        "<field var=\"arg1\"><value>value1</value></field>" +
                                        "</x>" +
                                        "</x>" +
                                        "</message>", packet.toXML());
                    }
                }));
                times = 1;
            }
        };
    }


    @Test(expected = PublishException.class)
    public void testPublish_When_Send_Exception() throws Exception {
        // Arrange
        String eventName = "activity_deleted";

        new NonStrictExpectations() {
            {
                sender.send(withAny((Packet)null));
                result = new Exception();
            }
        };

        // Act
        publisher.publish("user", eventName, null);
    }
}