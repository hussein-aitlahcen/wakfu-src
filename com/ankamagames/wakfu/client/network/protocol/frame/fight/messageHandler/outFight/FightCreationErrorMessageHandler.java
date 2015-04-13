package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fight.creation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

final class FightCreationErrorMessageHandler implements FightMessageHandler<FightCreationErrorMessage, ExternalFightInfo>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final FightCreationErrorMessage msg) {
        final byte errorId = msg.getErrorCode();
        final FightCreationError error = FightCreationError.valueOf(errorId);
        if (error == null) {
            FightCreationErrorMessageHandler.m_logger.error((Object)("ID d'erreur de cr\u00e9ation de combat inconnue : " + errorId));
            return false;
        }
        FightCreationErrorMessageHandler.m_logger.info((Object)("Erreur de creation de combat " + error));
        this.logChatMessage(error);
        this.popupErrorMessage(error);
        return false;
    }
    
    private void popupErrorMessage(final FightCreationError error) {
        String errorMessage = null;
        switch (error) {
            case PROTECTOR_FIGHT_ALREADY_ENDED: {
                errorMessage = WakfuTranslator.getInstance().getString("fight.protector.already.ended");
                break;
            }
            case PROTECTOR_FIGHT_FULL: {
                errorMessage = WakfuTranslator.getInstance().getString("attacked.protector.joinInFight");
                break;
            }
            case PROTECTOR_FIGHT_PLACEMENT_PHASE_ENDED: {
                errorMessage = WakfuTranslator.getInstance().getString("attacked.protector.joinInFight");
                break;
            }
        }
        if (errorMessage != null) {
            this.showPopup(errorMessage);
        }
    }
    
    private void showPopup(final String errorMessage) {
        Xulor.getInstance().msgBox(errorMessage, WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 3L, 102, 1);
    }
    
    private void logChatMessage(final FightCreationError error) {
        String notificationMsg = null;
        switch (error) {
            case IMPOSSIBLE_TO_CREATE_BATTLEGROUND: {
                notificationMsg = WakfuTranslator.getInstance().getString("fight.impossible.battleground.creation");
                break;
            }
            case INSTANCE_DOES_NOT_ALLOW_PVP:
            case INSTANCE_DOES_NOT_ALLOW_DUEL:
            case INSTANCE_DOES_NOT_ALLOW_PVE: {
                notificationMsg = WakfuTranslator.getInstance().getString("systemNotification.instanceConfig.cannotFight");
                break;
            }
            case ZONE_IS_UNDER_MODERATION: {
                notificationMsg = WakfuTranslator.getInstance().getString("systemNotification.moderatedZone.cannotFight");
                break;
            }
            case NO_PATH_BETWEN_FIGHTERS: {
                notificationMsg = WakfuTranslator.getInstance().getString("fight.creation.no.path.between.fighters");
                break;
            }
            case NPC_GROUP_BUSY:
            case TARGET_IS_BUSY: {
                notificationMsg = WakfuTranslator.getInstance().getString("fight.creation.target.busy");
                break;
            }
            default: {
                notificationMsg = WakfuTranslator.getInstance().getString("fight.creation.error");
                FightCreationErrorMessageHandler.m_logger.info((Object)("Cr\u00e9ation du combat impossible : " + error));
                break;
            }
        }
        if (notificationMsg != null) {
            this.pushChatMessageInGameErrorPipe(notificationMsg);
        }
    }
    
    private void pushChatMessageInGameErrorPipe(final String notifyJoinError) {
        final ChatMessage chatMessage = new ChatMessage(notifyJoinError);
        chatMessage.setPipeDestination(3);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    @Override
    public void setConcernedFight(final ExternalFightInfo fight) {
    }
    
    @Override
    public int getHandledMessageId() {
        return 7998;
    }
    
    @Override
    public void setHandledMessageId(final int messageId) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightCreationErrorMessageHandler.class);
    }
}
