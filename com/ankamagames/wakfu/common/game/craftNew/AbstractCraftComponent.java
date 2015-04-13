package com.ankamagames.wakfu.common.game.craftNew;

public abstract class AbstractCraftComponent implements ContractCraftComponent
{
    private final int m_referenceItemId;
    protected short m_quantity;
    private byte m_position;
    
    public AbstractCraftComponent(final int referenceItemId) {
        this(referenceItemId, (byte)(-1));
    }
    
    public AbstractCraftComponent(final int referenceItemId, final byte position) {
        super();
        this.m_referenceItemId = referenceItemId;
        this.m_position = position;
    }
    
    @Override
    public byte getPosition() {
        return this.m_position;
    }
    
    @Override
    public int getItemReferenceId() {
        return this.m_referenceItemId;
    }
    
    @Override
    public short getQuantity() {
        return this.m_quantity;
    }
}
