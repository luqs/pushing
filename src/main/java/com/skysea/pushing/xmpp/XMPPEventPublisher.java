package com.skysea.pushing.xmpp;

import com.skysea.pushing.api.EventPublisher;
import com.skysea.pushing.api.PublishException;
import com.skysea.pushing.util.ContractUtils;
import com.skysea.pushing.util.StringUtils;
import org.xmpp.forms.DataForm;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.PacketExtension;

import java.util.Map;

/**
 * 基于XMPP协议的事件发布器。
 * Created by zhangzhi on 2014/11/12.
 */
final class XMPPEventPublisher implements EventPublisher {
    private final PacketSender sender;
    private final JID from;

    /**
     * 初始化XMPP事件发布器。
     * @param sender XMPP消息发送器。
     */
    XMPPEventPublisher(PacketSender sender) {
        if (sender == null) {
            throw new NullPointerException("sender");
        }
        this.sender = sender;
        this.from = sender.newJidForSystem("event");
    }

    /**
     * 发布一个事件。
     *
     * @param user      事件关联的特定用户名，如果为空则表示事件是一个广播事件，
     *                  广播事件将会发布至所有用户。
     * @param event     事件名称。
     * @param eventArgs 事件参数列表。
     */
    @Override
    public void publish(String user, String event, Map<String, String> eventArgs) throws PublishException {
        ContractUtils.requiresNotEmpty(event, "event");

        Message packet = newEventMessage(user);
        /* 增加事件协议扩展节点 */
        packet.addExtension(createEventExtension(event, eventArgs));

        try {
            sender.send(packet);
        } catch (Exception exp) {
            throw new PublishException("send xmpp data fail.", exp);
        }
    }

    /**
     * 实例化一个事件消息。
     */
    private Message newEventMessage(String user) {
        Message msg = new Message();
        msg.setFrom(from);
        if (!StringUtils.isNullOrEmpty(user)) {
            /* 如果消息不是广播，则要设置收件人jid。 */
            msg.setTo(sender.newJidForUser(user));
        }
        msg.setType(Message.Type.headline);
        return msg;
    }

    /**
     * 生成事件协议扩展节点。
     *
     * @param event
     * @param eventArgs
     * @return
     */
    private PacketExtension createEventExtension(String event, Map<String, String> eventArgs) {
        DataForm form = new DataForm(DataForm.Type.result);

        /* 事件名称字段 */
        form.addField("EVENT_NAME", null, null).addValue(event);

        /* 添加每个事件参数 */
        if (eventArgs != null) {
            for (Map.Entry<String, String> arg : eventArgs.entrySet()) {
                form.addField(arg.getKey(), null, null).addValue(arg.getValue());
            }
        }
        return new EventPacketExtension(form);
    }

    /**
     * 事件扩展节点。
     * <message from='skysea.com/event' to='user@skysea.com' type='headline' id='v2'>
     * <x xmlns='http://skysea.com/protocol/event'>
     * <x xmlns='jabber:x:data' type='result'>
     * <field var='EVENT_NAME'> <value>activity_deleted</value> </field>
     * <field var='activity_id'> <value>100</value> </field>
     * </x>
     * </x>
     * </message>
     */
    private final static class EventPacketExtension extends PacketExtension {
        public static final String NAMESPACE = "http://skysea.com/protocol/event";
        public static final String ELEMENT_NAME = "x";

        public EventPacketExtension(DataForm argsForm) {
            super(ELEMENT_NAME, NAMESPACE);
            this.element.add(argsForm.getElement());
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
