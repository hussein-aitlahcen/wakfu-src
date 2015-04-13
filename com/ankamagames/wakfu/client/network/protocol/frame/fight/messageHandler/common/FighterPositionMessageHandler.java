package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterPositionMessageHandler extends UsingFightMessageHandler<FighterPositionMessage, Fight>
{
    @Override
    public boolean onMessage(final FighterPositionMessage msg) {
        final SetPositionAction action = new SetPositionAction(TimedAction.getNextUid(), FightActionType.SET_POSITION.getId(), 0, msg.getFightId(), msg.getFighterId(), msg.getPosition());
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), action);
        FightActionGroupManager.getInstance().executePendingGroup(msg.getFightId());
        return false;
    }
}
