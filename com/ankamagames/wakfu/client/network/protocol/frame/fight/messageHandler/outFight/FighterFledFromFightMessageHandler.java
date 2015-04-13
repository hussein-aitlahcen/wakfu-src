package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterFledFromFightMessageHandler extends UsingFightMessageHandler<FighterFledFromFightMessage, ExternalFightInfo>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final FighterFledFromFightMessage msg) {
        final FightInfo fight = FightManager.getInstance().getFightById(msg.getFightId());
        if (!(fight instanceof ExternalFightInfo)) {
            FighterFledFromFightMessageHandler.m_logger.error((Object)String.format("Un message %s concernant le %s id=%d a \u00e9t\u00e9 adress\u00e9 \u00e0 la %s", msg.getClass().getSimpleName(), fight.getClass().getSimpleName(), fight.getId(), this.getClass().getSimpleName()));
            return false;
        }
        final FighterFledFromOutFightAction action = new FighterFledFromOutFightAction(TimedAction.getNextUid(), msg.getFightActionType().getId(), msg.getActionId(), msg.getFightId());
        action.setFighterId(msg.getFighterId());
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), action);
        FightActionGroupManager.getInstance().executePendingGroup(msg.getFightId());
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FighterFledFromFightMessageHandler.class);
    }
}
