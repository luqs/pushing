package com.skysea.pushing.xmpp;

import com.skysea.pushing.util.ContractUtils;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * 通过HTTP接口发送的XMPP数据包发送器。
 * 这需要openfire服务端的skysea push插件支持。
 * <p/>
 * 发送请求报文如下所示：
 * POST /plugins/push/packet HTTP/1.1
 * Host: skysea.com:9090
 * Content-Type: application/x-www-form-urlencoded
 * Content-Length: 131
 * <p/>
 * packet_content=%3Cmessage+from%3D%27skysea.com%2
 * 7%3E%0D%0A%3Cbody%3Ehi%21+all.%3C%2Fbody%3E%0D%0
 * A%3C%2Fmessage%3E%0D%0A++++++++++++
 * <p/>
 * Created by zhangzhi on 2014/11/12.
 */
final class HttpPacketSender implements PacketSender {
    public final static String PACKET_CONTENT_PARAMETER_NAME = "packet_content";
    private final static String PUSH_OK_STATUS = "ok";
    private final static Charset CHARSET = Charset.forName("utf-8");
    private final String xmppDomain;
    private final URL pushGatewayUrl;

    /**
     * 初始化HTTP的XMPP数据包发送器。
     *
     * @param xmppDomain    XMPP域名。
     * @param pushGatewayUrl 发送器HTTP提交的目标地址。
     */
    HttpPacketSender(String xmppDomain, String pushGatewayUrl) {
        ContractUtils.requiresNotEmpty(xmppDomain, "xmppDomain");
        ContractUtils.requiresNotEmpty(pushGatewayUrl, "pushGatewayUrl");

        try {
            this.xmppDomain = xmppDomain;
            this.pushGatewayUrl = new URL(pushGatewayUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("pushGatewayUrl");
        }
    }

    /**
     * 生成用户JID 如：user@skysea.com
     *
     * @param user 用户名。
     * @return
     */
    @Override
    public JID newJidForUser(String user) {
        return new JID(user, xmppDomain, null);
    }

    /**
     * 生成系统频道JID 如：skysea.com/event
     *
     * @param channel 频道名称。
     * @return
     */
    @Override
    public JID newJidForSystem(String channel) {
        return new JID(null, xmppDomain, channel);
    }

    /**
     * 通过HTTP发送XMPP数据包。
     *
     * @param packet XMPP数据包。
     * @throws Exception
     */
    @Override
    public void send(Packet packet) throws Exception {
        assert packet != null;

        String status = sendRawPacket(packet.toXML());
        if (!PUSH_OK_STATUS.equalsIgnoreCase(status)) {
            throw new IllegalStateException("send response status error : " + status);
        }
    }

    /**
     * 发送原始的数据包。
     * @param raw
     * @return
     * @throws IOException
     */
    String sendRawPacket(String raw) throws IOException {
        HttpURLConnection con = openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);

        byte[] data = getData(raw);

        try {
            sendRequest(con, data);
            return receiveResponse(con);
        } finally {
            con.disconnect();
        }
    }

    /**
     * 获得xmpp域名。
     * @return
     */
    String getXmppDomain() {
        return xmppDomain;
    }

    /**
     * 获得推送网关url。
     * @return
     */
    URL getPushGatewayUrl() {
        return pushGatewayUrl;
    }


    /* 发送请求数据 */
    private void sendRequest(HttpURLConnection con, byte[] data) throws IOException {
        con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
        con.setRequestProperty("content-length", String.valueOf(data.length));
        OutputStream out = con.getOutputStream();
        try {
            out.write(data);
            out.flush();
        } finally {
            out.close();
        }
    }

    /* 接收响应*/
    private String receiveResponse(HttpURLConnection con) throws IOException {
        InputStream in = con.getInputStream();
        try {
            return readToEnd(in);
        } finally {
            in.close();
        }
    }

    /**
     * 将流读取到结束。
     */
    private String readToEnd(InputStream in) throws IOException {
        StringBuilder sb            = new StringBuilder();
        InputStreamReader reader    = new InputStreamReader(in, CHARSET);
        char[] buffer               = new char[128];
        int readBytes;

        while ((readBytes = reader.read(buffer)) > 0) {
            sb.append(buffer, 0, readBytes);
        }
        return sb.toString().trim();
    }


    private byte[] getData(String rawPacket) {
        return (encode(PACKET_CONTENT_PARAMETER_NAME) + "=" + encode(rawPacket)).getBytes(CHARSET);
    }

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private HttpURLConnection openConnection() throws IOException {
        return (HttpURLConnection) pushGatewayUrl.openConnection();
    }
}
