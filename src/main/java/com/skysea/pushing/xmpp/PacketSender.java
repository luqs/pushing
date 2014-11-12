package com.skysea.pushing.xmpp;

import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

/**
 *
 * XMPP消息发送器。
 * Created by zhangzhi on 2014/11/12.
 */
interface PacketSender {

    /**
     * 返回一个用户的JID实例。
     * @param user 用户名。
     * @return
     */
    JID newJidForUser(String user);

    /**
     * 返回一个系统频道的JID实例。
     * @param channel 频道名称。
     * @return
     */
    JID newJidForSystem(String channel);

    /**
     * 发送一个XMPP数据包。
     * @param packet XMPP数据包。
     */
    void send(Packet packet) throws Exception;

}
