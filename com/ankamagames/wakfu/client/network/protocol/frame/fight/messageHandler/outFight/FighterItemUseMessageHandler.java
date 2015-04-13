package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.actionsOperations.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterItemUseMessageHandler extends UsingFightMessageHandler<FighterItemUseMessage, ExternalFightInfo>
{
    @Override
    public boolean onMessage(final FighterItemUseMessage msg) {
        final FightInfo fight = FightManager.getInstance().getFightById(msg.getFightId());
        if (fight == null) {
            return false;
        }
        new UseItemInFightOperations(fight, msg, false).execute();
        return false;
    }
}
