package com.ankamagames.wakfu.common.game.inventory.action;

import com.ankamagames.framework.kernel.core.common.*;
import java.nio.*;

public class InventoryAddItemAction implements InventoryAction
{
    public static final SimpleObjectFactory<InventoryAction> FACTORY;
    private long m_itemId;
    private short m_quantity;
    private byte m_destinationPosition;
    
    public InventoryAddItemAction(final long itemId, final short quantity) {
        super();
        this.m_itemId = itemId;
        this.m_quantity = quantity;
        this.m_destinationPosition = -1;
    }
    
    public InventoryAddItemAction(final long itemId, final short quantity, final byte destinationPosition) {
        super();
        this.m_itemId = itemId;
        this.m_quantity = quantity;
        this.m_destinationPosition = destinationPosition;
    }
    
    private InventoryAddItemAction() {
        super();
    }
    
    @Override
    public int serializedSize() {
        return 11;
    }
    
    @Override
    public void serializeIn(final ByteBuffer buffer) {
        buffer.putLong(this.m_itemId);
        buffer.putShort(this.m_quantity);
        buffer.put(this.m_destinationPosition);
    }
    
    @Override
    public void unSerializeFrom(final ByteBuffer buffer) {
        this.m_itemId = buffer.getLong();
        this.m_quantity = buffer.getShort();
        this.m_destinationPosition = buffer.get();
    }
    
    @Override
    public InventoryActionType getType() {
        return InventoryActionType.ADD_ITEM;
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public byte getDestinationPosition() {
        return this.m_destinationPosition;
    }
    
    @Override
    public String toString() {
        return "InventoryAddItemAction{m_itemId=" + this.m_itemId + ", m_quantity=" + this.m_quantity + ", m_destinationPosition=" + this.m_destinationPosition + '}';
    }
    
    static {
        FACTORY = new SimpleObjectFactory<InventoryAction>() {
            @Override
            public InventoryAction createNew() {
                return new InventoryAddItemAction(null);
            }
        };
    }
}
