package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.util.*;

public class GameDate implements GameDateConst
{
    private static final Logger m_logger;
    public static final TimeZone DEFAULT_TZ;
    private static final GregorianCalendar CALENDAR;
    public static final long NULL_DATE_REPRESENTATION = 0L;
    public static final GameDateConst NULL_DATE;
    private int m_seconds;
    private int m_minutes;
    private int m_hours;
    private int m_day;
    private int m_month;
    private int m_year;
    private long m_longRepresentation;
    
    private GameDate() {
        super();
    }
    
    public GameDate(final GameDateConst date) {
        super();
        if (date != null) {
            this.set(date);
        }
        else {
            this.set(GameDate.NULL_DATE);
        }
    }
    
    public GameDate(final int seconds, final int minutes, final int hours, final int day, final int month, final int year) {
        super();
        this.set(seconds, minutes, hours, day, month, year);
    }
    
    public void set(final long longRepresentation) {
        if (longRepresentation == 0L) {
            this.makeNull();
            return;
        }
        synchronized (GameDate.CALENDAR) {
            GameDate.CALENDAR.clear();
            GameDate.CALENDAR.setTimeZone(GameDate.DEFAULT_TZ);
            GameDate.CALENDAR.setTimeInMillis(longRepresentation);
            this.m_seconds = GameDate.CALENDAR.get(13);
            this.m_minutes = GameDate.CALENDAR.get(12);
            this.m_hours = GameDate.CALENDAR.get(11);
            this.m_day = GameDate.CALENDAR.get(5);
            this.m_month = GameDate.CALENDAR.get(2) + 1;
            if (GameDate.CALENDAR.get(0) == 1) {
                this.m_year = GameDate.CALENDAR.get(1);
            }
            else {
                this.m_year = 1 - GameDate.CALENDAR.get(1);
            }
            this.m_longRepresentation = GameDate.CALENDAR.getTimeInMillis();
        }
    }
    
    public void set(@NotNull final GameDateConst date) {
        if (date.isNull()) {
            this.makeNull();
        }
        else {
            this.set(date.getSeconds(), date.getMinutes(), date.getHours(), date.getDay(), date.getMonth(), date.getYear());
        }
    }
    
    public void set(final int s, final int min, final int h, final int d, final int m, final int y) {
        this.m_year = y;
        this.m_month = m;
        this.m_day = d;
        this.m_hours = h;
        this.m_minutes = min;
        this.m_seconds = s;
        this.normalize();
    }
    
    @Override
    public void normalize() {
        synchronized (GameDate.CALENDAR) {
            GameDate.CALENDAR.clear();
            GameDate.CALENDAR.setTimeZone(GameDate.DEFAULT_TZ);
            GameDate.CALENDAR.set(this.m_year, this.m_month - 1, this.m_day, this.m_hours, this.m_minutes, this.m_seconds);
            this.m_longRepresentation = GameDate.CALENDAR.getTimeInMillis();
            this.m_seconds = GameDate.CALENDAR.get(13);
            this.m_minutes = GameDate.CALENDAR.get(12);
            this.m_hours = GameDate.CALENDAR.get(11);
            this.m_day = GameDate.CALENDAR.get(5);
            this.m_month = GameDate.CALENDAR.get(2) + 1;
            if (GameDate.CALENDAR.get(0) == 1) {
                this.m_year = GameDate.CALENDAR.get(1);
            }
            else {
                this.m_year = 1 - GameDate.CALENDAR.get(1);
            }
        }
    }
    
    public static GameDate getNullDate() {
        final GameDate d = new GameDate();
        d.makeNull();
        return d;
    }
    
    public static void setTimeZone(final TimeZone tz) {
        GameDate.CALENDAR.setTimeZone(tz);
    }
    
    @Override
    public boolean isNull() {
        return this.m_longRepresentation == 0L;
    }
    
    private void makeNull() {
        this.m_longRepresentation = 0L;
        this.m_year = GameDate.NULL_DATE.getYear();
        this.m_month = GameDate.NULL_DATE.getMonth();
        this.m_day = GameDate.NULL_DATE.getDay();
        this.m_hours = GameDate.NULL_DATE.getHours();
        this.m_minutes = GameDate.NULL_DATE.getMinutes();
        this.m_seconds = GameDate.NULL_DATE.getSeconds();
    }
    
