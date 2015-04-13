package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightChangeTimePointGapMessageHandler extends UsingFightMessageHandler<FightChangeTimePointGapMessage, Fight>
{
    private static Logger m_logger;
    
    @Override
    public boolean onMessage(final FightChangeTimePointGapMessage msg) {
        if (((Fight)this.m_concernedFight).getId() != msg.getFightId()) {
            FightChangeTimePointGapMessageHandler.m_logger.error((Object)"Le message ne concerne pas notre combat");
            return false;
        }
        ((Fight)this.m_concernedFight).forceTimePointGap(msg.getNewGap());
        return false;
    }
    
    static {
        FightChangeTimePointGapMessageHandler.m_logger = Logger.getLogger((Class)FightChangeTimePointGapMessageHandler.class);
    }
}
