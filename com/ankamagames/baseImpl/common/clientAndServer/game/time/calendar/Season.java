package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar;

import org.jetbrains.annotations.*;

public enum Season
{
    WINTER("Winter", new GameDateConst[] { new GameDate(0, 0, 0, 21, 12, 0), new GameDate(0, 0, 0, 31, 12, 0), new GameDate(0, 0, 0, 1, 1, 0), new GameDate(0, 0, 0, 20, 3, 0) }), 
    SPRING("Spring", new GameDateConst[] { new GameDate(0, 0, 0, 21, 3, 0), new GameDate(0, 0, 0, 20, 6, 0) }), 
    SUMMER("Summer", new GameDateConst[] { new GameDate(0, 0, 0, 21, 6, 0), new GameDate(0, 0, 0, 20, 9, 0) }), 
    FALL("Fall", new GameDateConst[] { new GameDate(0, 0, 0, 21, 9, 0), new GameDate(0, 0, 0, 20, 12, 0) });
    
    private static final Season[] m_seasons;
    private static final GameDate m_cleanDate;
    private final String m_name;
    private final GameDateConst[] m_intervals;
    private Season m_previous;
    private Season m_next;
    
    @Nullable
    public static Season fromDate(final GameDateConst date) {
        Season.m_cleanDate.set(0, 0, 0, date.getDay(), date.getMonth(), 0);
        for (final Season season : Season.m_seasons) {
            for (int k = season.m_intervals.length / 2, i = 0; i < k; ++i) {
                final GameDateConst begin = season.m_intervals[i * 2];
                final GameDateConst end = season.m_intervals[i * 2 + 1];
                if ((begin.equals(Season.m_cleanDate) || begin.before(Season.m_cleanDate)) && (end.after(Season.m_cleanDate) || end.equals(Season.m_cleanDate))) {
                    return season;
                }
            }
        }
        return null;
    }
    
    private Season(final String name, final GameDateConst[] intervals) {
        this.m_name = name;
        this.m_intervals = intervals;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public byte getIndex() {
        return (byte)(this.ordinal() + 1);
    }
    
    public GameDateConst getStartingDate() {
        return this.m_intervals[0];
    }
    
    public int daysFromStartingDate(final GameDateConst date) {
        Season.m_cleanDate.set(date.getSeconds(), date.getMinutes(), date.getHours(), date.getDay(), date.getMonth(), 0);
        final int k = this.m_intervals.length / 2;
        int days = 0;
        for (int i = 0; i < k; ++i) {
            final GameDateConst begin = this.m_intervals[i * 2];
            final GameDateConst end = this.m_intervals[i * 2 + 1];
            if ((begin.equals(Season.m_cleanDate) || begin.before(Season.m_cleanDate)) && (end.after(Season.m_cleanDate) || end.equals(Season.m_cleanDate))) {
                days += begin.timeTo(Season.m_cleanDate).getDays();
                break;
            }
            days += begin.timeTo(end).getDays();
        }
        return days;
    }
    
    private void setSeasonNeighbors(final Season previous, final Season next) {
        this.m_previous = previous;
        this.m_next = next;
    }
    
    public Season previous() {
        return this.m_previous;
    }
    
    public Season next() {
        return this.m_next;
    }
    
    @Override
    public String toString() {
        switch (this) {
            case SPRING: {
                return "Printemps";
            }
            case SUMMER: {
                return "Et\u00e9";
            }
            case FALL: {
                return "Automne";
            }
            case WINTER: {
                return "Hiver";
            }
            default: {
                return super.toString();
            }
        }
    }
    
    static {
        m_seasons = new Season[] { Season.WINTER, Season.SPRING, Season.SUMMER, Season.FALL };
        m_cleanDate = new GameDate(0, 0, 0, 0, 0, 0);
        Season.WINTER.setSeasonNeighbors(Season.FALL, Season.SPRING);
        Season.SPRING.setSeasonNeighbors(Season.WINTER, Season.SUMMER);
        Season.SUMMER.setSeasonNeighbors(Season.SPRING, Season.FALL);
        Season.FALL.setSeasonNeighbors(Season.SUMMER, Season.WINTER);
    }
}
