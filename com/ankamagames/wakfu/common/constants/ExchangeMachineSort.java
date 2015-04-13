package com.ankamagames.wakfu.common.constants;

import com.ankamagames.framework.external.*;

public enum ExchangeMachineSort implements ExportableEnum
{
    BY_COST((byte)0, "Par co\u00fbt"), 
    BY_AGT_ORDER((byte)1, "Selon l'ordre d'AGT");
    
    private final byte m_id;
    private final String m_label;
    
    private ExchangeMachineSort(final byte id, final String label) {
        this.m_id = id;
        this.m_label = label;
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
    
    public static ExchangeMachineSort getById(final byte id) {
        for (final ExchangeMachineSort sort : values()) {
            if (sort.m_id == id) {
                return sort;
            }
        }
        return null;
    }
}
