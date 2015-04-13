package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;

public final class OutFightHandlersFactory
{
    private static final Logger m_logger;
    
    public static <M extends Message, Fi extends FightInfo> void createHandlerForThisFrame(final int msgType, final FightManagementFrame<Fi> frame) {
        FightMessageHandler handler = null;
        switch (msgType) {
            case 4170: {
                handler = new SimpleOccupationModificationMessageHandler();
                break;
            }
            case 4114: {
                handler = new ActorPathUpdateMessageHandler();
                break;
            }
            case 4126: {
                handler = new ActorTeleportMessageHandler();
                break;
            }
            case 4122: {
                handler = new ApplyStateMessageHandler();
                break;
            }
            case 8154: {
                handler = new CallHelpMessageHandler();
                break;
            }
            case 8202: {
                handler = new EndFightCreationMessageHandler();
                break;
            }
            case 8300: {
                handler = new EndFightMessageHandler();
                break;
            }
            case 8006: {
                handler = new ExternalFightCreationMessageHandler();
                break;
            }
            case 8000: {
                handler = new FightCreationMessageHandler();
                break;
            }
            case 8038: {
                handler = new FightCreationForSpectatorMessageHandler();
                break;
            }
            case 8302: {
                handler = new FighterFledFromFightMessageHandler();
                break;
            }
            case 8108: {
                handler = new FighterItemUseMessageHandler();
                break;
            }
            case 8150: {
                handler = new FighterReadyMessageHandler();
                break;
            }
            case 7906: {
                handler = new FightersPlacementPositionMessageHandler();
                break;
            }
            case 7904: {
                handler = new FightInvitationCancelledMessageHandler();
                break;
            }
            case 7902: {
                handler = new FightInvitationMessageHandler();
                break;
            }
            case 8004: {
                handler = new FightJoinFailureMessageHandler();
                break;
            }
            case 8002: {
                handler = new FightJoinMessageHandler();
                break;
            }
            case 8028: {
                handler = new FightPlacementEndMessageHandler();
                break;
            }
            case 8026: {
                handler = new FightPlacementStartMessageHandler();
                break;
            }
            case 4300: {
                handler = new InteractiveElementUpdateActionMessageHandler();
                break;
            }
            case 8110: {
                handler = new SpellCastExecutionMessageHandler();
                break;
            }
            case 7998: {
                handler = new FightCreationErrorMessageHandler();
                break;
            }
            default: {
                OutFightHandlersFactory.m_logger.warn((Object)("ATTENTION : l'id de message pass\u00e9 en parametre n'est pas g\u00e9r\u00e9 par la factory : " + msgType));
                handler = CommonHandlersFactory.returnTrueHandler();
                break;
            }
        }
        handler.setHandledMessageId(msgType);
        frame.addHandler(handler);
    }
    
    static {
        m_logger = Logger.getLogger((Class)OutFightHandlersFactory.class);
    }
}
