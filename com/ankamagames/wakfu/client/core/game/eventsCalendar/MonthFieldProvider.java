package com.ankamagames.wakfu.client.core.game.eventsCalendar;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;

public class MonthFieldProvider implements FieldProvider
{
    public static final String DESCRIPTION_FIELD = "description";
    public static final String VALUE_FIELD = "value";
    private int m_monthIndex;
    
    public MonthFieldProvider(final int month) {
        super();
        this.m_monthIndex = month;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("description")) {
            return WakfuTranslator.getInstance().getString("realMonth." + this.m_monthIndex);
        }
        if (fieldName.equals("value")) {
            return this.m_monthIndex;
        }
        return null;
    }
    
    public int getMonthIndex() {
        return this.m_monthIndex;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
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
}
