package com.ankamagames.wakfu.common.game.fight.time.buff;

import com.ankamagames.framework.external.*;

public enum TimelineBuffType implements ExportableEnum
{
    POSITIVE(1, "bonus positif"), 
    NEGATIVE(2, "bonus negatif");
    
    private final int m_id;
    private final String m_description;
    
    private TimelineBuffType(final int id, final String description) {
        this.m_id = id;
        this.m_description = description;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_description;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static TimelineBuffType getFromId(final int id) {
        final TimelineBuffType[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final TimelineBuffType value = values[i];
            if (value.m_id == id) {
                return value;
            }
        }
        return null;
    }
}
