package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightJoinFailureMessageHandler extends UsingFightMessageHandler<FightJoinFailureMessage, FightInfo>
{
    @Override
    public boolean onMessage(final FightJoinFailureMessage msg) {
        ErrorsMessageTranslator.getInstance().pushMessage(msg.getReason(), 3, new Object[0]);
        return false;
    }
}
