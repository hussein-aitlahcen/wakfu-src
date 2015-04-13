package com.ankamagames.wakfu.common.game.craftNew;

public class CraftUserRateEntry
{
    private final long m_raterUserId;
    private final long m_ratedUserId;
    private final short m_sympathyRate;
    private final short m_speedRate;
    private final short m_priceRate;
    
    public CraftUserRateEntry(final long raterUserId, final long ratedUserId, final short sympathyRate, final short speedRate, final short priceRate) {
        super();
        this.m_raterUserId = raterUserId;
        this.m_ratedUserId = ratedUserId;
        this.m_sympathyRate = sympathyRate;
        this.m_speedRate = speedRate;
        this.m_priceRate = priceRate;
    }
    
    public long getRaterUserId() {
        return this.m_raterUserId;
    }
    
    public long getRatedUserId() {
        return this.m_ratedUserId;
    }
    
    public short getSympathyRate() {
        return this.m_sympathyRate;
    }
    
    public short getSpeedRate() {
        return this.m_speedRate;
    }
    
    public short getPriceRate() {
        return this.m_priceRate;
    }
    
    public byte[] serialize() {
        return CraftSerializer.serializeCraftRateEntry(this);
    }
}
