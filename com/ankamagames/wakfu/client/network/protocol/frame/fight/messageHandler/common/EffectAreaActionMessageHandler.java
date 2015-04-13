package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.framework.script.libraries.scriptedAction.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class EffectAreaActionMessageHandler extends UsingFightMessageHandler<EffectAreaActionMessage, Fight>
{
    @Override
    public boolean onMessage(final EffectAreaActionMessage msg) {
        final int actionTypeId = msg.getFightActionType().getId();
        final EffectAreaTriggeredAction action = new EffectAreaTriggeredAction(msg.getUniqueId(), actionTypeId, msg.getActionId(), msg.getFightId(), msg.isApply(), msg.getAreaBaseId());
        action.setInstigatorId(msg.getAreaId());
        action.setTargetId(msg.getTargetId());
        final ActionGroup group = FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        action.addJavaFunctionsLibrary(new ScriptedActionFunctionsLibrary(group));
        return false;
    }
}
