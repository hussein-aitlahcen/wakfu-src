package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item.xp.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.item.xp.*;
import com.ankamagames.wakfu.common.game.item.xp.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public final class NetItemXpFrame extends MessageRunnerFrame
{
    private static final Logger m_logger;
    public static final NetItemXpFrame INSTANCE;
    
    private NetItemXpFrame() {
        super(new MessageRunner[] { new ItempXpChangeMessageRunner() });
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
        m_logger = Logger.getLogger((Class)NetItemXpFrame.class);
        INSTANCE = new NetItemXpFrame();
    }
    
    private static class ItempXpChangeMessageRunner implements MessageRunner<ItemXpChangeMessage>
    {
        @Override
        public boolean run(final ItemXpChangeMessage msg) {
            final long itemId = msg.getItemId();
            final Item item = WakfuGameEntity.getInstance().getLocalPlayer().getFromEquipmentOrInventory(itemId);
            final ItemXpControllerUINotifier controller = new ItemXpControllerUINotifier(item);
            controller.setXp(msg.getXp());
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15990;
        }
    }
}
