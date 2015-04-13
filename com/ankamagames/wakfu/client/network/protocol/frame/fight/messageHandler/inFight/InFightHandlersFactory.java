package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;

public final class InFightHandlersFactory
{
    private static Logger m_logger;
    
    public static <M extends Message, Fi extends FightInfo> void createHandlerForThisFrame(final int msgType, final FightManagementFrame<Fi> frame) {
        FightMessageHandler handler = null;
        switch (msgType) {
            case 4127: {
                handler = new ActorMoveToMessageHandler();
                break;
            }
            case 4126: {
                handler = new ActorTeleportMessageHandler();
                break;
            }
            case 8156: {
                handler = new CellReportMessageHandler();
                break;
            }
            case 4124: {
                handler = new CharacterHealthUpdateMessageHandler();
                break;
            }
            case 8040: {
                handler = new DebugFightAccessSquareMessageHandler();
                break;
            }
            case 8300: {
                handler = new EndFightMessageHandler();
                break;
            }
            case 8012: {
                handler = new FightChangeParametersMessageHandler();
                break;
            }
            case 8000: {
                handler = new FightCreationMessageHandler();
                break;
            }
            case 4508: {
                handler = new FighterInvisibleDetectedMessageHandler();
                break;
            }
            case 8114: {
                handler = new FighterPublicCharacteristicChangedMessageHandler();
                break;
            }
            case 8150: {
                handler = new FighterReadyMessageHandler();
                break;
            }
            case 8026: {
                handler = new FightPlacementStartMessageHandler();
                break;
            }
            case 8158: {
                handler = new LockFightMessageHandler();
                break;
            }
            case 4214: {
                handler = new PlayerXpModificationMessageHandler();
                break;
            }
            case 8308: {
                handler = new PointEffectSelectedNotificationMessageHandler();
                break;
            }
            case 8310: {
                handler = new PointEffectSelectionActivationMessageHandler();
                break;
            }
            case 4170: {
                handler = new SimpleOccupationModificationMessageHandler();
                break;
            }
            case 8030: {
                handler = new StartActionMessageHandler();
                break;
            }
            case 5240: {
                handler = new UpdateWalletMessageHandler();
                break;
            }
            case 8100: {
                handler = new TableTurnBeginMessageHandler();
                break;
            }
            case 8104: {
                handler = new FighterTurnBeginMessageHandler();
                break;
            }
            case 8202: {
                handler = new EndFightCreationMessageHandler();
                break;
            }
            case 8016: {
                handler = new AwaitingFightersNotificationMessageHandler();
                break;
            }
            case 8042: {
                handler = new ReconnectionInFightMessageHandler();
                break;
            }
            case 8043: {
                handler = new CharacterEffectManagerForReconnectionMessageHandler();
                break;
            }
            case 8044: {
                handler = new CharacterDataForReconnectionMessageHandler();
                break;
            }
            case 8046: {
                handler = new CharacterPublicCharacteristicsDataMessageHandler();
                break;
            }
            case 8045: {
                handler = new SpellCastHistoryMessageHandler();
                break;
            }
            case 8415: {
                handler = new PlayerChangeConnectionStateMessageHandler();
                break;
            }
            default: {
                InFightHandlersFactory.m_logger.warn((Object)("ATTENTION : l'id de message pass\u00e9 en parametre n'est pas g\u00e9r\u00e9 par la factory : " + msgType));
                handler = CommonHandlersFactory.returnTrueHandler();
                break;
            }
        }
        handler.setHandledMessageId(msgType);
        frame.addHandler(handler);
    }
    
    static {
        InFightHandlersFactory.m_logger = Logger.getLogger((Class)InFightHandlersFactory.class);
    }
}
