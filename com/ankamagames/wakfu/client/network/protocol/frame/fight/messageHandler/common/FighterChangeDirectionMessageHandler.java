package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterChangeDirectionMessageHandler extends UsingFightMessageHandler<FighterChangeDirectionMessage, FightInfo>
{
    @Override
    public boolean onMessage(final FighterChangeDirectionMessage msg) {
        final int actionTypeId = msg.getFightActionType().getId();
        final ChangeDirectionAction action = new ChangeDirectionAction(msg.getId(), actionTypeId, msg.getActionId(), msg.getFightId(), msg.getDirection());
        action.setInstigatorId(msg.getFighterId());
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), action);
        FightActionGroupManager.getInstance().executePendingGroup(msg.getFightId());
        return false;
    }
}
