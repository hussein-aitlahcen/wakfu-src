package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class EventPeriod
{
    public static final EventPeriod EMPTY;
    public static final EventPeriod HOURLY;
    public static final EventPeriod DAILY;
    public static final EventPeriod WEEKLY;
    public static final EventPeriod MONTHLY;
    public static final EventPeriod YEARLY;
    private int m_years;
    private int m_months;
    private int m_days;
    private int m_hours;
    private int m_minutes;
    private int m_seconds;
    private long m_longRepresentation;
    
    public EventPeriod(final EventPeriod period) {
        super();
        this.m_years = period.m_years;
        this.m_months = period.m_months;
        this.m_days = period.m_days;
        this.m_hours = period.m_hours;
        this.m_minutes = period.m_minutes;
        this.m_seconds = period.m_seconds;
        this.updateLongRepresentation();
    }
    
    public EventPeriod(final GameIntervalConst interval) {
        super();
        this.m_years = 0;
        this.m_months = 0;
        this.m_days = interval.getDays();
        this.m_hours = interval.getHours();
        this.m_minutes = interval.getMinutes();
        this.m_seconds = interval.getSeconds();
        this.updateLongRepresentation();
    }
    
    public EventPeriod(final int years, final int months, final int days, final int hours, final int minutes, final int seconds) {
        super();
        this.m_years = years;
        this.m_months = months;
        this.m_days = days;
        this.m_hours = hours;
        this.m_minutes = minutes;
        this.m_seconds = seconds;
        this.updateLongRepresentation();
    }
    
    private void updateLongRepresentation() {
        this.m_longRepresentation = (this.m_seconds | this.m_minutes << 8 | this.m_hours << 16 | this.m_days << 24 | this.m_months << 32 | this.m_years << 40);
    }
    
    public int getYears() {
        return this.m_years;
    }
    
    public int getMonths() {
        return this.m_months;
    }
    
    public int getDays() {
        return this.m_days;
    }
    
    public int getHours() {
        return this.m_hours;
    }
    
    public int getMinutes() {
        return this.m_minutes;
    }
    
    public int getSeconds() {
        return this.m_seconds;
    }
    
    public long toLong() {
        return this.m_longRepresentation;
    }
    
    public static EventPeriod fromLong(final long value) {
        final int seconds = (int)(value & 0xFFL);
        long s = value >> 8;
        final int minutes = (int)(s & 0xFFL);
        s >>= 8;
        final int hours = (int)(s & 0xFFL);
        s >>= 8;
        final int days = (int)(s & 0xFFL);
        s >>= 8;
        final int months = (int)(s & 0xFFL);
        s >>= 8;
        final int years = (int)(s & 0xFFFFL);
        return new EventPeriod(years, months, days, hours, minutes, seconds);
    }
    
    @Override
    public String toString() {
        return ((this.m_years != 0) ? (this.m_years + " ans ") : " ") + ((this.m_months != 0) ? (this.m_months + " mois ") : "") + ((this.m_days != 0) ? (this.m_days + " jours ") : "") + ((this.m_hours != 0) ? (this.m_hours + " heures ") : "") + ((this.m_minutes != 0) ? (this.m_minutes + " minutes ") : "") + ((this.m_seconds != 0) ? (this.m_seconds + " secondes ") : "");
    }
    
    static {
        EMPTY = new EventPeriod(0, 0, 0, 0, 0, 0);
        HOURLY = new EventPeriod(0, 0, 0, 1, 0, 0);
        DAILY = new EventPeriod(0, 0, 1, 0, 0, 0);
        WEEKLY = new EventPeriod(0, 0, 7, 0, 0, 0);
        MONTHLY = new EventPeriod(0, 1, 0, 0, 0, 0);
        YEARLY = new EventPeriod(1, 0, 0, 0, 0, 0);
    }
}
