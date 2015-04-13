package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class PointEffectSelectedNotificationMessageHandler extends UsingFightMessageHandler<PointEffectSelectedNotificationMessage, Fight>
{
    @Override
    public boolean onMessage(final PointEffectSelectedNotificationMessage msg) {
        final PointEffectSelectedAction action = new PointEffectSelectedAction(TimedAction.getNextUid(), 0, FightActionType.POINT_EFFECT_SELECTED_NOTIFICATION.getId(), (Fight)this.m_concernedFight, msg.getChooserId(), msg.getEffectId());
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        return false;
    }
}
