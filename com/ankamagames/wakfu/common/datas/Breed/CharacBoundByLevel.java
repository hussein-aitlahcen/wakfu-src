package com.ankamagames.wakfu.common.datas.Breed;

public abstract class CharacBoundByLevel
{
    protected byte m_characId;
    
    CharacBoundByLevel(final byte characId) {
        super();
        this.m_characId = characId;
    }
    
    public abstract int getBound(final int p0);
    
    public byte getCharacId() {
        return this.m_characId;
    }
}
