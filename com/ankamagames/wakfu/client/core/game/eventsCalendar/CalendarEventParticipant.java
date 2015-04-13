package com.ankamagames.wakfu.client.core.game.eventsCalendar;

import com.ankamagames.framework.reflect.*;

public class CalendarEventParticipant implements FieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String ID_FIELD = "id";
    private long m_id;
    private String m_name;
    
    public CalendarEventParticipant(final long id, final String name) {
        super();
        this.m_id = id;
        this.m_name = name;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public void setId(final long id) {
        this.m_id = id;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("id")) {
            return this.m_id;
        }
        return null;
    }
    
    @Override
    public String[] getFields() {
        return null;
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
