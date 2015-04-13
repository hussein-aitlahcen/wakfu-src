package com.ankamagames.wakfu.common.game.inventory.reborn.change.temporaryInventory;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;

class ItemQuantityChange implements TemporaryInventoryChange
{
    private static final Logger m_logger;
    private long m_itemId;
    private short m_quantity;
    private short m_position;
    private long m_destination;
    
    ItemQuantityChange() {
        super();
    }
    
    ItemQuantityChange(final long itemId, final short quantity, final long container, final short position) {
        super();
        this.m_itemId = itemId;
        this.m_quantity = quantity;
        this.m_destination = container;
        this.m_position = position;
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(20);
        bb.putLong(this.m_itemId);
        bb.putShort(this.m_quantity);
        bb.putLong(this.m_destination);
        bb.putShort(this.m_position);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_itemId = bb.getLong();
        this.m_quantity = bb.getShort();
        this.m_destination = bb.getLong();
        this.m_position = bb.getShort();
    }
    
    @Override
    public void compute(final TemporaryInventoryController controller) {
        try {
            if (this.m_destination == -1L || this.m_destination == 0L) {
                controller.setQuantity(this.m_itemId, this.m_quantity);
            }
            else if (this.m_destination == 2L) {
                controller.moveToEquipment(this.m_itemId, this.m_position);
            }
            else {
                final short quantity = (short)(controller.getQuantity(this.m_itemId) - this.m_quantity);
                controller.moveToInventory(this.m_itemId, this.m_destination, this.m_position, quantity);
            }
        }
        catch (TemporaryInventoryException e) {
            ItemQuantityChange.m_logger.error((Object)("Impossible de modifier la quantit\u00e9. " + e.getMessage()));
        }
    }
    
    @Override
    public TemporaryInventoryChangeType getType() {
        return TemporaryInventoryChangeType.ITEM_QUANTITY;
    }
    
    @Override
    public String toString() {
        return "ItemQuantityChange{m_itemId=" + this.m_itemId + ", m_quantity=" + this.m_quantity + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemQuantityChange.class);
    }
}
