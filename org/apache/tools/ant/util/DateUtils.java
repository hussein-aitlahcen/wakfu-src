package org.apache.tools.ant.util;

import java.util.*;
import java.text.*;

public final class DateUtils
{
    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60;
    private static final int ONE_HOUR = 60;
    private static final int TEN = 10;
    public static final String ISO8601_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String ISO8601_DATE_PATTERN = "yyyy-MM-dd";
    public static final String ISO8601_TIME_PATTERN = "HH:mm:ss";
    public static final DateFormat DATE_HEADER_FORMAT;
    private static final DateFormat DATE_HEADER_FORMAT_INT;
    private static final MessageFormat MINUTE_SECONDS;
    private static final double[] LIMITS;
    private static final String[] MINUTES_PART;
    private static final String[] SECONDS_PART;
    private static final ChoiceFormat MINUTES_FORMAT;
    private static final ChoiceFormat SECONDS_FORMAT;
    
    public static String format(final long date, final String pattern) {
        return format(new Date(date), pattern);
    }
    
    public static String format(final Date date, final String pattern) {
        final DateFormat df = createDateFormat(pattern);
        return df.format(date);
    }
    
    public static String formatElapsedTime(final long millis) {
        final long seconds = millis / 1000L;
        final long minutes = seconds / 60L;
        final Object[] args = { new Long(minutes), new Long(seconds % 60L) };
        return DateUtils.MINUTE_SECONDS.format(args);
    }
    
    private static DateFormat createDateFormat(final String pattern) {
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        final TimeZone gmt = TimeZone.getTimeZone("GMT");
        sdf.setTimeZone(gmt);
        sdf.setLenient(true);
        return sdf;
    }
    
    public static int getPhaseOfMoon(final Calendar cal) {
        final int dayOfTheYear = cal.get(6);
        final int yearInMetonicCycle = (cal.get(1) - 1900) % 19 + 1;
        int epact = (11 * yearInMetonicCycle + 18) % 30;
        if ((epact == 25 && yearInMetonicCycle > 11) || epact == 24) {
            ++epact;
        }
        return ((dayOfTheYear + epact) * 6 + 11) % 177 / 22 & 0x7;
    }
    
    public static String getDateForHeader() {
        final Calendar cal = Calendar.getInstance();
        final TimeZone tz = cal.getTimeZone();
        int offset = tz.getOffset(cal.get(0), cal.get(1), cal.get(2), cal.get(5), cal.get(7), cal.get(14));
        final StringBuffer tzMarker = new StringBuffer((offset < 0) ? "-" : "+");
        offset = Math.abs(offset);
        final int hours = offset / 3600000;
        final int minutes = offset / 60000 - 60 * hours;
        if (hours < 10) {
            tzMarker.append("0");
        }
        tzMarker.append(hours);
        if (minutes < 10) {
            tzMarker.append("0");
        }
        tzMarker.append(minutes);
        synchronized (DateUtils.DATE_HEADER_FORMAT_INT) {
            return DateUtils.DATE_HEADER_FORMAT_INT.format(cal.getTime()) + tzMarker.toString();
        }
    }
    
    public static Date parseDateFromHeader(final String datestr) throws ParseException {
        synchronized (DateUtils.DATE_HEADER_FORMAT_INT) {
            return DateUtils.DATE_HEADER_FORMAT_INT.parse(datestr);
        }
    }
    
    public static Date parseIso8601DateTime(final String datestr) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(datestr);
    }
    
    public static Date parseIso8601Date(final String datestr) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(datestr);
    }
    
    public static Date parseIso8601DateTimeOrDate(final String datestr) throws ParseException {
        try {
            return parseIso8601DateTime(datestr);
        }
        catch (ParseException px) {
            return parseIso8601Date(datestr);
        }
    }
    
    static {
        DATE_HEADER_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ", Locale.US);
        DATE_HEADER_FORMAT_INT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ", Locale.US);
        MINUTE_SECONDS = new MessageFormat("{0}{1}");
        LIMITS = new double[] { 0.0, 1.0, 2.0 };
        MINUTES_PART = new String[] { "", "1 minute ", "{0,number,###############} minutes " };
        SECONDS_PART = new String[] { "0 seconds", "1 second", "{1,number} seconds" };
        MINUTES_FORMAT = new ChoiceFormat(DateUtils.LIMITS, DateUtils.MINUTES_PART);
        SECONDS_FORMAT = new ChoiceFormat(DateUtils.LIMITS, DateUtils.SECONDS_PART);
        DateUtils.MINUTE_SECONDS.setFormat(0, DateUtils.MINUTES_FORMAT);
        DateUtils.MINUTE_SECONDS.setFormat(1, DateUtils.SECONDS_FORMAT);
    }
}
