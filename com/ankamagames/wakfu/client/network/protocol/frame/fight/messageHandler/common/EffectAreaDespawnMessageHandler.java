package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class EffectAreaDespawnMessageHandler extends UsingFightMessageHandler<EffectAreaDespawnMessage, Fight>
{
    @Override
    public boolean onMessage(final EffectAreaDespawnMessage msg) {
        final int actionTypeId = msg.getFightActionType().getId();
        final EffectAreaDestroyedAction action = new EffectAreaDestroyedAction(msg.getUniqueId(), actionTypeId, msg.getActionId(), msg.getFightId());
        action.setInstigatorId(msg.getEffectAreaId());
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), action);
        return false;
    }
}
