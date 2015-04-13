package com.ankamagames.wakfu.common.game.inventory.action;

import com.ankamagames.framework.kernel.core.common.*;
import java.nio.*;

public class InventoryMoveItemAction implements InventoryAction
{
    public static final SimpleObjectFactory<InventoryAction> FACTORY;
    private long m_itemId;
    private byte m_destinationPosition;
    
    public InventoryMoveItemAction(final long itemId, final byte destinationPosition) {
        super();
        this.m_itemId = itemId;
        this.m_destinationPosition = destinationPosition;
    }
    
    private InventoryMoveItemAction() {
        super();
    }
    
    @Override
    public int serializedSize() {
        return 9;
    }
    
    @Override
    public void serializeIn(final ByteBuffer buffer) {
        buffer.putLong(this.m_itemId);
        buffer.put(this.m_destinationPosition);
    }
    
    @Override
    public void unSerializeFrom(final ByteBuffer buffer) {
        this.m_itemId = buffer.getLong();
        this.m_destinationPosition = buffer.get();
    }
    
    @Override
    public InventoryActionType getType() {
        return InventoryActionType.MOVE_ITEM;
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
    
    public byte getDestinationPosition() {
        return this.m_destinationPosition;
    }
    
    @Override
    public String toString() {
        return "InventoryMoveItemAction{m_itemId=" + this.m_itemId + ", m_destinationPosition=" + this.m_destinationPosition + '}';
    }
    
    static {
        FACTORY = new SimpleObjectFactory<InventoryAction>() {
            @Override
            public InventoryAction createNew() {
                return new InventoryMoveItemAction(null);
            }
        };
    }
}
