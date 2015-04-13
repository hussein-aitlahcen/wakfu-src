package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gift.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.client.core.game.gift.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class NetGiftFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static NetGiftFrame INSTANCE;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 13000: {
                final GiftInventoryResultMessage inventoryMessage = (GiftInventoryResultMessage)message;
                this.onGiftInventoryResult(inventoryMessage);
                return false;
            }
            case 13004: {
                final GiftConsumeResultMessage resultMessage = (GiftConsumeResultMessage)message;
                this.onGiftConsumeResult(resultMessage);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public void onGiftInventoryResult(final GiftInventoryResultMessage message) {
        final ArrayList<RawGiftPackage> inventory = message.getGiftTokenAccountInventory();
        final ArrayList<GiftPackage> giftInventory = new ArrayList<GiftPackage>();
        for (int i = 0; i < inventory.size(); ++i) {
            final RawGiftPackage rawGiftPackage = inventory.get(i);
            final GiftPackage giftPackage = new GiftPackage();
            giftPackage.fromRaw(rawGiftPackage);
            giftInventory.add(giftPackage);
        }
        GiftManager.getInstance().setPlayerGifts(giftInventory);
        WakfuGameEntity.getInstance().pushFrame(UIPlayerGiftFrame.getInstance());
    }
    
    public void onGiftConsumeResult(final GiftConsumeResultMessage message) {
        final boolean consumeResult = message.isConsumeResult();
        GiftManager.getInstance().validateLastRequest(consumeResult);
        UIPlayerGiftFrame.getInstance().setNetEnableGifts(true);
        if (GiftManager.getInstance().isEmpty()) {
            WakfuGameEntity.getInstance().removeFrame(UIPlayerGiftFrame.getInstance());
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetGiftFrame.class);
        NetGiftFrame.INSTANCE = new NetGiftFrame();
    }
}
