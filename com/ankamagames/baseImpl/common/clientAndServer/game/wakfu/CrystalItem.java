package com.ankamagames.baseImpl.common.clientAndServer.game.wakfu;

public class CrystalItem
{
    private short m_quantity;
    private long m_bagId;
    private short m_position;
    
    public CrystalItem(final short quantity, final long bagId, final short position) {
        super();
        this.m_quantity = quantity;
        this.m_bagId = bagId;
        this.m_position = position;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public long getBagId() {
        return this.m_bagId;
    }
    
    public short getPosition() {
        return this.m_position;
    }
}
