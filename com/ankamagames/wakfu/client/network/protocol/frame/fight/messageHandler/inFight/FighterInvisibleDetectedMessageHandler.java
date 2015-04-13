package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterInvisibleDetectedMessageHandler extends UsingFightMessageHandler<FighterInvisibleDetectedMessage, Fight>
{
    @Override
    public boolean onMessage(final FighterInvisibleDetectedMessage msg) {
        final InvisibleDetectedAction action = new InvisibleDetectedAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), ((Fight)this.m_concernedFight).getId(), msg.getDetectedByFightersId(), msg.getDetectedPosition());
        action.setTargetId(msg.getDetectedFighterId());
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        return false;
    }
}
