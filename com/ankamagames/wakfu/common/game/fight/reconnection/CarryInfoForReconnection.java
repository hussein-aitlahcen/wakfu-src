package com.ankamagames.wakfu.common.game.fight.reconnection;

public final class CarryInfoForReconnection
{
    private final long m_carrierId;
    private final long m_carriedId;
    private final int m_effectId;
    
    public CarryInfoForReconnection(final long carrierId, final long carriedId, final int effectId) {
        super();
        this.m_carrierId = carrierId;
        this.m_carriedId = carriedId;
        this.m_effectId = effectId;
    }
    
    public long getCarrierId() {
        return this.m_carrierId;
    }
    
    public long getCarriedId() {
        return this.m_carriedId;
    }
    
    public int getEffectId() {
        return this.m_effectId;
    }
    
    @Override
    public String toString() {
        return "CarryInfoForReconnection{m_carrierId=" + this.m_carrierId + ", m_carriedId=" + this.m_carriedId + ", m_effectId=" + this.m_effectId + '}';
    }
}
