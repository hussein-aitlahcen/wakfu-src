package com.ankamagames.framework.java.util;

public class PrimitiveConverter
{
    public static String getString(final Object value) {
        return (value == null) ? "null" : value.toString();
    }
    
    public static boolean getBoolean(final String value, final boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.equalsIgnoreCase("true");
    }
    
    public static boolean getBoolean(final String value) {
        return value != null && value.equalsIgnoreCase("true");
    }
    
    public static boolean getBoolean(final Object value) {
        return value != null && getBoolean(value.toString());
    }
    
    public static int getInteger(final Object value) {
        return getInteger(value, 0);
    }
    
    public static int getInteger(final Object value, final int defaultValue) {
        final long result = toLong(value, defaultValue);
        if (result < -2147483648L || result > 2147483647L) {
            return defaultValue;
        }
        return (int)result;
    }
    
    public static double getDouble(final Object value) {
        return getDouble(value, 0.0);
    }
    
    public static double getDouble(final Object value, final double defaultValue) {
        try {
            if (value instanceof Number) {
                return ((Number)value).doubleValue();
            }
            if (value instanceof String) {
                return Double.parseDouble((String)value);
            }
        }
        catch (Exception e) {
            return defaultValue;
        }
        return defaultValue;
    }
    
    public static float getFloat(final Object value) {
        return getFloat(value, 0.0f);
    }
    
    public static float getFloat(final Object value, final float defaultValue) {
        try {
            if (value instanceof Number) {
                return ((Number)value).floatValue();
            }
            if (value instanceof String) {
                return Float.parseFloat((String)value);
            }
        }
        catch (Exception e) {
            return defaultValue;
        }
        return defaultValue;
    }
    
    public static long getLong(final Object value) {
        return getLong(value, 0L);
    }
    
    public static long getLong(final Object value, final long defaultValue) {
        return toLong(value, defaultValue);
    }
    
    public static byte getByte(final Object value) {
        return getByte(value, (byte)0);
    }
    
    public static byte getByte(final Object value, final byte defaultValue) {
        final long result = toLong(value, defaultValue);
        if (result < -128L || result > 127L) {
            return defaultValue;
        }
        return (byte)result;
    }
    
    public static short getShort(final Object value) {
        return getShort(value, (short)0);
    }
    
    public static short getShort(final Object value, final short defaultValue) {
        final long result = toLong(value, defaultValue);
        if (result < -32768L || result > 32767L) {
            return defaultValue;
        }
        return (short)result;
    }
    
    private static long toLong(final Object object, final long defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        final String value = object.toString();
        final int end = value.length();
        long ival = 0L;
        int idx = 0;
        boolean sign = false;
        if (end == 0) {
            return defaultValue;
        }
        char ch;
        if ((ch = value.charAt(0)) < '0' || ch > '9') {
            if (ch == '-') {
                sign = true;
            }
            else {
                if (ch != '+') {
                    return defaultValue;
                }
                sign = false;
            }
            ++idx;
        }
        if ((ch = value.charAt(idx)) < '0' || ch > '9') {
            return defaultValue;
        }
        while (true) {
            ival += '0' - ch;
            if (++idx == end) {
                return sign ? ival : (-ival);
            }
            if ((ch = value.charAt(idx)) < '0' || ch > '9') {
                return defaultValue;
            }
            ival *= 10L;
        }
    }
    
    public static void main(final String[] args) {
        final long val = toLong("+1000", 1L);
        final long val2 = toLong("-1000", 1L);
        final long val3 = toLong("1000", 1L);
        System.out.println(val);
    }
}
