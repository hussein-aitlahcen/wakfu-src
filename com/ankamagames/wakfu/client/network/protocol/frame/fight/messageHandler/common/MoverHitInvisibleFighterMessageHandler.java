package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class MoverHitInvisibleFighterMessageHandler extends UsingFightMessageHandler<MoverHitInvisibleFighterMessage, FightInfo>
{
    @Override
    public boolean onMessage(final MoverHitInvisibleFighterMessage msg) {
        final int actionTypeId = msg.getFightActionType().getId();
        final MoverHitInvisibleFighterAction action = new MoverHitInvisibleFighterAction(msg.getId(), actionTypeId, msg.getActionId(), msg.getFightId());
        action.setInstigatorId(msg.getMoverId());
        action.setMoverId(msg.getMoverId());
        action.setHitedInvisibleFighterId(msg.getHitedInvisibleFighter());
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), action);
        return false;
    }
}
