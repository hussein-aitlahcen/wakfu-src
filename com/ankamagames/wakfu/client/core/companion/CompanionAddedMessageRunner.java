package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class CompanionAddedMessageRunner implements MessageRunner<CompanionAddedMessage>
{
    private static final Logger m_logger;
    
    @Override
    public boolean run(final CompanionAddedMessage msg) {
        final CompanionModel companion = msg.getCompanion();
        final LocalAccountInformations localPlayer = WakfuGameEntity.getInstance().getLocalAccount();
        final CompanionManagerController companionManagerController = new CompanionManagerController(localPlayer.getAccountId());
        try {
            companionManagerController.addCompanion(companion);
            if (WakfuGameEntity.getInstance().hasFrame(UICompanionsManagementFrame.INSTANCE)) {
                UICompanionsManagementFrame.INSTANCE.loadCompanionsList();
                UICompanionsManagementFrame.INSTANCE.reflowHeroesList();
                UICompanionsManagementFrame.INSTANCE.reflowCompanionsList();
            }
        }
        catch (CompanionException e) {
            CompanionAddedMessageRunner.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 5550;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionAddedMessageRunner.class);
    }
}
