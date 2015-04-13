package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class CompanionUpdateUnlockedMessageRunner implements MessageRunner<CompanionUpdateUnlockedMessage>
{
    private static final Logger m_logger;
    
    @Override
    public boolean run(final CompanionUpdateUnlockedMessage msg) {
        final LocalAccountInformations localPlayer = WakfuGameEntity.getInstance().getLocalAccount();
        final CompanionModel companion = CompanionManager.INSTANCE.getCompanion(localPlayer.getAccountId(), msg.getCompanionId());
        if (companion == null) {
            CompanionUpdateUnlockedMessageRunner.m_logger.error((Object)("Impossible de modifier le nom du compagnon " + msg.getCompanionId()));
            return false;
        }
        companion.setUnlocked(msg.isUnlocked());
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 5562;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionUpdateUnlockedMessageRunner.class);
    }
}
