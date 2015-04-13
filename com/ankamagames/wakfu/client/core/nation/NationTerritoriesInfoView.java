package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.xulor2.util.*;

public class NationTerritoriesInfoView extends NationFieldProvider
{
    public static final String TERRITORIES_COUNT = "territoriesCount";
    public static final String TERRITORIES_RATIO = "territoriesRatio";
    public static final String TERRITORIES_RATIO_PERC = "territoriesRatioPerc";
    public static final String[] FIELDS;
    private int m_cumulatedProtectorsSize;
    
    public NationTerritoriesInfoView(final int nationId) {
        super(nationId);
    }
    
    @Override
    public String[] getFields() {
        return NationTerritoriesInfoView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("territoriesCount")) {
            return this.getProtectorsSize();
        }
        if (fieldName.equals("territoriesRatio")) {
            return this.getTerritoriesRatio();
        }
        if (fieldName.equals("territoriesRatioPerc")) {
            final int value = this.m_cumulatedProtectorsSize - this.getProtectorsSize();
            final float result = ((value == 0) ? (this.getCumulatedProtectorsSize() / 2.0f) : (value + this.getProtectorsSize() / 2.0f)) / NationDisplayer.getInstance().getTotalProtectorsSize() * 100.0f;
            return new Percentage(result);
        }
        return super.getFieldValue(fieldName);
    }
    
    private float getTerritoriesRatio() {
        return this.m_cumulatedProtectorsSize / NationDisplayer.getInstance().getTotalProtectorsSize();
    }
    
    public int getProtectorsSize() {
        return NationDisplayer.getInstance().getNationProtectorCount(this.m_nationId);
    }
    
    public void setCumulatedProtectorsSize(final int cumulatedProtectorsSize) {
        this.m_cumulatedProtectorsSize = cumulatedProtectorsSize;
    }
    
    public int getCumulatedProtectorsSize() {
        return this.m_cumulatedProtectorsSize;
    }
    
    static {
        FIELDS = new String[] { "territoriesCount", "territoriesRatio", "territoriesRatioPerc" };
    }
}
