package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterTackledMessageHandler extends UsingFightMessageHandler<FighterTackledMessage, FightInfo>
{
    @Override
    public boolean onMessage(final FighterTackledMessage msg) {
        final TackleAction action = new TackleAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), msg.getFightId());
        action.setInstigatorId(msg.getTacklerId());
        action.setTargetId(msg.getTackledFighterId());
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), action);
        return false;
    }
}
