package com.ankamagames.framework.kernel.utils;

import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;
import java.util.*;

public class TroveUtils
{
    public static long removeAtRandomIndex(final TLongArrayList list) {
        return list.remove(MathHelper.random(list.size()));
    }
    
    public static boolean removeFirstValue(final TLongArrayList list, final long value) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            if (list.getQuick(i) == value) {
                list.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public static boolean removeFirstValue(final TIntArrayList list, final int value) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            if (list.getQuick(i) == value) {
                list.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public static boolean removeFirstValue(final TShortArrayList list, final short value) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            if (list.getQuick(i) == value) {
                list.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public static boolean removeFirstValue(final TByteArrayList list, final byte value) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            if (list.getQuick(i) == value) {
                list.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public static <T> void insert(final TIntObjectHashMap<ArrayList<T>> map, final int key, final T value) {
        ArrayList<T> list = map.get(key);
        if (list == null) {
            list = new ArrayList<T>();
            map.put(key, list);
        }
        list.add(value);
    }
}
