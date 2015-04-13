package com.ankamagames.wakfu.client.core.game.dimensionalBag;

public class DimensionalBagFromInstanceManager
{
    public static final DimensionalBagFromInstanceManager INSTANCE;
    private short m_fromInstanceId;
    
    public short getFromInstanceId() {
        return this.m_fromInstanceId;
    }
    
    public void setFromInstanceId(final short fromInstanceId) {
        this.m_fromInstanceId = fromInstanceId;
    }
    
    static {
        INSTANCE = new DimensionalBagFromInstanceManager();
    }
}