    @Override
    public boolean before(@NotNull final GameDateConst d) {
        return this.isNull() || this.compareTo(d) < 0;
    }
    
    @Override
    public boolean beforeOrEquals(@NotNull final GameDateConst d) {
        return this.isNull() || this.compareTo(d) <= 0;
    }
    
    @Override
    public boolean after(@NotNull final GameDateConst d) {
        return !this.isNull() && this.compareTo(d) > 0;
    }
    
    @Override
    public boolean afterOrEquals(@NotNull final GameDateConst d) {
        return !this.isNull() && this.compareTo(d) >= 0;
    }
    
    @Override
    public int compareTo(final GameDateConst d) {
        if (this.isNull()) {
            return d.isNull() ? 0 : -1;
        }
        if (d.isNull()) {
            return 1;
        }
        if (this.m_year > d.getYear()) {
            return 1;
        }
        if (this.m_year < d.getYear()) {
            return -1;
        }
        if (this.m_month > d.getMonth()) {
            return 1;
        }
        if (this.m_month < d.getMonth()) {
            return -1;
        }
        if (this.m_day > d.getDay()) {
            return 1;
        }
        if (this.m_day < d.getDay()) {
            return -1;
        }
        if (this.m_hours > d.getHours()) {
            return 1;
        }
        if (this.m_hours < d.getHours()) {
            return -1;
        }
        if (this.m_minutes > d.getMinutes()) {
            return 1;
        }
        if (this.m_minutes < d.getMinutes()) {
            return -1;
        }
        if (this.m_seconds > d.getSeconds()) {
            return 1;
        }
        if (this.m_seconds < d.getSeconds()) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final GameDate gameDate = (GameDate)o;
        if (this.isNull()) {
            return gameDate.isNull();
        }
        return this.m_day == gameDate.m_day && this.m_hours == gameDate.m_hours && this.m_minutes == gameDate.m_minutes && this.m_month == gameDate.m_month && this.m_seconds == gameDate.m_seconds && this.m_year == gameDate.m_year;
    }
    
    public GameDate add(@NotNull final GameIntervalConst intervalConst) {
        if (this.isNull()) {
            return this;
        }
        this.add(intervalConst.getSeconds(), intervalConst.getMinutes(), intervalConst.getHours(), intervalConst.getDays());
        return this;
    }
    
    public GameDate sub(@NotNull final GameIntervalConst intervalConst) {
        if (this.isNull()) {
            return this;
        }
        this.sub(intervalConst.getSeconds(), intervalConst.getMinutes(), intervalConst.getHours(), intervalConst.getDays());
        return this;
    }
    
    public GameDateConst add(@NotNull final EventPeriod period) {
        return this.add(period.getSeconds(), period.getMinutes(), period.getHours(), period.getDays(), period.getMonths(), period.getYears());
    }
    
    public GameDateConst sub(@NotNull final EventPeriod period) {
        return this.sub(period.getSeconds(), period.getMinutes(), period.getHours(), period.getDays(), period.getMonths(), period.getYears());
    }
    
    @Override
    public GameDateConst daysAgo(final int count) {
        final GameDate date = new GameDate(this);
        date.sub(0, 0, 0, count);
        return date;
    }
    
    @Override
    public GameDateConst yesterday() {
        return this.daysAgo(1);
    }
    
    @Override
    public GameDateConst daysLater(final int count) {
        final GameDate date = new GameDate(this);
        date.add(0, 0, 0, count);
        return date;
    }
    
    @Override
    public GameDateConst tomorrow() {
        return this.daysLater(1);
    }
    
    @Override
    public GameInterval timeTo(final GameDateConst dateConst) {
        assert !this.isNull() : "Onessaye de calculer une diff\u00e9rence \u00e0 partir d'une date nulle.";
        assert !dateConst.isNull() : "On essaye de calculer une diff\u00e9rence avec une date nulle";
        this.normalize();
        dateConst.normalize();
        final long delta = (dateConst.toLong() - this.toLong()) / 1000L;
        return new GameInterval(delta);
    }
    
    @Override
    public GameDateConst closestDatePeriod(final GameDateConst startTime, final GameIntervalConst period) {
        return this.closestDatePeriod(startTime, period, false);
    }
    
