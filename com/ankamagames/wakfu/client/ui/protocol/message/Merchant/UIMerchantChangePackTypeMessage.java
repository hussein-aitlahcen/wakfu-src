package com.ankamagames.wakfu.client.ui.protocol.message.Merchant;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.wakfu.client.core.game.item.*;

public class UIMerchantChangePackTypeMessage extends UIMessage
{
    private static final Logger m_logger;
    private PackType m_packType;
    private MerchantInventoryItem m_merchantInventoryItem;
    
    public PackType getPackType() {
        return this.m_packType;
    }
    
    public void setPackType(final PackType packType) {
        this.m_packType = packType;
    }
    
    public MerchantInventoryItem getMerchantInventoryItem() {
        return this.m_merchantInventoryItem;
    }
    
    public void setMerchantInventoryItem(final MerchantInventoryItem merchantInventoryItem) {
        this.m_merchantInventoryItem = merchantInventoryItem;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIMerchantChangePackTypeMessage.class);
    }
}
