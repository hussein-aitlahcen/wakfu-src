package com.ankamagames.wakfu.common.game.nation.crime.data;

final class CitizenRankImpl implements CitizenRank
{
    private final int m_id;
    private final int m_cap;
    private final int m_pdcLossFactor;
    private final String m_translationKey;
    private final String m_color;
    private final CitizenRankRule[] m_rules;
    
    CitizenRankImpl(final int id, final int cap, final int pdcLossFactor, final String translationKey, final String color, final CitizenRankRule[] rules) {
        super();
        this.m_id = id;
        this.m_cap = cap;
        this.m_pdcLossFactor = pdcLossFactor;
        this.m_translationKey = translationKey;
        this.m_color = color;
        this.m_rules = rules;
    }
    
    @Override
    public String getTranslationKey() {
        return this.m_translationKey;
    }
    
    @Override
    public int getCap() {
        return this.m_cap;
    }
    
    @Override
    public String getColor() {
        return this.m_color;
    }
    
    @Override
    public int getPdcLossFactor() {
        return this.m_pdcLossFactor;
    }
    
    @Override
    public boolean hasRule(final CitizenRankRule rule) {
        for (int i = 0; i < this.m_rules.length; ++i) {
            if (this.m_rules[i] == rule) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
}