    @Override
    public GameDateConst closestDatePeriod(final GameDateConst startTime, final GameIntervalConst period, final boolean after) {
        assert !this.isNull() : "On essaye de calculer une date \u00e0 partir d'une date nulle.";
        assert !startTime.isNull() : "On essaye de calculer une date avec une date nulle";
        assert !period.isEmpty() : "On essaye de calculer une date avec un interval nul";
        final GameInterval timeToThis = startTime.timeTo(this);
        final int positiveDelta = timeToThis.isPositive() ? 0 : -1;
        final int afterDelta = after ? 1 : 0;
        final GameInterval period2 = new GameInterval(period);
        final int numPeriods = timeToThis.getDivisionResult(period) + positiveDelta + afterDelta;
        period2.multiply(numPeriods);
        final GameDate closestDate = new GameDate(startTime);
        closestDate.add(period2);
        return closestDate;
    }
    
    public int timeToInDays(final GameDateConst dateConst) {
        final GameDate date = new GameDate(0, 0, 0, this.m_day, this.m_month, this.m_year);
        final GameDate date2 = new GameDate(0, 0, 0, dateConst.getDay(), dateConst.getMonth(), dateConst.getYear());
        final GameInterval interval = new GameInterval((date2.toLong() - date.toLong()) / 1000L);
        int days = interval.getDays();
        if (interval.getHours() > 12) {
            ++days;
        }
        return days;
    }
    
    public GameDate add(final int s, final int min, final int h, final int d, final int m, final int y) {
        if (this.isNull()) {
            return this;
        }
        this.m_seconds += s;
        this.m_minutes += min;
        this.m_hours += h;
        this.m_day += d;
        this.m_month += m;
        this.m_year += y;
        this.normalize();
        return this;
    }
    
    public GameDate add(final int s, final int min, final int h, final int d) {
        return this.add(s, min, h, d, 0, 0);
    }
    
    public GameDate addMonthWithoutCheck(final int amount) {
        this.m_month += amount;
        this.normalize();
        return this;
    }
    
    public GameDate addYearWithoutCheck(final int amount) {
        this.m_year += amount;
        this.normalize();
        return this;
    }
    
    public GameDate sub(final int s, final int min, final int h, final int d) {
        if (this.isNull()) {
            return this;
        }
        this.m_seconds -= s;
        this.m_minutes -= min;
        this.m_hours -= h;
        this.m_day -= d;
        this.normalize();
        return this;
    }
    
    public GameDate sub(final int s, final int min, final int h, final int d, final int m, final int y) {
        if (this.isNull()) {
            return this;
        }
        this.m_seconds -= s;
        this.m_minutes -= min;
        this.m_hours -= h;
        this.m_day -= d;
        this.m_month -= m;
        this.m_year -= y;
        this.normalize();
        return this;
    }
    
    public void trimToDay() {
        if (this.isNull()) {
            return;
        }
        this.m_hours = 0;
        this.m_minutes = 0;
        this.m_seconds = 0;
        this.normalize();
    }
    
    public void trimToHour() {
        if (this.isNull()) {
            return;
        }
        this.m_minutes = 0;
        this.m_seconds = 0;
        this.normalize();
    }
    
    public int get(final int field) {
        synchronized (GameDate.CALENDAR) {
            GameDate.CALENDAR.setTimeInMillis(this.m_longRepresentation);
            return GameDate.CALENDAR.get(field);
        }
    }
    
    public static GameDate fromJavaDate(final Date date) {
        synchronized (GameDate.CALENDAR) {
            GameDate.CALENDAR.clear();
            GameDate.CALENDAR.setTimeZone(GameDate.DEFAULT_TZ);
            GameDate.CALENDAR.setTime(date);
            int year;
            if (GameDate.CALENDAR.get(0) == 1) {
                year = GameDate.CALENDAR.get(1);
            }
            else {
                year = 1 - GameDate.CALENDAR.get(1);
            }
            return new GameDate(GameDate.CALENDAR.get(13), GameDate.CALENDAR.get(12), GameDate.CALENDAR.get(11), GameDate.CALENDAR.get(5), GameDate.CALENDAR.get(2) + 1, year);
        }
    }
    
