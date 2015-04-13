package org.apache.tools.ant.util.regexp;

public class RegexpUtil
{
    public static boolean hasFlag(final int options, final int flag) {
        return (options & flag) > 0;
    }
    
    public static int removeFlag(final int options, final int flag) {
        return options & -1 - flag;
    }
    
    public static int asOptions(final String flags) {
        int options = 0;
        if (flags != null) {
            options = asOptions(flags.indexOf(105) == -1, flags.indexOf(109) != -1, flags.indexOf(115) != -1);
            if (flags.indexOf(103) != -1) {
                options |= 0x10;
            }
        }
        return options;
    }
    
    public static int asOptions(final boolean caseSensitive) {
        return asOptions(caseSensitive, false, false);
    }
    
    public static int asOptions(final boolean caseSensitive, final boolean multiLine, final boolean singleLine) {
        int options = 0;
        if (!caseSensitive) {
            options |= 0x100;
        }
        if (multiLine) {
            options |= 0x1000;
        }
        if (singleLine) {
            options |= 0x10000;
        }
        return options;
    }
}
