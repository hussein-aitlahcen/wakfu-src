package com.ankamagames.wakfu.common.game.inventory;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class EquipmentToEquipmentExchanger extends AbstractInventoryContentExchanger<ItemEquipment, ItemEquipment>
{
    protected static final EquipmentToEquipmentExchanger m_instance;
    
    @Override
    public int moveItem(final ItemEquipment sourceInventory, final ItemEquipment targetInventory, final Item item, final EffectUser player, final EffectContext context) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        return -1;
    }
    
    @Override
    public int moveItem(final ItemEquipment sourceInventory, final short sourcePosition, final ItemEquipment targetInventory, final short targetPosition, final Item item, final EffectUser player, final EffectContext context) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        final Item targetItem = ((ArrayInventoryWithoutCheck<Item, R>)targetInventory).getFromPosition(targetPosition);
        return 1;
    }
    
    public static EquipmentToEquipmentExchanger getInstance() {
        return EquipmentToEquipmentExchanger.m_instance;
    }
    
    static {
        m_instance = new EquipmentToEquipmentExchanger();
    }
}
