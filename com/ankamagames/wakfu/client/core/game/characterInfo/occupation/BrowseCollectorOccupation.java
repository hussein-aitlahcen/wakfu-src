package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.collector.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.collector.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class BrowseCollectorOccupation extends AbstractOccupation
{
    private static final Logger m_logger;
    private final CollectorOccupationProvider m_collector;
    private final CollectorEventHandler m_eventHandler;
    
    public BrowseCollectorOccupation(final CollectorOccupationProvider machine, final CollectorEventHandler eventHandler) {
        super();
        this.m_collector = machine;
        this.m_eventHandler = eventHandler;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 17;
    }
    
    @Override
    public boolean isAllowed() {
        return this.m_localPlayer.getCurrentOccupation() == null;
    }
    
    @Override
    public void begin() {
        BrowseCollectorOccupation.m_logger.info((Object)"Lancement de l'occupation BROWSE_COLLECTOR");
        this.m_localPlayer.setCurrentOccupation(this);
        WakfuGameEntity.getInstance().pushFrame(NetCollectorMessageFrame.INSTANCE);
    }
    
    @Override
    public boolean cancel(final boolean fromServeur, final boolean sendMessage) {
        BrowseCollectorOccupation.m_logger.info((Object)"On cancel l'occupation BROWSE_COLLECTOR");
        if (sendMessage) {
            this.m_collector.getInteractiveElement().fireAction(InteractiveElementAction.STOP_BROWSING, WakfuGameEntity.getInstance().getLocalPlayer());
        }
        return this.finish();
    }
    
    @Override
    public boolean finish() {
        BrowseCollectorOccupation.m_logger.info((Object)"On fini l'occupation BROWSE_COLLECTOR");
        if (WakfuGameEntity.getInstance().hasFrame(UICollectMachineFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UICollectMachineFrame.getInstance());
        }
        WakfuGameEntity.getInstance().removeFrame(NetCollectorMessageFrame.INSTANCE);
        return true;
    }
    
    public void onContentMessage(final byte[] serializedContent) {
        this.m_collector.unSerializeInventory(serializedContent);
        if (this.m_eventHandler != null) {
            this.m_eventHandler.onContentMessageReceived();
        }
    }
    
    public void sendCollectorInventoryModificationRequest(final TIntIntHashMap items, final int cash) {
        final CollectorInventoryLimitedModificationRequestMessage msg = new CollectorInventoryLimitedModificationRequestMessage();
        msg.addedItem(items);
        msg.setCash(cash);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    static {
        m_logger = Logger.getLogger((Class)BrowseCollectorOccupation.class);
    }
}
