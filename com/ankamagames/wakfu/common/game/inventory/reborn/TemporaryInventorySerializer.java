package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.temporary.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;

public final class TemporaryInventorySerializer
{
    private final TemporaryInventory m_inventory;
    private final RawInventoryItemInventory m_raw;
    
    public TemporaryInventorySerializer(final TemporaryInventory inventory, final RawInventoryItemInventory raw) {
        super();
        this.m_inventory = inventory;
        this.m_raw = raw;
    }
    
    void toRaw() {
        final TemporaryInventoryModel inventory = (TemporaryInventoryModel)this.m_inventory;
        this.m_raw.contents.clear();
        for (short i = 0; i < inventory.size(); ++i) {
            final Item item = inventory.getByIdx(i);
            if (item != null) {
                final RawInventoryItemInventory.Content content = new RawInventoryItemInventory.Content();
                item.toRaw(content.item);
                content.position = i;
                this.m_raw.contents.add(content);
            }
        }
    }
    
    public void fromRaw() {
        final TemporaryInventoryModel inventory = (TemporaryInventoryModel)this.m_inventory;
        inventory.clear();
        for (int i = 0, size = this.m_raw.contents.size(); i < size; ++i) {
            final RawInventoryItemInventory.Content content = this.m_raw.contents.get(i);
            final Item item = ReferenceItemManager.getInstance().unSerializeContent(content.item);
            inventory.add(item);
        }
    }
    
    @Override
    public String toString() {
        return "TemporaryInventorySerializer{m_inventory=" + this.m_inventory + '}';
    }
}
