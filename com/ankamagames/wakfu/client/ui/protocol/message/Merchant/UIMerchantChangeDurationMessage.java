package com.ankamagames.wakfu.client.ui.protocol.message.Merchant;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.wakfu.client.core.game.item.*;

public class UIMerchantChangeDurationMessage extends UIMessage
{
    private static final Logger m_logger;
    private AuctionDuration m_duration;
    private MerchantInventoryItem m_merchantInventoryItem;
    
    public AuctionDuration getDuration() {
        return this.m_duration;
    }
    
    public void setDuration(final AuctionDuration duration) {
        this.m_duration = duration;
    }
    
    public MerchantInventoryItem getMerchantInventoryItem() {
        return this.m_merchantInventoryItem;
    }
    
    public void setMerchantInventoryItem(final MerchantInventoryItem merchantInventoryItem) {
        this.m_merchantInventoryItem = merchantInventoryItem;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIMerchantChangeDurationMessage.class);
    }
}
