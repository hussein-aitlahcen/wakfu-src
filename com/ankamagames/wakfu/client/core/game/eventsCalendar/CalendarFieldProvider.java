package com.ankamagames.wakfu.client.core.game.eventsCalendar;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.xulor2.property.*;

public class CalendarFieldProvider implements FieldProvider
{
    public static final String CALENDAR_FIELD = "calendar";
    public static final String SELECTED_MONTH_FIELD = "selectedMonth";
    public static final String MONTHES_FIELD = "monthesList";
    public static final String SELECTED_YEAR_FIELD = "selectedYear";
    public static final String YEARS_FIELD = "yearsList";
    public static final String HOUR_FIELD = "hour";
    public static final String MINUTE_FIELD = "minute";
    private static final int FIRST_YEAR;
    private static final ArrayList<Integer> YEARS;
    private static final ArrayList<MonthFieldProvider> MONTHES;
    private Calendar m_calendar;
    private MonthFieldProvider m_selectedMonth;
    private Integer m_selectedYear;
    
    public CalendarFieldProvider() {
        super();
        this.m_calendar = new GregorianCalendar(WakfuTranslator.getInstance().getLanguage().getActualLocale());
        this.resetToFirstDayOfCurrentMonth();
    }
    
    public Calendar getCalendar() {
        return this.m_calendar;
    }
    
    public void resetToFirstDayOfCurrentMonth() {
        this.m_calendar.setTime(new Date());
        this.computeMonthesListForCurrentYear();
        this.m_selectedMonth = CalendarFieldProvider.MONTHES.get(this.m_calendar.get(2) + CalendarFieldProvider.MONTHES.size() - 12);
        final int i = CalendarFieldProvider.YEARS.indexOf(this.m_calendar.get(1));
        this.m_selectedYear = CalendarFieldProvider.YEARS.get(i);
    }
    
    public void setDate(final GameDateConst date) {
        this.setCurrentYear(date.getYear());
        this.setCurrentMonth(date.getMonth() - 1);
        this.setCurrentDay(date.getDay());
        this.setCurrentHour(date.getHours());
        this.setCurrentMinute(date.getMinutes());
    }
    
    public void setCurrentDay(final int day) {
        this.m_calendar.set(5, day);
    }
    
    public int getCurrentDay() {
        return this.m_calendar.get(5);
    }
    
    public void setCurrentMonth(final int month) {
        this.m_calendar.set(2, month);
        this.m_selectedMonth = CalendarFieldProvider.MONTHES.get(Math.max(month + CalendarFieldProvider.MONTHES.size() - 12, 0));
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "monthesList", "selectedMonth", "calendar");
    }
    
    public int getCurrentMonth() {
        return this.m_selectedMonth.getMonthIndex();
    }
    
    public void setCurrentYear(final int year) {
        this.m_calendar.set(1, year);
        final int i = CalendarFieldProvider.YEARS.indexOf(year);
        this.m_selectedYear = CalendarFieldProvider.YEARS.get(i);
        this.computeMonthesListForCurrentYear();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "monthesList", "selectedMonth", "selectedYear", "calendar");
    }
    
    public int getCurrentYear() {
        return this.m_selectedYear;
    }
    
    public void setCurrentHour(final int hour) {
        this.m_calendar.set(11, hour);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hour");
    }
    
    public int getCurrentHour() {
        return this.m_calendar.get(11);
    }
    
    public void setCurrentMinute(final int minute) {
        this.m_calendar.set(12, minute);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "minute");
    }
    
    public int getCurrentMinute() {
        return this.m_calendar.get(12);
    }
    
    private void computeMonthesListForCurrentYear() {
        int startMonth = 0;
        final Calendar todayCalendar = new GregorianCalendar();
        todayCalendar.setTime(new Date());
        if (todayCalendar.get(1) == this.m_calendar.get(1)) {
            startMonth = todayCalendar.get(2);
        }
        final int selectedMonthOffset = (this.m_selectedMonth != null) ? this.m_selectedMonth.getMonthIndex() : -1;
        CalendarFieldProvider.MONTHES.clear();
        for (int i = startMonth; i < 12; ++i) {
            CalendarFieldProvider.MONTHES.add(new MonthFieldProvider(i));
        }
        this.m_selectedMonth = null;
        if (selectedMonthOffset != -1) {
            for (int i = CalendarFieldProvider.MONTHES.size() - 1; i >= 0; --i) {
                final MonthFieldProvider month = CalendarFieldProvider.MONTHES.get(i);
                if (month.getMonthIndex() == selectedMonthOffset) {
                    this.m_selectedMonth = month;
                    break;
                }
            }
        }
        if (this.m_selectedMonth == null) {
            this.m_selectedMonth = CalendarFieldProvider.MONTHES.get(0);
        }
        this.setCurrentMonth(this.m_selectedMonth.getMonthIndex());
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("calendar")) {
            return this.m_calendar;
        }
        if (fieldName.equals("monthesList")) {
            return CalendarFieldProvider.MONTHES;
        }
        if (fieldName.equals("selectedMonth")) {
            return this.m_selectedMonth;
        }
        if (fieldName.equals("yearsList")) {
            return CalendarFieldProvider.YEARS;
        }
        if (fieldName.equals("selectedYear")) {
            return this.m_selectedYear;
        }
        if (fieldName.equals("hour")) {
            return this.m_calendar.get(11);
        }
        if (fieldName.equals("minute")) {
            return this.m_calendar.get(12);
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
    
    static {
        YEARS = new ArrayList<Integer>(3);
        MONTHES = new ArrayList<MonthFieldProvider>(12);
        final Calendar todayCalendar = new GregorianCalendar();
        todayCalendar.setTime(new Date());
        FIRST_YEAR = todayCalendar.get(1);
        for (int i = 0; i < 2; ++i) {
            CalendarFieldProvider.YEARS.add(CalendarFieldProvider.FIRST_YEAR + i);
        }
    }
}
