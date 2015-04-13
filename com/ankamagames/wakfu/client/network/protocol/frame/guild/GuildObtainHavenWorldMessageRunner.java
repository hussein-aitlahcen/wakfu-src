package com.ankamagames.wakfu.client.network.protocol.frame.guild;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

final class GuildObtainHavenWorldMessageRunner implements MessageRunner<GuildObtainHavenWorldMessage>
{
    private static final Logger m_logger;
    
    @Override
    public boolean run(final GuildObtainHavenWorldMessage msg) {
        final short havenWorldInstanceId = msg.getHavenWorldInstanceId();
        final String havenWorldName = WakfuTranslator.getInstance().getString(77, havenWorldInstanceId, new Object[0]);
        final String titleKey = "notification.havenWorldObtainedTitle";
        final String notifText = WakfuTranslator.getInstance().getString("notification.havenWorldObtainedText", havenWorldName);
        final String title = WakfuTranslator.getInstance().getString("notification.havenWorldObtainedTitle");
        final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
        final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
        Worker.getInstance().pushMessage(uiNotificationMessage);
        final String mess = WakfuTranslator.getInstance().getString("havenWorld.chatWorldObtained", havenWorldName);
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.pipeName.guild"), mess);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 20099;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildObtainHavenWorldMessageRunner.class);
    }
}
