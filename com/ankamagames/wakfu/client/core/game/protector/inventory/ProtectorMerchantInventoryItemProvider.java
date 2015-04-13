package com.ankamagames.wakfu.client.core.game.protector.inventory;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ProtectorMerchantInventoryItemProvider implements InventoryContentProvider<AbstractMerchantInventoryItem, RawMerchantItem>
{
    protected static Logger m_logger;
    public static final ProtectorMerchantInventoryItemProvider INSTANCE;
    
    @Override
    public ProtectorMerchantInventoryItem unSerializeContent(final RawMerchantItem rawItem) {
        final ProtectorMerchantInventoryItem item = new ProtectorMerchantInventoryItem();
        if (item.fromRaw(rawItem)) {
            return item;
        }
        ProtectorMerchantInventoryItemProvider.m_logger.error((Object)("Erreur \u00e0 la d\u00e9s\u00e9rialisation d'un rawItem d'inventaire de protecteur (itemRefId=" + rawItem.item.refId + ")"));
        return null;
    }
    
    static {
        ProtectorMerchantInventoryItemProvider.m_logger = Logger.getLogger((Class)ProtectorMerchantInventoryItemProvider.class);
        INSTANCE = new ProtectorMerchantInventoryItemProvider();
    }
}
