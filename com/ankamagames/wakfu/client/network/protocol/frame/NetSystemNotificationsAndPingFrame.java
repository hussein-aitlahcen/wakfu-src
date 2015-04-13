package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.systemMessage.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.*;

public class NetSystemNotificationsAndPingFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static NetSystemNotificationsAndPingFrame m_instance;
    private final Map<Byte, Long> m_lastPingTimeByServer;
    private final Map<Byte, Double> m_lastPingRttByServer;
    private final Map<Byte, Double> m_lastPingProcessingByServer;
    private final Map<Byte, Integer> m_lastPingKeyByServer;
    
    public static NetSystemNotificationsAndPingFrame getInstance() {
        return NetSystemNotificationsAndPingFrame.m_instance;
    }
    
    private NetSystemNotificationsAndPingFrame() {
        super();
        this.m_lastPingTimeByServer = new HashMap<Byte, Long>();
        this.m_lastPingRttByServer = new HashMap<Byte, Double>();
        this.m_lastPingProcessingByServer = new HashMap<Byte, Double>();
        this.m_lastPingKeyByServer = new HashMap<Byte, Integer>();
    }
    
    public long getLastPingTime(final byte server) {
        return this.m_lastPingTimeByServer.containsKey(server) ? this.m_lastPingTimeByServer.get(server) : 0L;
    }
    
    public int getLastPingKey(final byte server) {
        return this.m_lastPingKeyByServer.containsKey(server) ? this.m_lastPingKeyByServer.get(server) : 0;
    }
    
    public double getLastPingRtt(final byte server) {
        return this.m_lastPingRttByServer.containsKey(server) ? this.m_lastPingRttByServer.get(server) : 0.0;
    }
    
    public double getLastPingProcessing(final byte server) {
        return this.m_lastPingProcessingByServer.containsKey(server) ? this.m_lastPingProcessingByServer.get(server) : 0.0;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 106: {
                final SystemNotificationMessage msg = (SystemNotificationMessage)message;
                switch (msg.getNotificationType()) {
                    case 1: {
                        final int shutdownDelay = msg.extractIntParameter();
                        final int hours = shutdownDelay / 60;
                        final int minutes = shutdownDelay % 60;
                        final String userMsg = WakfuTranslator.getInstance().getString("systemNotification.shutdown.time", hours, minutes);
                        final ChatMessage chatMsg = new ChatMessage(userMsg);
                        chatMsg.setPipeDestination(3);
                        ChatManager.getInstance().getChatPipe(3).pushMessage(chatMsg);
                        WakfuSystemMessageManager.getInstance().showMessage(new SystemMessageData(WakfuSystemMessageManager.SystemMessageType.SYS_INFO, userMsg, 5000));
                        break;
                    }
                    case 2: {
                        final String userMsg2 = WakfuTranslator.getInstance().getString("systemNotification.shutdown.canceled");
                        final ChatMessage chatMsg2 = new ChatMessage(userMsg2);
                        chatMsg2.setPipeDestination(3);
                        ChatManager.getInstance().getChatPipe(3).pushMessage(chatMsg2);
                        WakfuSystemMessageManager.getInstance().showMessage(new SystemMessageData(WakfuSystemMessageManager.SystemMessageType.SYS_INFO, userMsg2, 5000));
                        break;
                    }
                    case 3: {
                        final String notificationMsg = msg.extractStringParameter();
                        final ChatMessage chatMsg2 = new ChatMessage(notificationMsg);
                        chatMsg2.setPipeDestination(3);
                        ChatManager.getInstance().getChatPipe(3).pushMessage(chatMsg2);
                        WakfuSystemMessageManager.getInstance().showMessage(new SystemMessageData(WakfuSystemMessageManager.SystemMessageType.ADMIN_INFO, notificationMsg, this.getDurationForString(notificationMsg)));
                        break;
                    }
                    case 11: {
                        final String talkingCharacter = msg.extractStringParameter();
                        final String notificationMsg2 = msg.extractStringParameter();
                        final ChatMessage chatMsg3 = new ChatMessage(talkingCharacter, notificationMsg2);
                        chatMsg3.setPipeDestination(1);
                        WakfuWordsModerator.controlChatMessage(chatMsg3);
                        if (chatMsg3.getMessage().isEmpty()) {
                            return false;
                        }
                        ChatManager.getInstance().getChatPipe(1).pushMessage(chatMsg3);
                        break;
                    }
                    case 4: {
                        final String notificationMsg = msg.extractStringParameter();
                        final ChatMessage chatMsg2 = new ChatMessage(notificationMsg);
                        chatMsg2.setPipeDestination(4);
                        ChatManager.getInstance().getChatPipe(4).pushMessage(chatMsg2);
                        break;
                    }
                    case 5: {
                        final String notificationMsg = WakfuTranslator.getInstance().getString("systemNotification.moderatedZone.start");
                        final ChatMessage chatMsg2 = new ChatMessage(notificationMsg);
                        chatMsg2.setPipeDestination(4);
                        ChatManager.getInstance().getChatPipe(4).pushMessage(chatMsg2);
                        break;
                    }
                    case 6: {
                        final String notificationMsg = WakfuTranslator.getInstance().getString("systemNotification.moderatedZone.stop");
                        final ChatMessage chatMsg2 = new ChatMessage(notificationMsg);
                        chatMsg2.setPipeDestination(4);
                        ChatManager.getInstance().getChatPipe(4).pushMessage(chatMsg2);
                        break;
                    }
                    case 7: {
                        final String notificationMsg = WakfuTranslator.getInstance().getString("systemNotification.moderatedZone.enter");
                        final ChatMessage chatMsg2 = new ChatMessage(notificationMsg);
                        chatMsg2.setPipeDestination(4);
                        ChatManager.getInstance().getChatPipe(4).pushMessage(chatMsg2);
                        break;
                    }
                    case 8: {
                        final String notificationMsg = WakfuTranslator.getInstance().getString("systemNotification.moderatedZone.leave");
                        final ChatMessage chatMsg2 = new ChatMessage(notificationMsg);
                        chatMsg2.setPipeDestination(4);
                        ChatManager.getInstance().getChatPipe(4).pushMessage(chatMsg2);
                        break;
                    }
                    case 9: {
                        final String notificationMsg = WakfuTranslator.getInstance().getString("systemNotification.moderatedZone.cannotChat");
                        final ChatMessage chatMsg2 = new ChatMessage(notificationMsg);
                        chatMsg2.setPipeDestination(3);
                        ChatManager.getInstance().getChatPipe(3).pushMessage(chatMsg2);
                        break;
                    }
                    case 10: {
                        final String notificationMsg = WakfuTranslator.getInstance().getString("systemNotification.cannotChatWhenMuted");
                        final ChatMessage chatMsg2 = new ChatMessage(notificationMsg);
                        chatMsg2.setPipeDestination(3);
                        ChatManager.getInstance().getChatPipe(3).pushMessage(chatMsg2);
                        break;
                    }
                    default: {
                        NetSystemNotificationsAndPingFrame.m_logger.error((Object)("Notification syst\u00e8me inconnue : type=" + msg.getNotificationType()));
                        break;
                    }
                }
                return false;
            }
            case 108: {
                final PongMessage pong = (PongMessage)message;
                final double roundTripTime = (pong.getPongReceivedDate() - pong.getPingSentDate()) / 1000000.0;
                final double serverProcessingTime = (pong.getPongSentDate() - pong.getPingReceivedDate()) / 1000000.0;
                if (pong.getKey() == 0 && pong.getServerId() == 0) {
                    final String userMsg3 = WakfuTranslator.getInstance().getString("chat.ping", Math.round(roundTripTime));
                    final ChatMessage chatMsg4 = new ChatMessage(userMsg3);
                    chatMsg4.setPipeDestination(4);
                    ChatManager.getInstance().getChatPipe(4).pushMessage(chatMsg4);
                }
                NetSystemNotificationsAndPingFrame.m_logger.info((Object)("Pong! serverGroup=" + pong.getServerId() + " rtt=" + roundTripTime + " processing=" + serverProcessingTime + " key=" + pong.getKey()));
                this.m_lastPingTimeByServer.put(pong.getServerId(), System.currentTimeMillis());
                this.m_lastPingKeyByServer.put(pong.getServerId(), pong.getKey());
                this.m_lastPingRttByServer.put(pong.getServerId(), roundTripTime);
                this.m_lastPingProcessingByServer.put(pong.getServerId(), serverProcessingTime);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    private int getDurationForString(final String text) {
        return 500 + text.length() * 100;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetSystemNotificationsAndPingFrame.class);
        NetSystemNotificationsAndPingFrame.m_instance = new NetSystemNotificationsAndPingFrame();
    }
}
