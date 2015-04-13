package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightTimelineRecoveryMessageHandler extends UsingFightMessageHandler<FightTimelineRecoveryMessage, Fight>
{
    @Override
    public boolean onMessage(final FightTimelineRecoveryMessage msg) {
        FightActionGroupManager.getInstance().removePendingGroups(((Fight)this.m_concernedFight).getId());
        ((Fight)this.m_concernedFight).recoverFromDatas(msg.getSerializedTimeline());
        return false;
    }
}
