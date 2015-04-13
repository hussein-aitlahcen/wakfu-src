package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class AwaitingFightersNotificationMessageHandler extends UsingFightMessageHandler<AwaitingFightersNotificationMessage, Fight>
{
    @Override
    public boolean onMessage(final AwaitingFightersNotificationMessage msg) {
        final AwaitingFightersNotificationAction action = new AwaitingFightersNotificationAction(TimedAction.getNextUid(), FightActionType.AWAITED_FIGHTERS_NOTIFICATION.getId(), 0, ((Fight)this.m_concernedFight).getId());
        action.setAwaitedFighters(msg.getAwaitedFighters());
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        return false;
    }
}
