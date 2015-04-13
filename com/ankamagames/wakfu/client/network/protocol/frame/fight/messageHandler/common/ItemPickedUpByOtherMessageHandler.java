package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class ItemPickedUpByOtherMessageHandler extends UsingFightMessageHandler<ItemPickedUpByOtherMessage, Fight>
{
    @Override
    public boolean onMessage(final ItemPickedUpByOtherMessage msg) {
        final ItemPickedUpByOtherAction action = new ItemPickedUpByOtherAction(TimedAction.getNextUid(), FightActionType.ITEM_PICKED_UP_BY_OTHER.getId(), 0, ((Fight)this.m_concernedFight).getId(), msg);
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        return false;
    }
}
