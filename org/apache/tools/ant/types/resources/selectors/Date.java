package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import java.util.*;
import java.text.*;

public class Date implements ResourceSelector
{
    private static final String MILLIS_OR_DATETIME = "Either the millis or the datetime attribute must be set.";
    private static final FileUtils FILE_UTILS;
    private Long millis;
    private String dateTime;
    private String pattern;
    private TimeComparison when;
    private long granularity;
    
    public Date() {
        super();
        this.millis = null;
        this.dateTime = null;
        this.pattern = null;
        this.when = TimeComparison.EQUAL;
        this.granularity = Date.FILE_UTILS.getFileTimestampGranularity();
    }
    
    public synchronized void setMillis(final long m) {
        this.millis = new Long(m);
    }
    
    public synchronized long getMillis() {
        return (this.millis == null) ? -1L : this.millis;
    }
    
    public synchronized void setDateTime(final String s) {
        this.dateTime = s;
        this.millis = null;
    }
    
    public synchronized String getDatetime() {
        return this.dateTime;
    }
    
    public synchronized void setGranularity(final long g) {
        this.granularity = g;
    }
    
    public synchronized long getGranularity() {
        return this.granularity;
    }
    
    public synchronized void setPattern(final String p) {
        this.pattern = p;
    }
    
    public synchronized String getPattern() {
        return this.pattern;
    }
    
    public synchronized void setWhen(final TimeComparison c) {
        this.when = c;
    }
    
    public synchronized TimeComparison getWhen() {
        return this.when;
    }
    
    public synchronized boolean isSelected(final Resource r) {
        if (this.dateTime == null && this.millis == null) {
            throw new BuildException("Either the millis or the datetime attribute must be set.");
        }
        if (this.millis == null) {
            final DateFormat df = (this.pattern == null) ? DateFormat.getDateTimeInstance(3, 3, Locale.US) : new SimpleDateFormat(this.pattern);
            try {
                final long m = df.parse(this.dateTime).getTime();
                if (m < 0L) {
                    throw new BuildException("Date of " + this.dateTime + " results in negative milliseconds value" + " relative to epoch (January 1, 1970, 00:00:00 GMT).");
                }
                this.setMillis(m);
            }
            catch (ParseException pe) {
                throw new BuildException("Date of " + this.dateTime + " Cannot be parsed correctly. It should be in" + ((this.pattern == null) ? " MM/DD/YYYY HH:MM AM_PM" : this.pattern) + " format.");
            }
        }
        return this.when.evaluate(r.getLastModified(), this.millis, this.granularity);
    }
    
    static {
        FILE_UTILS = FileUtils.getFileUtils();
    }
}
