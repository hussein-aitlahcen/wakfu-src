package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class StartActionMessageHandler extends UsingFightMessageHandler<StartActionMessage, Fight>
{
    @Override
    public boolean onMessage(final StartActionMessage msg) {
        final StartActionAction action = StartActionAction.checkout(TimedAction.getNextUid(), FightActionType.START_ACTION.getId(), 0, this.m_concernedFight);
        if (((Fight)this.m_concernedFight).isNeedPlacementStep()) {
            action.setNeedSetTimePointGap(true);
            action.setTimePointGap(msg.getTimePointGap());
            FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
            FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        }
        else {
            action.setNeedSetTimePointGap(false);
            FightCreationData.INSTANCE.m_creationActionSequenceOperations.setStartActionAction(action);
        }
        WakfuSoundManager.getInstance().enterAction();
        return false;
    }
}
