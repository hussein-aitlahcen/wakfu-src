package com.ankamagames.wakfu.client.network.protocol.frame.guild;

import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

class GuildBonusLearnedMessageRunner implements MessageRunner<GuildBonusLearnedMessage>
{
    @Override
    public boolean run(final GuildBonusLearnedMessage msg) {
        final int bonusId = msg.getBonusId();
        final String notifTitleTranslatorKey = "notification.guildBonusLearnedTitle";
        final String bonusName = WakfuTranslator.getInstance().getString(138, bonusId, new Object[0]);
        final String notifText = WakfuTranslator.getInstance().getString("notification.guildBonusLearnedText", bonusName);
        final String title = WakfuTranslator.getInstance().getString("notification.guildBonusLearnedTitle");
        final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
        final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
        Worker.getInstance().pushMessage(uiNotificationMessage);
        final String mess = WakfuTranslator.getInstance().getString("guild.chatBonusActivated", bonusName);
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.pipeName.guild"), mess);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 20072;
    }
}
