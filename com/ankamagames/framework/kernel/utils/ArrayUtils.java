package com.ankamagames.framework.kernel.utils;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class ArrayUtils
{
    private static final Logger m_logger;
    
    public static void shuffleArray(final long[] array) {
        for (int i = array.length - 1; i >= 0; --i) {
            final int index = MathHelper.random(i + 1);
            final long a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }
    
    public static void shuffleArray(final int[] array) {
        for (int i = array.length - 1; i >= 0; --i) {
            final int index = MathHelper.random(i + 1);
            final int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }
    
    public static void shuffleArray(final Object[] array) {
        for (int i = array.length - 1; i >= 0; --i) {
            final int index = MathHelper.random(i + 1);
            final Object a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }
    
    public static boolean contains(final Object[] array, final Object value) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean contains(final byte[] array, final byte value) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean contains(final short[] array, final short value) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean contains(final int[] array, final int value) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean contains(final long[] array, final long value) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ArrayUtils.class);
    }
}
