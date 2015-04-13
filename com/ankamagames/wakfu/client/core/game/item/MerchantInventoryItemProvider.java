package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class MerchantInventoryItemProvider implements InventoryContentProvider<AbstractMerchantInventoryItem, RawMerchantItem>
{
    private static final MerchantInventoryItemProvider m_instance;
    
    public static MerchantInventoryItemProvider getInstance() {
        return MerchantInventoryItemProvider.m_instance;
    }
    
    @Override
    public AbstractMerchantInventoryItem unSerializeContent(final RawMerchantItem rawItem) {
        final MerchantInventoryItem merchantItem = MerchantInventoryItem.checkout();
        if (merchantItem.fromRaw(rawItem)) {
            return merchantItem;
        }
        merchantItem.release();
        return null;
    }
    
    static {
        m_instance = new MerchantInventoryItemProvider();
    }
}
