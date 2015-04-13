package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class ItemType extends AbstractItemType<ItemType>
{
    public ItemType(final short id, final short parentId, final EquipmentPosition[] equipPositions, final int[] craftIds, final short materialType) {
        super(id, parentId, equipPositions, craftIds, materialType);
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(14, this.getId(), new Object[0]);
    }
    
    public boolean hasChilds() {
        return super.hasChilds();
    }
    
    public GrowingArray<ItemType> getChilds() {
        return super.getChilds();
    }
}
