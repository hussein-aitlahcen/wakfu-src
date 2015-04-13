package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.account.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class CompanionListUpdateMessageRunner implements MessageRunner<CompanionListUpdateMessage>
{
    private static final Logger m_logger;
    
    @Override
    public boolean run(final CompanionListUpdateMessage msg) {
        CompanionListUpdateMessageRunner.m_logger.info((Object)"Mise \u00e0 jour de la liste des compagnons");
        final List<CompanionModel> companions = msg.getCompanions();
        final LocalAccountInformations localPlayer = WakfuGameEntity.getInstance().getLocalAccount();
        final long clientId = localPlayer.getAccountId();
        CompanionManager.INSTANCE.clearCompanions(clientId);
        for (final CompanionModel companion : companions) {
            CompanionListUpdateMessageRunner.m_logger.info((Object)("Companion dans le message : " + companion));
            CompanionManager.INSTANCE.addCompanion(clientId, companion);
        }
        if (WakfuGameEntity.getInstance().hasFrame(UICompanionsManagementFrame.INSTANCE)) {
            UICompanionsManagementFrame.INSTANCE.loadCompanionsList();
            UICompanionsManagementFrame.INSTANCE.reflowCompanionsList();
        }
        CompanionListUpdateMessageRunner.m_logger.info((Object)("Liste des companions : " + CompanionManager.INSTANCE.getCompanions(clientId)));
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 5551;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionListUpdateMessageRunner.class);
    }
}
