package com.ankamagames.wakfu.client.core.game.eventsCalendar;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;

public class ClientEventsCalendarView implements FieldProvider
{
    public static final String CALENDAR_FIELD = "calendar";
    public static final String EVENTS_FOR_MONTH_FIELD = "eventsForMonth";
    private CharacterEventsCalendar m_calendar;
    private CalendarWidget m_widget;
    private CalendarFieldProvider m_calendarFieldProvider;
    private ArrayList<DayEvents> m_eventsForMonth;
    private static ClientEventsCalendarView m_instance;
    
    private ClientEventsCalendarView() {
        super();
        this.m_calendarFieldProvider = new CalendarFieldProvider();
    }
    
    public static ClientEventsCalendarView getInstance() {
        return ClientEventsCalendarView.m_instance;
    }
    
    public void setCalendar(final CharacterEventsCalendar calendar) {
        this.m_calendar = calendar;
    }
    
    public CalendarWidget getWidget() {
        return this.m_widget;
    }
    
    private Calendar getCurrentCalendar() {
        return this.m_calendarFieldProvider.getCalendar();
    }
    
    public void setWidget(final CalendarWidget widget) {
        this.m_widget = widget;
        this.m_calendarFieldProvider.resetToFirstDayOfCurrentMonth();
        this.m_widget.setCalendar(this.getCurrentCalendar());
        this.fireEventsListChanged();
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("calendar")) {
            return this.m_calendarFieldProvider;
        }
        if (fieldName.equals("eventsForMonth")) {
            return this.m_eventsForMonth;
        }
        return null;
    }
    
    public void setCurrentMonth(final int month) {
        this.m_calendarFieldProvider.setCurrentMonth(month);
        this.m_widget.setCalendar(this.getCurrentCalendar());
        this.fireEventsListChanged();
    }
    
    public void setCurrentYear(final int year) {
        this.m_calendarFieldProvider.setCurrentYear(year);
        this.m_widget.setCalendar(this.getCurrentCalendar());
        this.fireEventsListChanged();
    }
    
    public int getCurrentDay() {
        if (this.m_widget != null) {
            return this.m_widget.getDayOver();
        }
        return 1;
    }
    
    public int getCurrentMonth() {
        if (this.m_widget != null) {
            return this.m_calendarFieldProvider.getCurrentMonth();
        }
        return 1;
    }
    
    public int getCurrentYear() {
        if (this.m_widget != null) {
            return this.m_calendarFieldProvider.getCurrentYear();
        }
        return 1;
    }
    
    public void fireEventsListChanged() {
        if (this.m_widget != null) {
            this.m_eventsForMonth = this.getEventsForCurrentMonth();
            this.m_widget.setContent(this.m_eventsForMonth);
        }
    }
    
    private ArrayList<DayEvents> getEventsForCurrentMonth() {
        final ArrayList<DayEvents> events = new ArrayList<DayEvents>();
        final GameDate date = new GameDate(0, 0, 0, 1, this.m_calendarFieldProvider.getCurrentMonth() + 1, this.m_calendarFieldProvider.getCurrentYear());
        for (int lastDay = this.getCurrentCalendar().getActualMaximum(5), i = 1; i <= lastDay; ++i) {
            final Set<WakfuCalendarEvent> eventsByDate = this.m_calendar.getEventsByDate(date);
            if (eventsByDate != null && !eventsByDate.isEmpty()) {
                final DayEvents dayEvents = new DayEvents(i, eventsByDate);
                events.add(dayEvents);
            }
            date.add(GameIntervalConst.DAY_INTERVAL);
        }
        return events;
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
    
    static {
        ClientEventsCalendarView.m_instance = new ClientEventsCalendarView();
    }
}
