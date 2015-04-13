package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;
import com.ankamagames.wakfu.client.core.game.fight.actionsOperations.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightPlacementStartMessageHandler extends UsingFightMessageHandler<FightPlacementStartMessage, ExternalFightInfo>
{
    @Override
    public boolean onMessage(final FightPlacementStartMessage msg) {
        final FightInfo fightInfo = FightManager.getInstance().getFightById(msg.getFightId());
        ExternalFightCreationActions.INSTANCE.m_startPlacementAction.put(msg.getFightId(), StartPlacementAction.checkout(TimedAction.getNextUid(), FightActionType.START_PLACEMENT.getId(), 0, fightInfo, 0, 0L));
        new ExternalFightPlacementStartOperations(msg).execute();
        return false;
    }
}
