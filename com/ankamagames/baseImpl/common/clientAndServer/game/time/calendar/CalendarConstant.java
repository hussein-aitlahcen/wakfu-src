package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar;

import java.util.regex.*;

public interface CalendarConstant
{
    public static final Pattern FORMAT_PATTERN = Pattern.compile("[^%]*%[yYMdhms]([^%]+%[yYMdhms])*[^%]*");
    public static final Pattern FORMAT_VAR_DECLARATION_PATTERN = Pattern.compile("%");
    public static final int NUMBER_OF_SEASON_PER_YEAR = 4;
    public static final int NUMBER_OF_MONTH_PER_YEAR = 12;
    public static final int NUMBER_OF_DAY_PER_MONTH = 30;
    public static final int NUMBER_OF_HOURS_PER_DAY = 24;
    public static final int NUMBER_OF_MINUTE_PER_HOUR = 60;
    public static final int NUMBER_OF_SECOND_PER_MINUTE = 60;
    public static final int NUMBER_OF_SECOND_PER_DAY = 86400;
    public static final int NUMBER_OF_MILLISECONDS_PER_SECOND = 1000;
    public static final long NUMBER_OF_MILLISECONDS_PER_MINUTE = 60000L;
    public static final long NUMBER_OF_MILLISECONDS_PER_HOUR = 3600000L;
    public static final long NUMBER_OF_MILLISECONDS_PER_DAY = 86400000L;
    public static final long NUMBER_OF_MILLISECONDS_PER_MONTH = 2592000000L;
    public static final long NUMBER_OF_MILLISECONDS_PER_YEAR = 31104000000L;
    public static final long NUMBER_OF_NANO_PER_MILLISECOND = 1000000L;
    public static final int NUMBER_OF_DAY_PER_YEAR = 360;
    public static final short REFERENCE_YEAR = 1970;
    public static final int SEASON_SPRING = 1;
    public static final int SEASON_SUMMER = 2;
    public static final int SEASON_AUTUMN = 3;
    public static final int SEASON_WINTER = 4;
}
