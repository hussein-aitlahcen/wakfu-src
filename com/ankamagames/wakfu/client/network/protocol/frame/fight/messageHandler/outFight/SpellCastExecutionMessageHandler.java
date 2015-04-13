package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.actionsOperations.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class SpellCastExecutionMessageHandler extends UsingFightMessageHandler<SpellCastExecutionMessage, ExternalFightInfo>
{
    @Override
    public boolean onMessage(final SpellCastExecutionMessage msg) {
        final FightInfo fight = FightManager.getInstance().getFightById(msg.getFightId());
        new SpellCastOperations(fight, msg).execute();
        return false;
    }
}
