package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class SpellCastNotificationMessageHandler extends UsingFightMessageHandler<SpellCastNotificationMessage, Fight>
{
    @Override
    public boolean onMessage(final SpellCastNotificationMessage msg) {
        final SpellCastNotificationAction action = new SpellCastNotificationAction(TimedAction.getNextUid(), msg.getFightActionType().getId(), msg.getActionId(), msg.getFightId());
        action.setInstigatorId(msg.getCasterId());
        action.setCasterId(msg.getCasterId());
        action.setSpellRefId(msg.getSpellRefId());
        action.setCriticalHit(msg.isCriticalHit());
        action.setCriticalMiss(msg.isCriticalMiss());
        action.setRawSpellLevel(msg.getRawSpell());
        action.setX(msg.getX());
        action.setY(msg.getY());
        action.setZ(msg.getZ());
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), action);
        return false;
    }
}
