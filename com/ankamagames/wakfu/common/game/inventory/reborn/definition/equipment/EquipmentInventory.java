package com.ankamagames.wakfu.common.game.inventory.reborn.definition.equipment;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;

public interface EquipmentInventory extends Inventory<EquipmentItem>
{
    EquipmentItem getItem(EquipmentPosition p0);
    
    boolean addListener(EquipmentListener p0);
    
    boolean removeListener(EquipmentListener p0);
}
