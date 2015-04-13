package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightChangeParametersMessageHandler extends UsingFightMessageHandler<FightChangeParametersMessage, Fight>
{
    @Override
    public boolean onMessage(final FightChangeParametersMessage msg) {
        if (msg.mustChangeTurnDuration()) {
            ((Fight)this.m_concernedFight).setTurnDurationInMilliSecond(msg.getTurnDuration());
        }
        return false;
    }
}
