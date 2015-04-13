package com.ankamagames.framework.kernel.core.common.debug;

import org.apache.log4j.*;
import java.util.*;

public class PoolDebug
{
    private static final Logger m_logger;
    private static boolean ACTIVATED;
    private static final HashMap<Class, HashMap<Object, StackTraceElement[]>> MAPS;
    
    public static void activate(final boolean ACTIVATED) {
        PoolDebug.ACTIVATED = ACTIVATED;
        if (ACTIVATED) {
            PoolDebug.MAPS.clear();
        }
    }
    
    public static void add(final Object obj) {
        if (!PoolDebug.ACTIVATED) {
            return;
        }
        HashMap<Object, StackTraceElement[]> map = PoolDebug.MAPS.get(obj.getClass());
        if (map == null) {
            map = new HashMap<Object, StackTraceElement[]>();
            PoolDebug.MAPS.put(obj.getClass(), map);
        }
        map.put(obj, new Exception().getStackTrace());
    }
    
    public static void remove(final Object obj) {
        if (PoolDebug.MAPS.isEmpty()) {
            return;
        }
        final HashMap<Object, StackTraceElement[]> map = PoolDebug.MAPS.get(obj.getClass());
        if (map == null || map.isEmpty()) {
            return;
        }
        map.remove(obj);
    }
    
    public static HashMap<Class, HashMap<Object, StackTraceElement[]>> getLeaked() {
        final HashMap<Class, HashMap<Object, StackTraceElement[]>> result = new HashMap<Class, HashMap<Object, StackTraceElement[]>>();
        for (final Map.Entry<Class, HashMap<Object, StackTraceElement[]>> entry : PoolDebug.MAPS.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PoolDebug.class);
        PoolDebug.ACTIVATED = false;
        MAPS = new HashMap<Class, HashMap<Object, StackTraceElement[]>>();
    }
}
