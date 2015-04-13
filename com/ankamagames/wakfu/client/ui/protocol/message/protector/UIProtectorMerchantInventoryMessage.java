package com.ankamagames.wakfu.client.ui.protocol.message.protector;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.protector.inventory.*;

public class UIProtectorMerchantInventoryMessage extends UIMessage
{
    private ProtectorMerchantInventory m_protectorMerchantInventory;
    
    public ProtectorMerchantInventory getProtectorMerchantInventory() {
        return this.m_protectorMerchantInventory;
    }
    
    public void setProtectorMerchantInventory(final ProtectorMerchantInventory protectorMerchantInventory) {
        this.m_protectorMerchantInventory = protectorMerchantInventory;
    }
}
