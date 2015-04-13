package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.*;

public final class CommonHandlersFactory
{
    private static Logger m_logger;
    private static final FightMessageHandler RETURN_TRUE_MESSAGE_HANDLER;
    
    public static <M extends Message, Fi extends FightInfo> FightMessageHandler<M, Fi> returnTrueHandler() {
        return (FightMessageHandler<M, Fi>)CommonHandlersFactory.RETURN_TRUE_MESSAGE_HANDLER;
    }
    
    public static <M extends Message, Fi extends FightInfo> void createHandlerForThisFrame(final int msgType, final FightManagementFrame<Fi> frame) {
        FightMessageHandler handler = null;
        switch (msgType) {
            case 4122: {
                handler = new ApplyStateMessageHandler();
                break;
            }
            case 6200: {
                handler = new EffectAreaActionMessageHandler();
                break;
            }
            case 6204: {
                handler = new EffectAreaDespawnMessageHandler();
                break;
            }
            case 8014: {
                handler = new FightChangeTimePointGapMessageHandler();
                break;
            }
            case 4520: {
                handler = new FighterActivityChangeMessageHandler();
                break;
            }
            case 4522: {
                handler = new FighterChangeDirectionMessageHandler();
                break;
            }
            case 8412: {
                handler = new MoverHitInvisibleFighterMessageHandler();
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
            case 4524: {
                handler = new FighterMoveMessageHandler();
                break;
            }
            case 4528: {
                handler = new FighterPositionMessageHandler();
                break;
            }
            case 8410: {
                handler = new FighterSpeakMessageHandler();
                break;
            }
            case 4506: {
                handler = new FighterTackledMessageHandler();
                break;
            }
            case 8106: {
                handler = new FighterTurnEndMessageHandler();
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
            case 8010: {
                handler = new FightTimelineRecoveryMessageHandler();
                break;
            }
            case 8304: {
                handler = new FightReportMessageHandler();
                break;
            }
            case 4300: {
                handler = new InteractiveElementUpdateActionMessageHandler();
                break;
            }
            case 202: {
                handler = new InteractiveElementUpdateMessageHandler();
                break;
            }
            case 8033: {
                handler = new ItemPickedUpByOtherMessageHandler();
                break;
            }
            case 8034: {
                handler = new ItemPickedUpInFightMessageHandler();
                break;
            }
            case 8120: {
                handler = new RunningEffectActionMessageHandler();
                break;
            }
            case 8124: {
                handler = new RunningEffectApplicationMessageHandler();
                break;
            }
            case 8122: {
                handler = new RunningEffectUnapplicationMessageHandler();
                break;
            }
            case 8110: {
                handler = new SpellCastExecutionMessageHandler();
                break;
            }
            case 8116: {
                handler = new SpellCastNotificationMessageHandler();
                break;
            }
            case 4123: {
                handler = new UnapplyStateMessageHandler();
                break;
            }
            case 8200: {
                handler = new FightActionSequenceExecuteMessageHandler();
                break;
            }
            default: {
                CommonHandlersFactory.m_logger.warn((Object)("ATTENTION : l'id de message pass\u00e9 en parametre n'est pas g\u00e9r\u00e9 par la factory : " + msgType));
                handler = CommonHandlersFactory.RETURN_TRUE_MESSAGE_HANDLER;
                break;
            }
        }
        handler.setHandledMessageId(msgType);
        frame.addHandler(handler);
    }
    
    static {
        CommonHandlersFactory.m_logger = Logger.getLogger((Class)CommonHandlersFactory.class);
        RETURN_TRUE_MESSAGE_HANDLER = new ReturnTrueMessageHandler();
    }
}
