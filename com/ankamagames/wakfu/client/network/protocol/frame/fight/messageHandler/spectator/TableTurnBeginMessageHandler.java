package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.spectator;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class TableTurnBeginMessageHandler extends UsingFightMessageHandler<TableTurnBeginMessage, Fight>
{
    @Override
    public boolean onMessage(final TableTurnBeginMessage msg) {
        final NewTableTurnAction action = new NewTableTurnAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), ((Fight)this.m_concernedFight).getId(), msg.getShortTimelineSerialize());
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        return false;
    }
}
