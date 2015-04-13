package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class EquipmentItemProvider implements InventoryContentProvider<Item, RawInventoryItem>
{
    public static final EquipmentItemProvider m_instance;
    
    public static EquipmentItemProvider getInstance() {
        return EquipmentItemProvider.m_instance;
    }
    
    @Override
    public Item unSerializeContent(final RawInventoryItem rawItem) {
        return ReferenceItemManager.getInstance().unSerializeContent(rawItem);
    }
    
    static {
        m_instance = new EquipmentItemProvider();
    }
}
