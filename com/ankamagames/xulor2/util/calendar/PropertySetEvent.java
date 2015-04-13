package com.ankamagames.xulor2.util.calendar;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.xulor2.property.*;

public class PropertySetEvent extends CalendarEvent
{
    private final String m_propertyName;
    private final Object m_value;
    
    public PropertySetEvent(final GameDateConst date, final String propertyName, final Object value) {
        super(date);
        this.m_propertyName = propertyName;
        this.m_value = value;
    }
    
    @Override
    public void runEvent(final GameCalendar calendar) {
        PropertiesProvider.getInstance().setPropertyValue(this.m_propertyName, this.m_value);
    }
}
