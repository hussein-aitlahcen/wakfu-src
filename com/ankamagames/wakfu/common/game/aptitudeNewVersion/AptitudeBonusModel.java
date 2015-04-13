package com.ankamagames.wakfu.common.game.aptitudeNewVersion;

public class AptitudeBonusModel
{
    private final int m_id;
    private final int m_effectId;
    private final int m_max;
    
    public AptitudeBonusModel(final int id, final int effectId) {
        super();
        this.m_id = id;
        this.m_effectId = effectId;
        this.m_max = 0;
    }
    
    public AptitudeBonusModel(final int id, final int effectId, final int max) {
        super();
        this.m_id = id;
        this.m_effectId = effectId;
        this.m_max = max;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getEffectId() {
        return this.m_effectId;
    }
    
    public int getMax() {
        return this.m_max;
    }
    
    @Override
    public String toString() {
        return "AptitudeBonusModel{m_id=" + this.m_id + ", m_effectId=" + this.m_effectId + '}';
    }
}
