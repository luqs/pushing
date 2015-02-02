package com.skysea.pushing.xmpp;

import com.google.gson.Gson;
import com.skysea.pushing.api.MessagePublisher;
import com.skysea.pushing.api.PublishException;
import com.skysea.pushing.util.ContractUtils;
import com.skysea.pushing.util.StringUtils;

import org.xmpp.forms.DataForm;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Message.Type;
import org.xmpp.packet.PacketExtension;

import java.util.Map;

/**
 * 基于XMPP协议的事件发布器。
 * Created by zhangzhi on 2014/11/12.
 */
final class XMPPMessagePublisher implements MessagePublisher {
    private final PacketSender sender;
    private final JID from;

    /**
     * 初始化XMPP事件发布器。
     * @param sender XMPP消息发送器。
     */
    XMPPMessagePublisher(PacketSender sender) {
        if (sender == null) {
            throw new NullPointerException("sender");
        }
        this.sender = sender;
        this.from = sender.newJidForSystem("event");
    }

    @Override
	public void publish(String from, String to,MessageKind messageKind, Map obj)
			throws PublishException {
    	ContractUtils.requiresNotNull(obj, "ArticleContent");
    	ContractUtils.requiresNotEmpty(from, "from");

        Message msg = new Message();
        msg.setType(Message.Type.headline);
        msg.setFrom(sender.newJidForUser(from));
        if (!StringUtils.isNullOrEmpty(to)) {
            /* 如果消息不是广播，则要设置收件人jid。 */
            msg.setTo(sender.newJidForUser(to));
        }
//        msg.setType(Message.Type.chat);
        msg.setBody(new Gson().toJson(obj));
       
        /* 增加事件协议扩展节点 */
        msg.addExtension(new MessagePacketExtension(messageKind));
       
        try {
            sender.send(msg);
        } catch (Exception exp) {
            throw new PublishException("send xmpp data fail.", exp);
        }
		
	}
    /**
     * 发布一个事件。
     *
     * @param user      事件关联的特定用户名，如果为空则表示事件是一个广播事件，
     *                  广播事件将会发布至所有用户。
     * @param event     事件名称。
     * @param eventArgs 事件参数列表。
     */
    public void publish(String user,String from, String event, Map<String, String> eventArgs) throws PublishException {
        
    }


    /**
     * 事件扩展节点。
     * <message from='user@skysea.com' to='user@skysea.com' type='chat' id='v2'>
     * <x xmlns=''http://skysea.com/protocol/message#extension'>
     * <x xmlns='jabber:x:data' type='result'>
     * <field var='EVENT_NAME'> <value>activity_deleted</value> </field>
     * <field var='activity_id'> <value>100</value> </field>
     * </x>
     * </x>
     * </message>
     */
    private final static class MessagePacketExtension extends PacketExtension {
        public static final String NAMESPACE = "http://skysea.com/protocol/message#extension";
        public static final String ELEMENT_NAME = "x";

        public MessagePacketExtension(MessageKind messageKind) {
            super(ELEMENT_NAME, NAMESPACE);
            this.element.addElement("contentType").addText(messageKind.getType());
        }
    }

    /**
     * 获得packe发送器。
     * @return
     */
    PacketSender getSender() {
        return sender;
    }

}
