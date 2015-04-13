package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterActivityChangeMessageHandler extends UsingFightMessageHandler<FighterActivityChangeMessage, FightInfo>
{
    @Override
    public boolean onMessage(final FighterActivityChangeMessage msg) {
        final ChangeActivityAction action = new ChangeActivityAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), msg.getActivity(), msg.getFightId(), true);
        action.setTargetId(msg.getFighterId());
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), action);
        return false;
    }
}
