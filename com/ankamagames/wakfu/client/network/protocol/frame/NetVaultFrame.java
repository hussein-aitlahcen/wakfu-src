package com.ankamagames.wakfu.client.network.protocol.frame;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.core.dragndrop.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.vault.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.vault.*;

public final class NetVaultFrame extends MessageRunnerFrame
{
    private static final Logger m_logger;
    public static final NetVaultFrame INSTANCE;
    
    private NetVaultFrame() {
        super(new MessageRunner[] { new VaultConsultResultRunner(), new VaultReloadedRunner() });
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetVaultFrame.class);
        INSTANCE = new NetVaultFrame();
    }
    
    private static class VaultConsultResultRunner implements MessageRunner<VaultConsultResultMessage>
    {
        @Override
        public boolean run(final VaultConsultResultMessage msg) {
            final RawVault rawVault = msg.getRawVault();
            DragNDropManager.getInstance().cancel();
            UIVaultFrame.getInstance().clearContent();
            UIVaultFrame.getInstance().onVaultReceived(rawVault, msg.getUpgrade());
            NetVaultFrame.m_logger.info((Object)"[VAULT]  Contenu re\u00e7u");
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15671;
        }
    }
    
    private static class VaultReloadedRunner implements MessageRunner<VaultReloadedMessage>
    {
        @Override
        public boolean run(final VaultReloadedMessage msg) {
            if (!WakfuGameEntity.getInstance().hasFrame(UIVaultFrame.getInstance())) {
                return false;
            }
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new VaultConsultRequestMessage());
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15674;
        }
    }
}
