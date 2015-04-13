package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightPlacementEndMessageHandler extends UsingFightMessageHandler<FightPlacementEndMessage, Fight>
{
    @Override
    public boolean onMessage(final FightPlacementEndMessage msg) {
        final EndPlacementAction endPlacementAction = EndPlacementAction.checkout(TimedAction.getNextUid(), FightActionType.END_PLACEMENT.getId(), 0, this.m_concernedFight);
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, endPlacementAction);
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        return false;
    }
}
