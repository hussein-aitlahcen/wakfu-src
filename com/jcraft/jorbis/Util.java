package com.jcraft.jorbis;

import java.util.*;

class Util
{
    static int ilog(int v) {
        int ret = 0;
        while (v != 0) {
            ++ret;
            v >>>= 1;
        }
        return ret;
    }
    
    static int ilog2(int v) {
        int ret = 0;
        while (v > 1) {
            ++ret;
            v >>>= 1;
        }
        return ret;
    }
    
    static int icount(int v) {
        int ret = 0;
        while (v != 0) {
            ret += (v & 0x1);
            v >>>= 1;
        }
        return ret;
    }
    
    static class LightMap<K, V>
    {
        private final ArrayList<K> m_keys;
        private final ArrayList<V> m_values;
        
        LightMap() {
            super();
            this.m_keys = new ArrayList<K>();
            this.m_values = new ArrayList<V>();
        }
        
        public void put(final K key, final V value) {
            if (this.m_keys.contains(key)) {
                return;
            }
            this.m_keys.add(key);
            this.m_values.add(value);
        }
        
        public V getValue(final K key) {
            final int index = this.m_keys.indexOf(key);
            return (index == -1) ? null : this.m_values.get(index);
        }
    }
    
    static class TypeParamPair<T>
    {
        public final int m_type;
        public final T m_param;
        
        TypeParamPair(final int type, final T param) {
            super();
            this.m_type = type;
            this.m_param = param;
        }
    }
}
