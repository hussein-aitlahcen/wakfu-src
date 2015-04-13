package com.ankamagames.wakfu.common.game.inventory.action;

import com.ankamagames.framework.kernel.core.common.*;
import java.nio.*;

public class InventoryRemoveItemAction implements InventoryAction
{
    public static final SimpleObjectFactory<InventoryAction> FACTORY;
    private long m_itemId;
    private short m_quantity;
    private long m_destinationId;
    private byte m_destinationPosition;
    
    public InventoryRemoveItemAction(final long itemId, final short quantity, final long destinationId, final byte destinationPosition) {
        super();
        this.m_itemId = itemId;
        this.m_quantity = quantity;
        this.m_destinationId = destinationId;
        this.m_destinationPosition = destinationPosition;
    }
    
    private InventoryRemoveItemAction() {
        super();
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public long getDestinationId() {
        return this.m_destinationId;
    }
    
    public byte getDestinationPosition() {
        return this.m_destinationPosition;
    }
    
    @Override
    public int serializedSize() {
        return 19;
    }
    
    @Override
    public void serializeIn(final ByteBuffer buffer) {
        buffer.putLong(this.m_itemId);
        buffer.putShort(this.m_quantity);
        buffer.putLong(this.m_destinationId);
        buffer.put(this.m_destinationPosition);
    }
    
    @Override
    public void unSerializeFrom(final ByteBuffer buffer) {
        this.m_itemId = buffer.getLong();
        this.m_quantity = buffer.getShort();
        this.m_destinationId = buffer.getLong();
        this.m_destinationPosition = buffer.get();
    }
    
    @Override
    public InventoryActionType getType() {
        return InventoryActionType.REMOVE_ITEM;
    }
    
    @Override
    public String toString() {
        return "InventoryRemoveItemAction{m_itemId=" + this.m_itemId + ", m_quantity=" + this.m_quantity + ", m_destinationId=" + this.m_destinationId + ", m_destinationPosition=" + this.m_destinationPosition + '}';
    }
    
    static {
        FACTORY = new SimpleObjectFactory<InventoryAction>() {
            @Override
            public InventoryAction createNew() {
                return new InventoryRemoveItemAction(null);
            }
        };
    }
}
