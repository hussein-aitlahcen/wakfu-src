package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.spectator;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class EndFightMessageHandler extends UsingFightMessageHandler<EndFightMessage, Fight>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final EndFightMessage msg) {
        final int fightId = msg.getFightId();
        if (this.m_concernedFight != null && ((Fight)this.m_concernedFight).getId() == fightId) {
            final FightEndAction action = new FightEndAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), ((Fight)this.m_concernedFight).getId(), false);
            action.setIsForSpectator(true);
            ((Fight)this.m_concernedFight).setWinners(msg.getWinnerTeamMates());
            ((Fight)this.m_concernedFight).setLosers(msg.getLooserTeamMates());
            FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
            FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
            return false;
        }
        EndFightMessageHandler.m_logger.error((Object)"Pas de combat associ\u00e9 a la frame pour sortir du mode spectateur");
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EndFightMessageHandler.class);
    }
}
