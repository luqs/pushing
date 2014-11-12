package com.skysea.pushing.xmpp;

import org.eclipse.jetty.server.AbstractNetworkConnector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

/**
* Created by zhangzhi on 2014/11/12.
*/
public class TestWebServer {
    private Server server;
    private ContextHandlerCollection handlerCollection;

    public TestWebServer() {
        server = new Server(0);
        handlerCollection = new ContextHandlerCollection();
        server.setHandler(handlerCollection);
    }

    public String getHost() {
        AbstractNetworkConnector connector = (AbstractNetworkConnector) server.getConnectors()[0];
        return String.format("http://localhost:%d", connector.getLocalPort());
    }

    public String getPath(String pathInfo) {
        if (pathInfo.startsWith("/")) {
            return getHost() + pathInfo;
        } else {
            return getHost() + "/" + pathInfo;
        }
    }

    public void addHandler(String path, Handler handler) {
        ContextHandler context = new ContextHandler(path);
        context.setHandler(handler);
        handlerCollection.addHandler(context);
    }

    public void start() throws Exception {
        server.start();
    }

    public void shutdown() throws Exception {
        server.stop();
    }
}
