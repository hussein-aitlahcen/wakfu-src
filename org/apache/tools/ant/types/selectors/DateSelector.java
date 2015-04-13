package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class DateSelector extends BaseExtendSelector
{
    private static final FileUtils FILE_UTILS;
    private long millis;
    private String dateTime;
    private boolean includeDirs;
    private long granularity;
    private String pattern;
    private TimeComparison when;
    public static final String MILLIS_KEY = "millis";
    public static final String DATETIME_KEY = "datetime";
    public static final String CHECKDIRS_KEY = "checkdirs";
    public static final String GRANULARITY_KEY = "granularity";
    public static final String WHEN_KEY = "when";
    public static final String PATTERN_KEY = "pattern";
    
    public DateSelector() {
        super();
        this.millis = -1L;
        this.dateTime = null;
        this.includeDirs = false;
        this.granularity = 0L;
        this.when = TimeComparison.EQUAL;
        this.granularity = DateSelector.FILE_UTILS.getFileTimestampGranularity();
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{dateselector date: ");
        buf.append(this.dateTime);
        buf.append(" compare: ").append(this.when.getValue());
        buf.append(" granularity: ");
        buf.append(this.granularity);
        if (this.pattern != null) {
            buf.append(" pattern: ").append(this.pattern);
        }
        buf.append("}");
        return buf.toString();
    }
    
    public void setMillis(final long millis) {
        this.millis = millis;
    }
    
    public long getMillis() {
        if (this.dateTime != null) {
            this.validate();
        }
        return this.millis;
    }
    
    public void setDatetime(final String dateTime) {
        this.dateTime = dateTime;
        this.millis = -1L;
    }
    
    public void setCheckdirs(final boolean includeDirs) {
        this.includeDirs = includeDirs;
    }
    
    public void setGranularity(final int granularity) {
        this.granularity = granularity;
    }
    
    public void setWhen(final TimeComparisons tcmp) {
        this.setWhen((TimeComparison)tcmp);
    }
    
    public void setWhen(final TimeComparison t) {
        this.when = t;
    }
    
    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }
    
    public void setParameters(final Parameter[] parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; ++i) {
                final String paramname = parameters[i].getName();
                if ("millis".equalsIgnoreCase(paramname)) {
                    try {
                        this.setMillis(Long.parseLong(parameters[i].getValue()));
                    }
                    catch (NumberFormatException nfe) {
                        this.setError("Invalid millisecond setting " + parameters[i].getValue());
                    }
                }
                else if ("datetime".equalsIgnoreCase(paramname)) {
                    this.setDatetime(parameters[i].getValue());
                }
                else if ("checkdirs".equalsIgnoreCase(paramname)) {
                    this.setCheckdirs(Project.toBoolean(parameters[i].getValue()));
                }
                else if ("granularity".equalsIgnoreCase(paramname)) {
                    try {
                        this.setGranularity(Integer.parseInt(parameters[i].getValue()));
                    }
                    catch (NumberFormatException nfe) {
                        this.setError("Invalid granularity setting " + parameters[i].getValue());
                    }
                }
                else if ("when".equalsIgnoreCase(paramname)) {
                    this.setWhen(new TimeComparison(parameters[i].getValue()));
                }
                else if ("pattern".equalsIgnoreCase(paramname)) {
                    this.setPattern(parameters[i].getValue());
                }
                else {
                    this.setError("Invalid parameter " + paramname);
                }
            }
        }
    }
    
    public void verifySettings() {
        if (this.dateTime == null && this.millis < 0L) {
            this.setError("You must provide a datetime or the number of milliseconds.");
        }
        else if (this.millis < 0L && this.dateTime != null) {
            final DateFormat df = (this.pattern == null) ? DateFormat.getDateTimeInstance(3, 3, Locale.US) : new SimpleDateFormat(this.pattern);
            try {
                this.setMillis(df.parse(this.dateTime).getTime());
                if (this.millis < 0L) {
                    this.setError("Date of " + this.dateTime + " results in negative milliseconds value" + " relative to epoch (January 1, 1970, 00:00:00 GMT).");
                }
            }
            catch (ParseException pe) {
                this.setError("Date of " + this.dateTime + " Cannot be parsed correctly. It should be in" + ((this.pattern == null) ? " MM/DD/YYYY HH:MM AM_PM" : this.pattern) + " format.");
            }
        }
    }
    
    public boolean isSelected(final File basedir, final String filename, final File file) {
        this.validate();
        return (file.isDirectory() && !this.includeDirs) || this.when.evaluate(file.lastModified(), this.millis, this.granularity);
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
    }
    
    public static class TimeComparisons extends TimeComparison
    {
    }
}
