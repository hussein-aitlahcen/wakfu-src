package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class EndFightCreationMessageHandler extends UsingFightMessageHandler<EndFightCreationMessage, Fight>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final EndFightCreationMessage msg) {
        if (FightCreationData.INSTANCE.m_fightCreation) {
            FightCreationData.INSTANCE.m_creationActionSequenceOperations.execute();
            FightCreationData.INSTANCE.m_creationActionSequenceOperations.reset();
            return FightCreationData.INSTANCE.m_fightCreation = false;
        }
        EndFightCreationMessageHandler.m_logger.warn((Object)"On re\u00e7oit une fin de creation de combat mais nous ne sommes pas en phase de creation");
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EndFightCreationMessageHandler.class);
    }
}
