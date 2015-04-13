package com.ankamagames.framework.kernel.core.translator;

import org.apache.log4j.*;
import com.ankamagames.framework.bundle.*;
import java.text.*;
import com.ankamagames.framework.kernel.utils.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.util.*;

public abstract class Translator
{
    public static final String DATE_YMD_SHORT_KEY = "dateFormat.yearMonthDay.short";
    public static final String DATE_YMDHM_SHORT_KEY = "dateFormat.yearMonthDayHourMinute.short";
    public static final String DATE_MDHM_SHORT_KEY = "dateFormat.monthDayHourMinute.short";
    public static final String DURATION_KEY = "dateFormat.yearMonthDayHourMinuteSecond";
    public static final String DURATION_KEY_SHORT = "durationFormat.yearMonthDayHourMinuteSecond.short";
    public static final String DURATION_KEY_VERY_SHORT = "durationFormat.yearMonthDayHourMinuteSecond.veryShort";
    public static final String DURATION_KEY_VERY_SHORT_WITH_LONG_UNITS = "durationFormat.yearMonthDayHourMinuteSecond.veryShortWithLongUnits";
    private static final Logger m_logger;
    protected static Translator m_instance;
    private Language m_language;
    private String m_path;
    private Bundle m_bundle;
    public static final TimeZone DEFAULT_TZ;
    private NumberFormat m_integerFormatter;
    private NumberFormat m_decimalFormatter;
    private NumberFormat m_percentFormatter;
    private DateFormat m_dateFormatFull;
    private DateFormat m_dateFormatShort;
    private DateFormat m_timeFormatShort;
    private DateFormat m_dateTimeFormatShort;
    
    public static Translator getInstance() {
        return Translator.m_instance;
    }
    
    public Translator() {
        super();
        this.setLanguage(getDefaultLanguage());
    }
    
    public void setLanguage(final Language language) {
        this.m_language = language;
        this.reload();
    }
    
    public Language getLanguage() {
        return this.m_language;
    }
    
    public void setPath(final String path) {
        this.m_path = path;
        this.reload();
    }
    
    public String getString(final String key) {
        return StringFormatter.format(this.getStringWithoutFormat(key), new Object[0]);
    }
    
    @NotNull
    public String getString(final String key, final Object... args) {
        final String format = this.getStringWithoutFormat(key);
        try {
            return StringFormatter.format(format, args);
        }
        catch (IllegalFormatException e) {
            Translator.m_logger.warn((Object)e.getMessage());
            return format;
        }
    }
    
    @NotNull
    public String getStringWithoutFormat(final String key) {
        final String value = (this.m_bundle != null) ? this.m_bundle.get(key) : null;
        if (value == null) {
            Translator.m_logger.warn((Object)("Propri\u00e9t\u00e9 introuvable dans le Translator key=" + key));
            final StringBuilder sb1 = new StringBuilder(key.length() + 2);
            return sb1.append('!').append(key).append('!').toString();
        }
        return value;
    }
    
    public boolean containsKey(final String key) {
        return this.m_bundle != null && this.m_bundle.containsKey(key);
    }
    
    public static Language getDefaultLanguage() {
        final String sysLanguage = System.getProperty("user.language");
        final Language language = Language.getLanguage(sysLanguage);
        if (language == null) {
            return Language.EN;
        }
        return language;
    }
    
    private boolean reload() {
        if (this.m_path != null && this.m_language != null) {
            try {
                final InputStream inputStream = ContentFileHelper.openFile(this.m_path + "_" + this.m_language.getLocale().getLanguage() + ".properties");
                this.m_bundle = new Bundle(inputStream);
                inputStream.close();
            }
            catch (Exception e) {
                Translator.m_logger.error((Object)("Exception sur le chargement de la langue " + this.m_language), (Throwable)e);
                return false;
            }
        }
        if (this.m_language != null) {
            this.initializeFormatters(this.m_language);
        }
        return false;
    }
    
    private void initializeFormatters(@NotNull final Language language) {
        this.m_integerFormatter = NumberFormat.getIntegerInstance(language.getActualLocale());
        this.m_decimalFormatter = NumberFormat.getNumberInstance(language.getActualLocale());
        this.m_percentFormatter = NumberFormat.getPercentInstance(language.getActualLocale());
        this.m_dateFormatFull = DateFormat.getDateInstance(0, language.getActualLocale());
        this.m_timeFormatShort = DateFormat.getTimeInstance(3, language.getActualLocale());
        this.m_dateFormatShort = DateFormat.getDateInstance(3, language.getActualLocale());
        this.m_dateTimeFormatShort = DateFormat.getDateTimeInstance(3, 3, language.getActualLocale());
        this.m_dateFormatFull.setTimeZone(Translator.DEFAULT_TZ);
        this.m_timeFormatShort.setTimeZone(Translator.DEFAULT_TZ);
        this.m_dateFormatShort.setTimeZone(Translator.DEFAULT_TZ);
        this.m_dateTimeFormatShort.setTimeZone(Translator.DEFAULT_TZ);
    }
    
    public String formatNumber(final double number) {
        return this.m_decimalFormatter.format(number);
    }
    
    public String formatNumber(final long number) {
        return this.m_integerFormatter.format(number);
    }
    
    public String formatPercent(final double percent) {
        return this.m_percentFormatter.format(percent);
    }
    
    public String formatDateFull(final Date date) {
        return this.m_dateFormatFull.format(date);
    }
    
    public String formatTimeShort(final Date date) {
        return this.m_timeFormatShort.format(date);
    }
    
    public String formatDateShort(final Date date) {
        return this.m_dateFormatShort.format(date);
    }
    
    public String formatDateTimeShort(final Date date) {
        return this.m_dateTimeFormatShort.format(date);
    }
    
    static {
        m_logger = Logger.getLogger((Class)Translator.class);
        Translator.m_instance = null;
        DEFAULT_TZ = TimeZone.getTimeZone("UTC");
    }
}
