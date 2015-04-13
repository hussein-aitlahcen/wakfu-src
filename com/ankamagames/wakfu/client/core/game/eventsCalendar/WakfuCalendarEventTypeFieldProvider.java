package com.ankamagames.wakfu.client.core.game.eventsCalendar;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;

public class WakfuCalendarEventTypeFieldProvider implements FieldProvider
{
    public static final String DESCRIPTION_FIELD = "description";
    public static final String VALUE_FIELD = "value";
    private byte m_type;
    private static WakfuCalendarEventTypeFieldProvider[] m_types;
    
    public static WakfuCalendarEventTypeFieldProvider[] getTypes() {
        return WakfuCalendarEventTypeFieldProvider.m_types;
    }
    
    public static WakfuCalendarEventTypeFieldProvider getByType(final byte type) {
        for (final WakfuCalendarEventTypeFieldProvider fp : WakfuCalendarEventTypeFieldProvider.m_types) {
            if (fp.m_type == type) {
                return fp;
            }
        }
        return null;
    }
    
    public WakfuCalendarEventTypeFieldProvider(final byte type) {
        super();
        this.m_type = type;
    }
    
    public byte getType() {
        return this.m_type;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("description")) {
            return WakfuTranslator.getInstance().getString("calendar.event.type." + this.m_type);
        }
        if (fieldName.equals("value")) {
            return String.valueOf(this.m_type);
        }
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
    
    static {
        final ArrayList<WakfuCalendarEventTypeFieldProvider> types = new ArrayList<WakfuCalendarEventTypeFieldProvider>();
        types.add(new WakfuCalendarEventTypeFieldProvider((byte)1));
        types.add(new WakfuCalendarEventTypeFieldProvider((byte)2));
        types.add(new WakfuCalendarEventTypeFieldProvider((byte)3));
        WakfuCalendarEventTypeFieldProvider.m_types = new WakfuCalendarEventTypeFieldProvider[types.size()];
        WakfuCalendarEventTypeFieldProvider.m_types = types.toArray(WakfuCalendarEventTypeFieldProvider.m_types);
    }
}
