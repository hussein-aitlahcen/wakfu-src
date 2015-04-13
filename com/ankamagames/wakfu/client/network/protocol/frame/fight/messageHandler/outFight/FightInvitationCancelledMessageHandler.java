package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightInvitationCancelledMessageHandler extends UsingFightMessageHandler<FightInvitationCancelledMessage, ExternalFightInfo>
{
    @Override
    public boolean onMessage(final FightInvitationCancelledMessage msg) {
        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("<text color=\"").append(ChatConstants.CHAT_GAME_ERROR_COLOR).append("\">");
        switch (msg.getReason()) {
            case 3: {
                stringBuffer.append(WakfuTranslator.getInstance().getString("fight.invitation.cancel.anotherInvitation"));
                break;
            }
            case 7: {
                stringBuffer.append(WakfuTranslator.getInstance().getString("fight.invitation.cancel.fightCreationImpossible"));
                break;
            }
            case 4: {
                stringBuffer.append(WakfuTranslator.getInstance().getString("fight.invitation.cancel.initiatorCancelled"));
                break;
            }
            case 6: {
                stringBuffer.append(WakfuTranslator.getInstance().getString("fight.invitation.cancel.initiatorNotAvailable"));
                break;
            }
            case 5: {
                stringBuffer.append(WakfuTranslator.getInstance().getString("fight.invitation.cancel.noPendingFight"));
                break;
            }
            case 2: {
                stringBuffer.append(WakfuTranslator.getInstance().getString("fight.invitation.cancel.playerNotFound"));
                break;
            }
            case 1: {
                stringBuffer.append(WakfuTranslator.getInstance().getString("fight.invitation.cancel.playerRefused"));
                break;
            }
            case 0: {
                stringBuffer.append(WakfuTranslator.getInstance().getString("fight.invitation.cancel.unknownError"));
                break;
            }
        }
        stringBuffer.append("</text>");
        final ChatMessage chatMessage = new ChatMessage(stringBuffer.toString());
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
        return false;
    }
}
