package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class InteractiveElementUpdateActionMessageHandler extends UsingFightMessageHandler<InteractiveElementUpdateActionMessage, ExternalFightInfo>
{
    @Override
    public boolean onMessage(final InteractiveElementUpdateActionMessage msg) {
        if (msg.getActionContextType() == 1) {
            final InteractiveElementUpdateAction action = InteractiveElementUpdateAction.checkout(TimedAction.getNextUid(), FightActionType.INTERACTIVE_ELEMENT_UPDATE.getId(), 0, msg.getElementId(), msg.getSharedDatas());
            final FightInfo fight = FightManager.getInstance().getFightById((int)msg.getActionContextUniqueId());
            if (fight != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fight.getId(), action);
            }
        }
        return false;
    }
}
