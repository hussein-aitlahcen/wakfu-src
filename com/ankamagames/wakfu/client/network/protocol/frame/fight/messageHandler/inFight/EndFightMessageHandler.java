package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class EndFightMessageHandler extends UsingFightMessageHandler<EndFightMessage, Fight>
{
    @Override
    public boolean onMessage(final EndFightMessage msg) {
        FightCreationData.INSTANCE.m_fightIsGonnaFinish = true;
        if (!msg.isFlee()) {
            ((Fight)this.m_concernedFight).setLosers(msg.getLooserTeamMates());
            ((Fight)this.m_concernedFight).setWinners(msg.getWinnerTeamMates());
        }
        final FightEndAction action = new FightEndAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), ((Fight)this.m_concernedFight).getId(), msg.isFlee());
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        if (msg.isFlee()) {
            FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        }
        return false;
    }
}
