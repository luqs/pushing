package com.skysea.pushing.xmpp;

import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.xmpp.packet.Message;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;

import static org.junit.Assert.*;

/**
 * HTTP XMPP包发送器的集成测试。
 */
public class HttpPacketSenderIntegrationTest {
    private static TestWebServer webServer;
    private static HttpPacketSender sender;
    private static TestHandler handler;


    @BeforeClass
    public static void classInitialize() throws Exception {
        webServer = new TestWebServer();
        handler = new TestHandler();
        webServer.addHandler("/", handler);
        webServer.start();
        sender = new HttpPacketSender("skysea.com", webServer.getPath("/"));
    }

    @AfterClass
    public static void clean() throws Exception{
        webServer.shutdown();
    }

    @Test
    public void testSend() throws Exception {
        // Arrange
        final Message msg = new Message();
        msg.setFrom("skysea.com");
        msg.setTo("user@skysea.com");
        msg.setBody("test");

        // Act
        sender.send(msg);

        // Assert
        assertEquals(msg.toXML(), handler.content);
    }

    public static class TestHandler extends AbstractHandler {
        private String content;

        @Override
        public void handle(String s, Request request,
                           HttpServletRequest serReq,
                           HttpServletResponse serRes) throws IOException, ServletException {

            content = serReq.getParameter(HttpPacketSender.PACKET_CONTENT_PARAMETER_NAME);
            serRes.setStatus(HttpServletResponse.SC_OK);
            serRes.getWriter().write("ok");
            serRes.getWriter().flush();
            serRes.getWriter().close();
        }
    }

}