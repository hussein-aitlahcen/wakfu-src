package com.ankamagames.framework.kernel.core.common.collections;

import java.io.*;

public class PrimitiveArrays
{
    public static final boolean[] EMPTY_BOOLEAN_ARRAY;
    public static final byte[] EMPTY_BYTE_ARRAY;
    public static final char[] EMPTY_CHAR_ARRAY;
    public static final double[] EMPTY_DOUBLE_ARRAY;
    public static final float[] EMPTY_FLOAT_ARRAY;
    public static final int[] EMPTY_INT_ARRAY;
    public static final long[] EMPTY_LONG_ARRAY;
    public static final short[] EMPTY_SHORT_ARRAY;
    static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY;
    static final Byte[] EMPTY_BYTE_OBJECT_ARRAY;
    static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY;
    static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY;
    static final Float[] EMPTY_FLOAT_OBJECT_ARRAY;
    static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY;
    static final Long[] EMPTY_LONG_OBJECT_ARRAY;
    static final Short[] EMPTY_SHORT_OBJECT_ARRAY;
    
    public static Boolean[] toObject(final boolean[] booleans) {
        if (booleans == null) {
            return null;
        }
        final int length = booleans.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_BOOLEAN_OBJECT_ARRAY;
        }
        final Boolean[] rv = new Boolean[length];
        for (int i = 0; i < length; ++i) {
            rv[i] = booleans[i];
        }
        return rv;
    }
    
    public static Byte[] toObject(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final int length = bytes.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_BYTE_OBJECT_ARRAY;
        }
        final Byte[] rv = new Byte[length];
        for (int i = 0; i < length; ++i) {
            rv[i] = bytes[i];
        }
        return rv;
    }
    
    public static Character[] toObject(final char[] chars) {
        if (chars == null) {
            return null;
        }
        final int length = chars.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_CHARACTER_OBJECT_ARRAY;
        }
        final Character[] rv = new Character[length];
        for (int i = 0; i < length; ++i) {
            rv[i] = chars[i];
        }
        return rv;
    }
    
    public static Double[] toObject(final double[] doubles) {
        if (doubles == null) {
            return null;
        }
        final int length = doubles.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_DOUBLE_OBJECT_ARRAY;
        }
        final Double[] rv = new Double[length];
        for (int i = 0; i < length; ++i) {
            rv[i] = doubles[i];
        }
        return rv;
    }
    
    public static Float[] toObject(final float[] floats) {
        if (floats == null) {
            return null;
        }
        final int length = floats.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_FLOAT_OBJECT_ARRAY;
        }
        final Float[] rv = new Float[length];
        for (int i = 0; i < length; ++i) {
            rv[i] = floats[i];
        }
        return rv;
    }
    
    public static Integer[] toObject(final int[] ints) {
        if (ints == null) {
            return null;
        }
        final int length = ints.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_INTEGER_OBJECT_ARRAY;
        }
        final Integer[] rv = new Integer[length];
        for (int i = 0; i < length; ++i) {
            rv[i] = ints[i];
        }
        return rv;
    }
    
    public static Long[] toObject(final long[] longs) {
        if (longs == null) {
            return null;
        }
        final int length = longs.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_LONG_OBJECT_ARRAY;
        }
        final Long[] rv = new Long[length];
        for (int i = 0; i < length; ++i) {
            rv[i] = longs[i];
        }
        return rv;
    }
    
    public static Short[] toObject(final short[] shorts) {
        if (shorts == null) {
            return null;
        }
        final int length = shorts.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_SHORT_OBJECT_ARRAY;
        }
        final Short[] rv = new Short[length];
        for (int i = 0; i < length; ++i) {
            rv[i] = shorts[i];
        }
        return rv;
    }
    
    public static boolean[] toPrimitive(final Boolean[] booleans) {
        if (booleans == null) {
            return null;
        }
        final int length = booleans.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_BOOLEAN_ARRAY;
        }
        final boolean[] rv = new boolean[length];
        for (int i = 0; i < length; ++i) {
            final Boolean _boolean = booleans[i];
            if (_boolean == null) {
                throw new IllegalArgumentException("array element is null.");
            }
            rv[i] = _boolean;
        }
        return rv;
    }
    
    public static byte[] toPrimitive(final Byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final int length = bytes.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_BYTE_ARRAY;
        }
        final byte[] rv = new byte[length];
        for (int i = 0; i < length; ++i) {
            final Byte _byte = bytes[i];
            if (_byte == null) {
                throw new IllegalArgumentException("array element is null.");
            }
            rv[i] = _byte;
        }
        return rv;
    }
    
    public static char[] toPrimitive(final Character[] characters) {
        if (characters == null) {
            return null;
        }
        final int length = characters.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_CHAR_ARRAY;
        }
        final char[] rv = new char[length];
        for (int i = 0; i < length; ++i) {
            final Character _character = characters[i];
            if (_character == null) {
                throw new IllegalArgumentException("array element is null.");
            }
            rv[i] = _character;
        }
        return rv;
    }
    
    public static double[] toPrimitive(final Double[] doubles) {
        if (doubles == null) {
            return null;
        }
        final int length = doubles.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_DOUBLE_ARRAY;
        }
        final double[] rv = new double[length];
        for (int i = 0; i < length; ++i) {
            final Double _double = doubles[i];
            if (_double == null) {
                throw new IllegalArgumentException("array element is null.");
            }
            rv[i] = _double;
        }
        return rv;
    }
    
    public static float[] toPrimitive(final Float[] floats) {
        if (floats == null) {
            return null;
        }
        final int length = floats.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_FLOAT_ARRAY;
        }
        final float[] rv = new float[length];
        for (int i = 0; i < length; ++i) {
            final Float _float = floats[i];
            if (_float == null) {
                throw new IllegalArgumentException("array element is null.");
            }
            rv[i] = _float;
        }
        return rv;
    }
    
    public static int[] toPrimitive(final Integer[] integers) {
        if (integers == null) {
            return null;
        }
        final int length = integers.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_INT_ARRAY;
        }
        final int[] rv = new int[length];
        for (int i = 0; i < length; ++i) {
            final Integer _integer = integers[i];
            if (_integer == null) {
                throw new IllegalArgumentException("array element is null.");
            }
            rv[i] = _integer;
        }
        return rv;
    }
    
    public static long[] toPrimitive(final Long[] longs) {
        if (longs == null) {
            return null;
        }
        final int length = longs.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_LONG_ARRAY;
        }
        final long[] rv = new long[length];
        for (int i = 0; i < length; ++i) {
            final Long _long = longs[i];
            if (_long == null) {
                throw new IllegalArgumentException("array element is null.");
            }
            rv[i] = _long;
        }
        return rv;
    }
    
    public static short[] toPrimitive(final Short[] shorts) {
        if (shorts == null) {
            return null;
        }
        final int length = shorts.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_SHORT_ARRAY;
        }
        final short[] rv = new short[length];
        for (int i = 0; i < length; ++i) {
            final Short _short = shorts[i];
            if (_short == null) {
                throw new IllegalArgumentException("array element is null.");
            }
            rv[i] = _short;
        }
        return rv;
    }
    
    public static boolean[] toPrimitive(final Boolean[] booleans, final boolean valueIfNull) {
        if (booleans == null) {
            return null;
        }
        final int length = booleans.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_BOOLEAN_ARRAY;
        }
        final boolean[] rv = new boolean[length];
        for (int i = 0; i < length; ++i) {
            final Boolean _boolean = booleans[i];
            rv[i] = ((_boolean == null) ? valueIfNull : _boolean);
        }
        return rv;
    }
    
    public static byte[] toPrimitive(final Byte[] bytes, final byte valueIfNull) {
        if (bytes == null) {
            return null;
        }
        final int length = bytes.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_BYTE_ARRAY;
        }
        final byte[] rv = new byte[length];
        for (int i = 0; i < length; ++i) {
            final Byte _byte = bytes[i];
            rv[i] = ((_byte == null) ? valueIfNull : _byte);
        }
        return rv;
    }
    
    public static char[] toPrimitive(final Character[] characters, final char valueIfNull) {
        if (characters == null) {
            return null;
        }
        final int length = characters.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_CHAR_ARRAY;
        }
        final char[] rv = new char[length];
        for (int i = 0; i < length; ++i) {
            final Character _character = characters[i];
            rv[i] = ((_character == null) ? valueIfNull : _character);
        }
        return rv;
    }
    
    public static double[] toPrimitive(final Double[] doubles, final double valueIfNull) {
        if (doubles == null) {
            return null;
        }
        final int length = doubles.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_DOUBLE_ARRAY;
        }
        final double[] rv = new double[length];
        for (int i = 0; i < length; ++i) {
            final Double _double = doubles[i];
            rv[i] = ((_double == null) ? valueIfNull : _double);
        }
        return rv;
    }
    
    public static float[] toPrimitive(final Float[] floats, final float valueIfNull) {
        if (floats == null) {
            return null;
        }
        final int length = floats.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_FLOAT_ARRAY;
        }
        final float[] rv = new float[length];
        for (int i = 0; i < length; ++i) {
            final Float _float = floats[i];
            rv[i] = ((_float == null) ? valueIfNull : _float);
        }
        return rv;
    }
    
    public static int[] toPrimitive(final Integer[] integers, final int valueIfNull) {
        if (integers == null) {
            return null;
        }
        final int length = integers.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_INT_ARRAY;
        }
        final int[] rv = new int[length];
        for (int i = 0; i < length; ++i) {
            final Integer _integer = integers[i];
            rv[i] = ((_integer == null) ? valueIfNull : _integer);
        }
        return rv;
    }
    
    public static long[] toPrimitive(final Long[] longs, final long valueIfNull) {
        if (longs == null) {
            return null;
        }
        final int length = longs.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_LONG_ARRAY;
        }
        final long[] rv = new long[length];
        for (int i = 0; i < length; ++i) {
            final Long _long = longs[i];
            rv[i] = ((_long == null) ? valueIfNull : _long);
        }
        return rv;
    }
    
    public static short[] toPrimitive(final Short[] shorts, final short valueIfNull) {
        if (shorts == null) {
            return null;
        }
        final int length = shorts.length;
        if (length == 0) {
            return PrimitiveArrays.EMPTY_SHORT_ARRAY;
        }
        final short[] rv = new short[length];
        for (int i = 0; i < length; ++i) {
            final Short _short = shorts[i];
            rv[i] = ((_short == null) ? valueIfNull : _short);
        }
        return rv;
    }
    
    public static String toString(final boolean[] booleans, final String openDelimiter, final String closeDelimiter, final String innerDelimiter) {
        if (booleans == null) {
            throw new IllegalArgumentException("Array argument null.");
        }
        final int length = booleans.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(booleans[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final byte[] bytes, final String openDelimiter, final String closeDelimiter, final String innerDelimiter) {
        if (bytes == null) {
            throw new IllegalArgumentException("Array argument null.");
        }
        final int length = bytes.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(bytes[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final char[] chars, final String openDelimiter, final String closeDelimiter, final String innerDelimiter) {
        if (chars == null) {
            throw new IllegalArgumentException("Array argument null.");
        }
        final int length = chars.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(chars[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final double[] doubles, final String openDelimiter, final String closeDelimiter, final String innerDelimiter) {
        if (doubles == null) {
            throw new IllegalArgumentException("Array argument null.");
        }
        final int length = doubles.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(doubles[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final float[] floats, final String openDelimiter, final String closeDelimiter, final String innerDelimiter) {
        if (floats == null) {
            throw new IllegalArgumentException("Array argument null.");
        }
        final int length = floats.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(floats[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final int[] ints, final String openDelimiter, final String closeDelimiter, final String innerDelimiter) {
        if (ints == null) {
            throw new IllegalArgumentException("Array argument null.");
        }
        final int length = ints.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(ints[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final long[] longs, final String openDelimiter, final String closeDelimiter, final String innerDelimiter) {
        if (longs == null) {
            throw new IllegalArgumentException("Array argument null.");
        }
        final int length = longs.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(longs[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final short[] shorts, final String openDelimiter, final String closeDelimiter, final String innerDelimiter) {
        if (shorts == null) {
            throw new IllegalArgumentException("Array argument null.");
        }
        final int length = shorts.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(shorts[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final boolean[] booleans, final String openDelimiter, final String closeDelimiter, final String innerDelimiter, final String stringIfNull) {
        if (booleans == null) {
            return stringIfNull;
        }
        final int length = booleans.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(booleans[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final byte[] bytes, final String openDelimiter, final String closeDelimiter, final String innerDelimiter, final String stringIfNull) {
        if (bytes == null) {
            return stringIfNull;
        }
        final int length = bytes.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(bytes[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final char[] chars, final String openDelimiter, final String closeDelimiter, final String innerDelimiter, final String stringIfNull) {
        if (chars == null) {
            return stringIfNull;
        }
        final int length = chars.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(chars[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final double[] doubles, final String openDelimiter, final String closeDelimiter, final String innerDelimiter, final String stringIfNull) {
        if (doubles == null) {
            return stringIfNull;
        }
        final int length = doubles.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(doubles[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final float[] floats, final String openDelimiter, final String closeDelimiter, final String innerDelimiter, final String stringIfNull) {
        if (floats == null) {
            return stringIfNull;
        }
        final int length = floats.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(floats[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final int[] ints, final String openDelimiter, final String closeDelimiter, final String innerDelimiter, final String stringIfNull) {
        if (ints == null) {
            return stringIfNull;
        }
        final int length = ints.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(ints[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final long[] longs, final String openDelimiter, final String closeDelimiter, final String innerDelimiter, final String stringIfNull) {
        if (longs == null) {
            return stringIfNull;
        }
        final int length = longs.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(longs[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static String toString(final short[] shorts, final String openDelimiter, final String closeDelimiter, final String innerDelimiter, final String stringIfNull) {
        if (shorts == null) {
            return stringIfNull;
        }
        final int length = shorts.length;
        if (length == 0) {
            return openDelimiter + closeDelimiter;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(openDelimiter);
        for (int i = 0; i < length; ++i) {
            sb.append(shorts[i]);
            sb.append(innerDelimiter);
        }
        sb.setLength(sb.length() - innerDelimiter.length());
        sb.append(closeDelimiter);
        return sb.toString();
    }
    
    public static Object[] toObject(final Object primitiveArray) {
        if (!primitiveArray.getClass().isArray()) {
            throw new IllegalArgumentException("argument is not an array.");
        }
        if (primitiveArray instanceof boolean[]) {
            return toObject((boolean[])primitiveArray);
        }
        if (primitiveArray instanceof byte[]) {
            return toObject((byte[])primitiveArray);
        }
        if (primitiveArray instanceof char[]) {
            return toObject((char[])primitiveArray);
        }
        if (primitiveArray instanceof double[]) {
            return toObject((double[])primitiveArray);
        }
        if (primitiveArray instanceof float[]) {
            return toObject((float[])primitiveArray);
        }
        if (primitiveArray instanceof int[]) {
            return toObject((int[])primitiveArray);
        }
        if (primitiveArray instanceof long[]) {
            return toObject((long[])primitiveArray);
        }
        if (primitiveArray instanceof short[]) {
            return toObject((short[])primitiveArray);
        }
        return (Object[])primitiveArray;
    }
    
    public static boolean contains(final int[] values, final int searched) {
        for (int i = 0; i < values.length; ++i) {
            if (values[i] == searched) {
                return true;
            }
        }
        return false;
    }
    
    static {
        EMPTY_BOOLEAN_ARRAY = new boolean[0];
        EMPTY_BYTE_ARRAY = new byte[0];
        EMPTY_CHAR_ARRAY = new char[0];
        EMPTY_DOUBLE_ARRAY = new double[0];
        EMPTY_FLOAT_ARRAY = new float[0];
        EMPTY_INT_ARRAY = new int[0];
        EMPTY_LONG_ARRAY = new long[0];
        EMPTY_SHORT_ARRAY = new short[0];
        EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
        EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
        EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
        EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
        EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
        EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
        EMPTY_LONG_OBJECT_ARRAY = new Long[0];
        EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
    }
}
