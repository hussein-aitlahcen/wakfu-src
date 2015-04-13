package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.havenWorld.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;

public class ManageHavenWorldOccupation extends AbstractOccupation
{
    private static final Logger m_logger;
    private final HavenWorldActionManager m_actionManager;
    private final HavenWorldFrame m_uiFrame;
    
    public ManageHavenWorldOccupation(final HavenWorldFrame uiFrame) {
        super();
        this.m_actionManager = new HavenWorldActionManager();
        this.m_uiFrame = uiFrame;
    }
    
    public HavenWorldActionManager getActionManager() {
        return this.m_actionManager;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 25;
    }
    
    @Override
    public boolean isAllowed() {
        final AbstractOccupation occupation = this.m_localPlayer.getCurrentOccupation();
        if (occupation != null && occupation != this) {
            return false;
        }
        final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(this.m_localPlayer.getInstanceId());
        if (!worldInfo.isHavenWorld()) {
            return false;
        }
        final long guildId = this.m_localPlayer.getGuildHandler().getGuildId();
        return guildId > 0L;
    }
    
    @Override
    public void begin() {
        ManageHavenWorldOccupation.m_logger.info((Object)"[HAVEN_WORLD] Lancement de l'occupation");
        this.m_localPlayer.setCurrentOccupation(this);
        WakfuGameEntity.getInstance().pushFrame(this.m_uiFrame);
    }
    
    @Override
    public boolean cancel(final boolean fromServer, final boolean sendMessage) {
        ManageHavenWorldOccupation.m_logger.info((Object)"[HAVEN_WORLD] On cancel l'occupation");
        if (sendMessage) {
            final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
            netMsg.setModificationType((byte)3);
            netMsg.setOccupationType(this.getOccupationTypeId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        }
        if (WakfuGameEntity.getInstance().hasFrame(this.m_uiFrame)) {
            WakfuGameEntity.getInstance().removeFrame(this.m_uiFrame);
        }
        NetHavenWorldFrame.INSTANCE.setCacheOccupation(null);
        return true;
    }
    
    @Override
    public boolean finish() {
        ManageHavenWorldOccupation.m_logger.info((Object)"[HAVEN_WORLD] On fini l'occupation");
        if (WakfuGameEntity.getInstance().hasFrame(this.m_uiFrame)) {
            WakfuGameEntity.getInstance().removeFrame(this.m_uiFrame);
        }
        final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
        netMsg.setModificationType((byte)2);
        netMsg.setOccupationType(this.getOccupationTypeId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        NetHavenWorldFrame.INSTANCE.setCacheOccupation(null);
        return true;
    }
    
    public HavenWorldFrame getUiFrame() {
        return this.m_uiFrame;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ManageHavenWorldOccupation.class);
    }
}
