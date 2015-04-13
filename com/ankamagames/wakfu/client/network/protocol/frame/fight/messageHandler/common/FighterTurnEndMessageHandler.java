package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterTurnEndMessageHandler extends UsingFightMessageHandler<FighterTurnEndMessage, Fight>
{
    @Override
    public boolean onMessage(final FighterTurnEndMessage msg) {
        final FighterTurnEndAction action = new FighterTurnEndAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), msg.getTimeScoreGain(), ((Fight)this.m_concernedFight).getId());
        action.setInstigatorId(msg.getFighterId());
        action.setAddedRemainingSeconds(msg.getAddedRemainingSeconds());
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        return false;
    }
}
