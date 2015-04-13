package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.account.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class CompanionListMessageRunner implements MessageRunner<CompanionListMessage>
{
    private static final Logger m_logger;
    
    @Override
    public boolean run(final CompanionListMessage msg) {
        CompanionListMessageRunner.m_logger.info((Object)"R\u00e9ception de la liste des companions");
        final byte[] serializedCompanions = msg.getSerializedCompanions();
        final List<CompanionModel> companions = this.unserializeCompanionList(serializedCompanions);
        final LocalAccountInformations localAccount = WakfuGameEntity.getInstance().getLocalAccount();
        final long clientId = localAccount.getAccountId();
        CompanionManager.INSTANCE.clearCompanions(clientId);
        for (final CompanionModel companion : companions) {
            CompanionListMessageRunner.m_logger.info((Object)("Companion dans le message : " + companion));
            try {
                CompanionManager.INSTANCE.addCompanion(clientId, companion);
                this.resetEquipmentCache(companion);
                if (!WakfuGameEntity.getInstance().hasFrame(UICompanionsManagementFrame.INSTANCE)) {
                    continue;
                }
                UICompanionsManagementFrame.INSTANCE.loadCompanionsList();
                UICompanionsManagementFrame.INSTANCE.reflowCompanionsList();
            }
            catch (Exception e) {
                CompanionListMessageRunner.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        CompanionListMessageRunner.m_logger.info((Object)("Liste des companions : " + CompanionManager.INSTANCE.getCompanions(clientId)));
        return false;
    }
    
    private List<CompanionModel> unserializeCompanionList(final byte[] serializedCompanions) {
        final List<CompanionModel> companions = new ArrayList<CompanionModel>();
        final ByteBuffer bb = ByteBuffer.wrap(serializedCompanions);
        final byte companionListSize = bb.get();
        for (int i = 0; i < companionListSize; ++i) {
            companions.add(CompanionModelSerializer.unserialize(bb));
        }
        return companions;
    }
    
    private void resetEquipmentCache(final CompanionModel companion) {
        for (final Item item : companion.getItemEquipment()) {
            item.resetCache();
        }
    }
    
    @Override
    public int getProtocolId() {
        return 2077;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionListMessageRunner.class);
    }
}
