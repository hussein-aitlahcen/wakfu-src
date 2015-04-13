package com.ankamagames.wakfu.common.game.nation.government;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public enum NationRank implements ExportableEnum
{
    GOVERNOR(1L, "Gouverneur"), 
    DEPUTY(2L, "Vice-gouverneur"), 
    MARSHAL(3L, "Pr\u00e9v\u00f4t"), 
    GENERAL(4L, "G\u00e9n\u00e9ral"), 
    METEOROLOGIST(5L, "M\u00e9t\u00e9orologiste"), 
    TREASURER(6L, "Tr\u00e9sorier"), 
    CHALLENGER(7L, "Challenger"), 
    SPY(8L, "Espion"), 
    ASSASSIN(9L, "Assassin"), 
    ZOOLOGIST(10L, "Zoologiste");
    
    private final byte m_baseId;
    private final long m_id;
    private final String m_label;
    private float m_pdcLossFactor;
    private int m_citizenScoreLine;
    private SimpleCriterion m_criterion;
    
    private NationRank(final long id, final String label) {
        this.m_criterion = null;
        this.m_baseId = (byte)id;
        this.m_id = 1 << (int)id;
        this.m_label = label;
    }
    
    public static NationRank getById(final long id) {
        for (final NationRank nationRank : values()) {
            if (nationRank.getId() == id) {
                return nationRank;
            }
        }
        return null;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public void setCriterion(final SimpleCriterion criterion) {
        this.m_criterion = criterion;
    }
    
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    public void setPdcLossFactor(final float pdcLossFactor) {
        this.m_pdcLossFactor = pdcLossFactor;
    }
    
    public float getPdcLossFactor() {
        return this.m_pdcLossFactor;
    }
    
    public byte getBaseId() {
        return this.m_baseId;
    }
    
    public int getCitizenScoreLine() {
        return this.m_citizenScoreLine;
    }
    
    public void setCitizenScoreLine(final int citizenScoreLine) {
        this.m_citizenScoreLine = citizenScoreLine;
    }
    
    @Override
    public String toString() {
        return "NationRank{m_baseId=" + this.m_baseId + ", m_id=" + this.m_id + ", m_label='" + this.m_label + '\'' + ", m_pdcLossFactor=" + this.m_pdcLossFactor + ", m_citizenScoreLine=" + this.m_citizenScoreLine + ", m_criterion=" + this.m_criterion + '}';
    }
}
