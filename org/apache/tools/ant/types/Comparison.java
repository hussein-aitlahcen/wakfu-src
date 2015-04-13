package org.apache.tools.ant.types;

import org.apache.tools.ant.*;
import java.util.*;

public class Comparison extends EnumeratedAttribute
{
    private static final String[] VALUES;
    public static final Comparison EQUAL;
    public static final Comparison NOT_EQUAL;
    public static final Comparison GREATER;
    public static final Comparison LESS;
    public static final Comparison GREATER_EQUAL;
    public static final Comparison LESS_EQUAL;
    private static final int[] EQUAL_INDEX;
    private static final int[] LESS_INDEX;
    private static final int[] GREATER_INDEX;
    
    public Comparison() {
        super();
    }
    
    public Comparison(final String value) {
        super();
        this.setValue(value);
    }
    
    public String[] getValues() {
        return Comparison.VALUES;
    }
    
    public boolean evaluate(final int comparisonResult) {
        if (this.getIndex() == -1) {
            throw new BuildException("Comparison value not set.");
        }
        final int[] i = (comparisonResult < 0) ? Comparison.LESS_INDEX : ((comparisonResult > 0) ? Comparison.GREATER_INDEX : Comparison.EQUAL_INDEX);
        return Arrays.binarySearch(i, this.getIndex()) >= 0;
    }
    
    static {
        VALUES = new String[] { "equal", "greater", "less", "ne", "ge", "le", "eq", "gt", "lt", "more" };
        EQUAL = new Comparison("equal");
        NOT_EQUAL = new Comparison("ne");
        GREATER = new Comparison("greater");
        LESS = new Comparison("less");
        GREATER_EQUAL = new Comparison("ge");
        LESS_EQUAL = new Comparison("le");
        EQUAL_INDEX = new int[] { 0, 4, 5, 6 };
        LESS_INDEX = new int[] { 2, 3, 5, 8 };
        GREATER_INDEX = new int[] { 1, 3, 4, 7, 9 };
    }
}