    public static GameDate fromLong(final long timeMilli) {
        synchronized (GameDate.CALENDAR) {
            GameDate.CALENDAR.clear();
            GameDate.CALENDAR.setTimeZone(GameDate.DEFAULT_TZ);
            GameDate.CALENDAR.setTimeInMillis(timeMilli);
            int year;
            if (GameDate.CALENDAR.get(0) == 1) {
                year = GameDate.CALENDAR.get(1);
            }
            else {
                year = 1 - GameDate.CALENDAR.get(1);
            }
            return new GameDate(GameDate.CALENDAR.get(13), GameDate.CALENDAR.get(12), GameDate.CALENDAR.get(11), GameDate.CALENDAR.get(5), GameDate.CALENDAR.get(2) + 1, year);
        }
    }
    
    @Override
    public long hourToLong() {
        assert !this.isNull() : "On essaye de calculer les heurs d'une date nulle";
        synchronized (GameDate.CALENDAR) {
            GameDate.CALENDAR.clear();
            GameDate.CALENDAR.setTimeZone(GameDate.DEFAULT_TZ);
            GameDate.CALENDAR.set(this.m_year, this.m_month - 1, this.m_day, this.m_hours, 0, 0);
            return GameDate.CALENDAR.getTimeInMillis();
        }
    }
    
    @Override
    public long dayToLong() {
        assert !this.isNull() : "On essaye de calculer les jours d'une date nulle";
        synchronized (GameDate.CALENDAR) {
            GameDate.CALENDAR.clear();
            GameDate.CALENDAR.setTimeZone(GameDate.DEFAULT_TZ);
            GameDate.CALENDAR.set(this.m_year, this.m_month - 1, this.m_day, 0, 0, 0);
            return GameDate.CALENDAR.getTimeInMillis();
        }
    }
    
    @Override
    public long toLong() {
        return this.m_longRepresentation;
    }
    
    @Override
    public Date toJavaDate() {
        assert !this.isNull() : "On essaye de convertir une date nulle";
        synchronized (GameDate.CALENDAR) {
            GameDate.CALENDAR.clear();
            GameDate.CALENDAR.setTimeZone(GameDate.DEFAULT_TZ);
            GameDate.CALENDAR.set(this.m_year, this.m_month - 1, this.m_day, this.m_hours, this.m_minutes, this.m_seconds);
            return GameDate.CALENDAR.getTime();
        }
    }
    
    @Override
    public int getDay() {
        return this.m_day;
    }
    
    @Override
    public int getHours() {
        return this.m_hours;
    }
    
    @Override
    public int getMinutes() {
        return this.m_minutes;
    }
    
    @Override
    public int getMonth() {
        return this.m_month;
    }
    
    @Override
    public int getSeconds() {
        return this.m_seconds;
    }
    
    @Override
    public int getYear() {
        return this.m_year;
    }
    
    public void setSeconds(final int seconds) {
        this.m_seconds = seconds;
        this.normalize();
    }
    
    public void setMinutes(final int minutes) {
        this.m_minutes = minutes;
        this.normalize();
    }
    
    public void setHours(final int hours) {
        this.m_hours = hours;
        this.normalize();
    }
    
    public void setDay(final int day) {
        this.m_day = day;
        this.normalize();
    }
    
    public void setMonth(final int month) {
        this.m_month = month;
        this.normalize();
    }
    
    public void setYear(final int year) {
        this.m_year = year;
        this.normalize();
    }
    
    public void set() {
    }
    
    @Override
    public String toString() {
        if (this.isNull()) {
            return "Date{ nulle }";
        }
        final int gmtOffset = GameDate.CALENDAR.getTimeZone().getRawOffset() / 3600000;
        if (gmtOffset >= 0) {
            return GameDateFormatter.format("{Date : %d/%M/%Y %h:%m:%s UTC+" + gmtOffset + "}", this);
        }
        return GameDateFormatter.format("{Date : %d/%M/%Y %h:%m:%s UTC" + gmtOffset + "}", this);
    }
    
    @Override
    public String toDescriptionString() {
        if (this.isNull()) {
            return "null date";
        }
        return GameDateFormatter.format("%d/%M/%Y %hH%mmin", this);
    }
    
    static {
        m_logger = Logger.getLogger((Class)GameDate.class);
        DEFAULT_TZ = TimeZone.getTimeZone("UTC");
        CALENDAR = new GregorianCalendar(GameDate.DEFAULT_TZ);
        GameDate.m_logger.info((Object)("GameDate initialized. Timezone : " + GameDate.CALENDAR.getTimeZone()));
        NULL_DATE = fromLong(0L);
    }
}
