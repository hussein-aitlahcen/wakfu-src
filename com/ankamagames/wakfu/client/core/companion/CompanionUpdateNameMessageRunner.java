package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class CompanionUpdateNameMessageRunner implements MessageRunner<CompanionUpdateNameMessage>
{
    private static final Logger m_logger;
    
    @Override
    public boolean run(final CompanionUpdateNameMessage msg) {
        final LocalAccountInformations localPlayer = WakfuGameEntity.getInstance().getLocalAccount();
        final CompanionModel companion = CompanionManager.INSTANCE.getCompanion(localPlayer.getAccountId(), msg.getCompanionId());
        if (companion == null) {
            CompanionUpdateNameMessageRunner.m_logger.error((Object)("Impossible de modifier le nom du compagnon " + msg.getCompanionId()));
            return false;
        }
        final CompanionController companionController = new CompanionController(companion);
        try {
            companionController.setName(msg.getName());
        }
        catch (CompanionException e) {
            CompanionUpdateNameMessageRunner.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 5552;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionUpdateNameMessageRunner.class);
    }
}
