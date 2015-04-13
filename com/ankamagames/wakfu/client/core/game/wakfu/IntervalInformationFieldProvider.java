package com.ankamagames.wakfu.client.core.game.wakfu;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class IntervalInformationFieldProvider implements FieldProvider
{
    private static final String VALUE_MIN_FIELD = "valueMin";
    private static final String VALUE_MAX_FIELD = "valueMax";
    private static final String INTERVAL_TEXT_FIELD = "intervalText";
    private final int m_intervalMinValue;
    private final int m_intervalMaxValue;
    private final int m_baseMaxValue;
    private final String[] FIELDS;
    
    public IntervalInformationFieldProvider(final Interval interval, final int baseMaxValue) {
        super();
        this.FIELDS = new String[] { "valueMin", "valueMax", "intervalText" };
        this.m_intervalMinValue = interval.getMin();
        this.m_intervalMaxValue = interval.getMax();
        this.m_baseMaxValue = baseMaxValue;
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("valueMin")) {
            return this.m_intervalMinValue / this.m_baseMaxValue;
        }
        if (fieldName.equals("valueMax")) {
            return this.m_intervalMaxValue / this.m_baseMaxValue;
        }
        if (!fieldName.equals("intervalText")) {
            return null;
        }
        if (this.m_intervalMinValue > 100 && this.m_intervalMaxValue > 100) {
            return UIEcosystemUtils.getRoundedEcosystemValue(this.m_intervalMinValue) + " - " + UIEcosystemUtils.getRoundedEcosystemValue(this.m_intervalMaxValue);
        }
        return this.m_intervalMinValue + " - " + this.m_intervalMaxValue;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public int getIntervalMinValue() {
        return this.m_intervalMinValue;
    }
    
    public int getIntervalMaxValue() {
        return this.m_intervalMaxValue;
    }
    
    public boolean isInInterval(final int value) {
        return value <= this.m_intervalMaxValue && value >= this.m_intervalMinValue;
    }
    
    @Override
    public String toString() {
        return "interval : " + this.m_intervalMinValue + "-" + this.m_intervalMaxValue;
    }
}
