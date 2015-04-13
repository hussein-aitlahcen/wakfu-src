package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar;

import org.jetbrains.annotations.*;

public class GameInterval implements GameIntervalConst
{
    private int m_seconds;
    private int m_minutes;
    private int m_hours;
    private int m_days;
    private long m_internalSeconds;
    
    public GameInterval(final long longRepresentation) {
        super();
        this.m_internalSeconds = longRepresentation;
        this.updateUnits();
    }
    
    public GameInterval(final GameIntervalConst gd) {
        super();
        this.m_seconds = gd.getSeconds();
        this.m_minutes = gd.getMinutes();
        this.m_hours = gd.getHours();
        this.m_days = gd.getDays();
        this.updateInternalSeconds();
        this.updateUnits();
    }
    
    public GameInterval(final int seconds, final int minutes, final int hours, final int days) {
        super();
        this.m_seconds = seconds;
        this.m_minutes = minutes;
        this.m_hours = hours;
        this.m_days = days;
        this.updateInternalSeconds();
        this.updateUnits();
    }
    
    public void set(final GameIntervalConst interval) {
        if (interval == null) {
            this.set(GameInterval.EMPTY_INTERVAL);
            return;
        }
        this.m_seconds = interval.getSeconds();
        this.m_minutes = interval.getMinutes();
        this.m_hours = interval.getHours();
        this.m_days = interval.getDays();
        this.m_internalSeconds = interval.toSeconds();
    }
    
    public void set(final int seconds, final int minutes, final int hours, final int days) {
        this.m_seconds = seconds;
        this.m_minutes = minutes;
        this.m_hours = hours;
        this.m_days = days;
        this.updateInternalSeconds();
        this.updateUnits();
    }
    
    private void updateUnits() {
        long seconds = this.m_internalSeconds;
        this.m_days = (int)(seconds / 86400L);
        seconds -= this.m_days * 3600 * 24;
        this.m_hours = (int)(seconds / 3600L);
        seconds -= this.m_hours * 3600;
        this.m_minutes = (int)(seconds / 60L);
        seconds -= this.m_minutes * 60;
        this.m_seconds = (int)seconds;
    }
    
    private void updateInternalSeconds() {
        this.m_internalSeconds = this.m_seconds + this.m_minutes * 60 + this.m_hours * 3600 + this.m_days * 3600 * 24;
    }
    
    @Override
    public int getSeconds() {
        return this.m_seconds;
    }
    
    @Override
    public int getMinutes() {
        return this.m_minutes;
    }
    
    @Override
    public int getHours() {
        return this.m_hours;
    }
    
    @Override
    public int getDays() {
        return this.m_days;
    }
    
    @Override
    public boolean isPositive() {
        return this.m_internalSeconds > 0L;
    }
    
    @Override
    public boolean isEmpty() {
        return this.m_internalSeconds == 0L;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this || o instanceof GameIntervalConst) {
            final GameIntervalConst gi = (GameIntervalConst)o;
            return this.toSeconds() == gi.toSeconds();
        }
        return false;
    }
    
    @Override
    public boolean greaterThan(@NotNull final GameIntervalConst interval) {
        return this.m_internalSeconds > interval.toSeconds();
    }
    
    @Override
    public boolean lowerThan(@NotNull final GameIntervalConst interval) {
        return this.m_internalSeconds < interval.toSeconds();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{Interval: ");
        sb.append(this.m_days).append("d ");
        sb.append(this.m_hours).append(":").append(this.m_minutes).append(';').append(this.m_seconds);
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public long toSeconds() {
        return this.m_internalSeconds;
    }
    
    @Override
    public long toLong() {
        return this.m_internalSeconds * 1000L;
    }
    
    public static GameInterval fromSeconds(final long timeSeconds) {
        return new GameInterval(timeSeconds);
    }
    
    public static GameInterval fromLong(final long timeMillis) {
        return new GameInterval(timeMillis / 1000L);
    }
    
    @Override
    public int getDivisionResult(final GameIntervalConst divider) {
        if (this.isEmpty()) {
            return 0;
        }
        if (divider.isEmpty()) {
            throw new ArithmeticException("/ by zero");
        }
        final long thisSecs = this.m_internalSeconds;
        final long dividerSecs = divider.toSeconds();
        if (dividerSecs == 0L) {
            throw new ArithmeticException("/ by zero");
        }
        return (int)(thisSecs / dividerSecs);
    }
    
    public void add(final GameIntervalConst duration) {
        if (duration == null) {
            return;
        }
        this.m_internalSeconds += duration.toSeconds();
        this.updateUnits();
    }
    
    public void substract(final GameIntervalConst duration) {
        if (duration == null) {
            return;
        }
        this.m_internalSeconds -= duration.toSeconds();
        this.updateUnits();
    }
    
    public void multiply(final int multiplier) {
        this.m_internalSeconds *= multiplier;
        this.updateUnits();
    }
    
    public void multiply(final float multiplier) {
        this.m_internalSeconds *= (long)multiplier;
        this.updateUnits();
    }
}
