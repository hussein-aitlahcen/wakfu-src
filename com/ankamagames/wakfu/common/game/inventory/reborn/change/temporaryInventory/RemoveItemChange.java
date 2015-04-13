package com.ankamagames.wakfu.common.game.inventory.reborn.change.temporaryInventory;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;
import com.ankamagames.wakfu.common.game.item.*;

class RemoveItemChange implements TemporaryInventoryChange
{
    private static final Logger m_logger;
    private long m_destinationId;
    private short m_position;
    private long m_itemId;
    
    RemoveItemChange() {
        super();
    }
    
    RemoveItemChange(final long itemId, final long containerId, final short position) {
        super();
        this.m_itemId = itemId;
        this.m_destinationId = containerId;
        this.m_position = position;
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(18);
        bb.putLong(this.m_itemId);
        bb.putLong(this.m_destinationId);
        bb.putShort(this.m_position);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_itemId = bb.getLong();
        this.m_destinationId = bb.getLong();
        this.m_position = bb.getShort();
    }
    
    @Override
    public void compute(final TemporaryInventoryController controller) {
        try {
            if (this.m_destinationId == -1L) {
                final Item item = controller.removeItem(this.m_itemId);
                if (item != null) {
                    item.release();
                }
            }
            else if (this.m_destinationId == 2L) {
                controller.moveToEquipment(this.m_itemId, this.m_position);
            }
            else {
                controller.moveToInventory(this.m_itemId, this.m_destinationId, this.m_position, (short)(-1));
            }
        }
        catch (TemporaryInventoryException e) {
            RemoveItemChange.m_logger.error((Object)("Impossible de retirer l'objet : " + e.getMessage()));
        }
    }
    
    @Override
    public TemporaryInventoryChangeType getType() {
        return TemporaryInventoryChangeType.REMOVE_ITEM;
    }
    
    @Override
    public String toString() {
        return "RemoveItemChange{m_itemId=" + this.m_itemId + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)RemoveItemChange.class);
    }
}
