package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class SimpleOccupationModificationMessageHandler extends UsingFightMessageHandler<SimpleOccupationModificationMessage, Fight>
{
    @Override
    public boolean onMessage(final SimpleOccupationModificationMessage msg) {
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, new SimpleOccupationModificationAction(msg));
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        return false;
    }
}
