package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar;

import com.ankamagames.framework.kernel.core.translator.*;

public class TimeUtils
{
    public static String getVeryShortDescription(final GameIntervalConst interval) {
        return Translator.getInstance().getString("durationFormat.yearMonthDayHourMinuteSecond.veryShort", 0, 0, interval.getDays(), interval.getHours(), interval.getMinutes(), interval.getSeconds());
    }
    
    public static String getVeryShortDescriptionWithLongUnits(final GameIntervalConst interval) {
        return Translator.getInstance().getString("durationFormat.yearMonthDayHourMinuteSecond.veryShortWithLongUnits", 0, 0, interval.getDays(), interval.getHours(), interval.getMinutes(), interval.getSeconds());
    }
    
    public static String getShortDescription(final GameIntervalConst interval) {
        return Translator.getInstance().getString("durationFormat.yearMonthDayHourMinuteSecond.short", 0, 0, interval.getDays(), interval.getHours(), interval.getMinutes(), interval.getSeconds());
    }
    
    public static String getLongDescription(final GameIntervalConst interval) {
        return Translator.getInstance().getString("dateFormat.yearMonthDayHourMinuteSecond", 0, 0, interval.getDays(), interval.getHours(), interval.getMinutes(), interval.getSeconds());
    }
}
