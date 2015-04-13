package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightPlacementStartMessageHandler extends UsingFightMessageHandler<FightPlacementStartMessage, Fight>
{
    @Override
    public boolean onMessage(final FightPlacementStartMessage msg) {
        FightCreationData.INSTANCE.m_creationActionSequenceOperations.setDisplayFightMessageInterface2(DisplayFightMessageInterface.checkout(TimedAction.getNextUid(), FightActionType.DISPLAY_FIGHT_INTERFACE_MESSAGE.getId(), 0, "fight.placement"));
        FightCreationData.INSTANCE.m_creationActionSequenceOperations.setStartPlacementAction(StartPlacementAction.checkout(TimedAction.getNextUid(), FightActionType.START_PLACEMENT.getId(), 0, this.m_concernedFight, msg.getRemainingTime(), System.currentTimeMillis()));
        return false;
    }
}
