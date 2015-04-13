package com.ankamagames.wakfu.client.core.game.eventsCalendar;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;

public class CalendarEventFormatter
{
    private static final Logger m_logger;
    
    public static String formatTitle(@NotNull final WakfuCalendarEvent event) {
        if (event.areTextsUserDefined()) {
            return event.getTitle();
        }
        final StringBuilder keyBuilder = new StringBuilder("calendar.events.predefined.title.").append(event.getType()).append('.').append(event.getSubType());
        return WakfuTranslator.getInstance().getString(keyBuilder.toString());
    }
    
    public static String formatDescription(@NotNull final WakfuCalendarEvent event) {
        if (event.areTextsUserDefined()) {
            return event.getDescription();
        }
        final StringBuilder keyBuilder = new StringBuilder("calendar.events.predefined.desc.").append(event.getType()).append('.').append(event.getSubType());
        return WakfuTranslator.getInstance().getString(keyBuilder.toString());
    }
    
    static {
        m_logger = Logger.getLogger((Class)CalendarEventFormatter.class);
    }
}
