package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightReportMessageHandler extends UsingFightMessageHandler<FightReportMessage, Fight>
{
    @Override
    public boolean onMessage(final FightReportMessage msg) {
        if (((Fight)this.m_concernedFight).getModel().displayFightResult()) {
            final int actionTypeId = msg.getFightActionType().getId();
            final Action action = new FightReportAction(msg.getUniqueId(), actionTypeId, 0, ((Fight)this.m_concernedFight).getId(), msg.getHistory(), ((Fight)this.m_concernedFight).getPlayerXpModifications(), ((Fight)this.m_concernedFight).getModel());
            FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        }
        else {
            FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, new FightCleanUpAction(msg.getUniqueId(), 0, ((Fight)this.m_concernedFight).getId()));
        }
        return false;
    }
}
