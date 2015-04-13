package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class EndFightMessageHandler extends UsingFightMessageHandler<EndFightMessage, ExternalFightInfo>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final EndFightMessage msg) {
        final int actionTypeId = msg.getFightActionType().getId();
        final FightInfo fight = FightManager.getInstance().getFightById(msg.getFightId());
        if (fight == null) {
            EndFightMessageHandler.m_logger.warn((Object)"On re\u00e7oit une fin de combat exterieur sur un combat qu'on ne connait pas");
            return false;
        }
        fight.getFightMap().blockFightingGroundInTopology(false, false);
        final ExternalFightEndAction action = new ExternalFightEndAction(msg.getUniqueId(), actionTypeId, msg.getActionId(), msg.getFightId());
        action.addLoosers(msg.getLooserTeamMates());
        action.addWinners(msg.getWinnerTeamMates());
        action.addEscapees(msg.getEscapees());
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), action);
        FightActionGroupManager.getInstance().executePendingGroup(fight);
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EndFightMessageHandler.class);
    }
}
