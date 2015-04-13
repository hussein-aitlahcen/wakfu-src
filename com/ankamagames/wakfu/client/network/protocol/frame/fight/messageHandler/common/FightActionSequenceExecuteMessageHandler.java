package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightActionSequenceExecuteMessageHandler extends UsingFightMessageHandler<FightActionSequenceExecuteMessage, Fight>
{
    @Override
    public boolean onMessage(final FightActionSequenceExecuteMessage msg) {
        FightActionGroupManager.getInstance().executePendingGroup(msg.getFightId());
        return false;
    }
}
