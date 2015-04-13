package com.ankamagames.wakfu.common.game.inventory.reborn.change.cosmetics;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;

class AddItemChange implements CosmeticsInventoryChange
{
    private static final Logger m_logger;
    private CosmeticsItem m_item;
    
    AddItemChange() {
        super();
    }
    
    AddItemChange(final CosmeticsItem item) {
        super();
        this.m_item = item;
    }
    
    @Override
    public byte[] serialize() {
        final RawCosmeticsItemInventory.RawCosmeticsItem raw = CosmeticsInventorySerializer.itemToRaw(this.m_item);
        final ByteBuffer bb = ByteBuffer.allocate(raw.serializedSize());
        raw.serialize(bb);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        final RawCosmeticsItemInventory.RawCosmeticsItem raw = new RawCosmeticsItemInventory.RawCosmeticsItem();
        raw.unserialize(bb);
        try {
            this.m_item = CosmeticsInventorySerializer.itemFromRaw(raw, ReferenceItemManager.getInstance());
        }
        catch (CosmeticsInventoryException e) {
            AddItemChange.m_logger.error((Object)("Impossible de d\u00e9-s\u00e9rialiser l'item " + raw), (Throwable)e);
        }
    }
    
    @Override
    public void compute(final CosmeticsInventoryController controller) {
        try {
            controller.addItem(this.m_item);
        }
        catch (CosmeticsInventoryException e) {
            AddItemChange.m_logger.error((Object)"Impossible d'ajouter l'item", (Throwable)e);
        }
    }
    
    @Override
    public CosmeticsInventoryChangeType getType() {
        return CosmeticsInventoryChangeType.ADD_ITEM;
    }
    
    @Override
    public String toString() {
        return "AddItemChange{m_item=" + this.m_item + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddItemChange.class);
    }
}
