package org.apache.tools.ant.types;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;

public class TimeComparison extends EnumeratedAttribute
{
    private static final String[] VALUES;
    private static final FileUtils FILE_UTILS;
    public static final TimeComparison BEFORE;
    public static final TimeComparison AFTER;
    public static final TimeComparison EQUAL;
    
    public TimeComparison() {
        super();
    }
    
    public TimeComparison(final String value) {
        super();
        this.setValue(value);
    }
    
    public String[] getValues() {
        return TimeComparison.VALUES;
    }
    
    public boolean evaluate(final long t1, final long t2) {
        return this.evaluate(t1, t2, TimeComparison.FILE_UTILS.getFileTimestampGranularity());
    }
    
    public boolean evaluate(final long t1, final long t2, final long g) {
        final int cmp = this.getIndex();
        if (cmp == -1) {
            throw new BuildException("TimeComparison value not set.");
        }
        if (cmp == 0) {
            return t1 - g < t2;
        }
        if (cmp == 1) {
            return t1 + g > t2;
        }
        return Math.abs(t1 - t2) <= g;
    }
    
    public static int compare(final long t1, final long t2) {
        return compare(t1, t2, TimeComparison.FILE_UTILS.getFileTimestampGranularity());
    }
    
    public static int compare(final long t1, final long t2, final long g) {
        final long diff = t1 - t2;
        final long abs = Math.abs(diff);
        return (abs > Math.abs(g)) ? ((int)(diff / abs)) : 0;
    }
    
    static {
        VALUES = new String[] { "before", "after", "equal" };
        FILE_UTILS = FileUtils.getFileUtils();
        BEFORE = new TimeComparison("before");
        AFTER = new TimeComparison("after");
        EQUAL = new TimeComparison("equal");
    }
}
