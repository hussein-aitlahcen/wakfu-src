package com.ankamagames.wakfu.common.game.item.visitor.operation;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class UpdateItemOperation extends BagOperation implements QuantityOperation
{
    private short m_qty;
    private boolean m_insideMove;
    
    public UpdateItemOperation(final short quantity) {
        super();
        this.m_qty = quantity;
    }
    
    public UpdateItemOperation() {
        super();
    }
    
    public short getQty() {
        return this.m_qty;
    }
    
    @Override
    public byte getOperationType() {
        return 2;
    }
    
    public boolean isInsideMove() {
        return this.m_insideMove;
    }
    
    public void setInsideMove(final boolean insideMove) {
        this.m_insideMove = insideMove;
    }
    
    @Override
    public void updateQuantity(final short quantity) {
        this.m_qty = quantity;
    }
    
    @Override
    public void serialize(final ByteArray buff) {
        buff.putShort(this.m_qty);
    }
    
    @Override
    public void unSerialize(final ByteBuffer buffer) {
        this.m_qty = buffer.getShort();
    }
}
