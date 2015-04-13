package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.wakfu.common.game.nation.crime.data.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class CitizenRankBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_cap;
    protected int m_pdcLossFactor;
    protected String m_translationKey;
    protected String m_color;
    protected int[] m_rules;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getCap() {
        return this.m_cap;
    }
    
    public int getPdcLossFactor() {
        return this.m_pdcLossFactor;
    }
    
    public String getTranslationKey() {
        return this.m_translationKey;
    }
    
    public String getColor() {
        return this.m_color;
    }
    
    public int[] getRules() {
        return this.m_rules;
    }
    
    public CitizenRankRule[] getCitizenRankRules() {
        final CitizenRankRule[] rules = new CitizenRankRule[this.m_rules.length];
        for (int i = 0; i < this.m_rules.length; ++i) {
            rules[i] = CitizenRankRule.getFromId(this.m_rules[i]);
        }
        return rules;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_cap = 0;
        this.m_pdcLossFactor = 0;
        this.m_translationKey = null;
        this.m_color = null;
        this.m_rules = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_cap = buffer.getInt();
        this.m_pdcLossFactor = buffer.getInt();
        this.m_translationKey = buffer.readUTF8().intern();
        this.m_color = buffer.readUTF8().intern();
        this.m_rules = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.CITIZEN_RANK.getId();
    }
}
