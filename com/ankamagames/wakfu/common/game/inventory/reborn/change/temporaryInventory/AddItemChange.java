package com.ankamagames.wakfu.common.game.inventory.reborn.change.temporaryInventory;

import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;

class AddItemChange implements TemporaryInventoryChange
{
    private Item m_item;
    
    AddItemChange() {
        super();
    }
    
    AddItemChange(final Item item) {
        super();
        this.m_item = item;
    }
    
    @Override
    public byte[] serialize() {
        final RawInventoryItem rawItem = new RawInventoryItem();
        this.m_item.toRaw(rawItem);
        final ByteBuffer bb = ByteBuffer.allocate(rawItem.serializedSize());
        rawItem.serialize(bb);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        final RawInventoryItem rawItem = new RawInventoryItem();
        rawItem.unserialize(bb);
        this.m_item = ReferenceItemManager.getInstance().unSerializeContent(rawItem);
    }
    
    @Override
    public void compute(final TemporaryInventoryController controller) {
        controller.addItem(this.m_item);
    }
    
    @Override
    public TemporaryInventoryChangeType getType() {
        return TemporaryInventoryChangeType.ADD_ITEM;
    }
    
    @Override
    public String toString() {
        return "AddItemChange{m_item=" + this.m_item + '}';
    }
}
