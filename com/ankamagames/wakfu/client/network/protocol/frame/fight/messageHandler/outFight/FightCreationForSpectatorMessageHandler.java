package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.spectator.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightCreationForSpectatorMessageHandler extends UsingFightMessageHandler<FightCreationForSpectatorMessage, ExternalFightInfo>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final FightCreationForSpectatorMessage msg) {
        CreateFightForSpectatorProcedure.createFight(msg);
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightCreationForSpectatorMessageHandler.class);
    }
}
