package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.serverToClient.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class InteractiveElementUpdateActionMessageHandler extends UsingFightMessageHandler<InteractiveElementUpdateMessage, Fight>
{
    @Override
    public boolean onMessage(final InteractiveElementUpdateMessage msg) {
        final InteractiveElementUpdateAction action = InteractiveElementUpdateAction.checkout(TimedAction.getNextUid(), FightActionType.INTERACTIVE_ELEMENT_UPDATE.getId(), 0, msg.getElementId(), msg.getSharedDatas());
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        return false;
    }
}
