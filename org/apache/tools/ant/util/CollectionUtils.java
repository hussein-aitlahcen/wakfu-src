package org.apache.tools.ant.util;

import java.util.*;
import java.io.*;

public class CollectionUtils
{
    @Deprecated
    public static final List EMPTY_LIST;
    
    public static boolean equals(final Vector<?> v1, final Vector<?> v2) {
        return v1 == v2 || (v1 != null && v2 != null && v1.equals(v2));
    }
    
    public static boolean equals(final Dictionary<?, ?> d1, final Dictionary<?, ?> d2) {
        if (d1 == d2) {
            return true;
        }
        if (d1 == null || d2 == null) {
            return false;
        }
        if (d1.size() != d2.size()) {
            return false;
        }
        final Enumeration<?> e1 = d1.keys();
        while (e1.hasMoreElements()) {
            final Object key = e1.nextElement();
            final Object value1 = d1.get(key);
            final Object value2 = d2.get(key);
            if (value2 == null || !value1.equals(value2)) {
                return false;
            }
        }
        return true;
    }
    
    public static String flattenToString(final Collection<?> c) {
        final StringBuilder sb = new StringBuilder();
        for (final Object o : c) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(o);
        }
        return sb.toString();
    }
    
    public static <K, V> void putAll(final Dictionary<? super K, ? super V> m1, final Dictionary<? extends K, ? extends V> m2) {
        final Enumeration<? extends K> it = m2.keys();
        while (it.hasMoreElements()) {
            final K key = (K)it.nextElement();
            m1.put((Object)key, (Object)m2.get(key));
        }
    }
    
    public static <E> Enumeration<E> append(final Enumeration<E> e1, final Enumeration<E> e2) {
        return new CompoundEnumeration<E>(e1, e2);
    }
    
    public static <E> Enumeration<E> asEnumeration(final Iterator<E> iter) {
        return new Enumeration<E>() {
            public boolean hasMoreElements() {
                return iter.hasNext();
            }
            
            public E nextElement() {
                return iter.next();
            }
        };
    }
    
    public static <E> Iterator<E> asIterator(final Enumeration<E> e) {
        return new Iterator<E>() {
            public boolean hasNext() {
                return e.hasMoreElements();
            }
            
            public E next() {
                return e.nextElement();
            }
            
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public static <T> Collection<T> asCollection(final Iterator<? extends T> iter) {
        final List<T> l = new ArrayList<T>();
        while (iter.hasNext()) {
            l.add((T)iter.next());
        }
        return l;
    }
    
    public static int frequency(final Collection<?> c, final Object o) {
        int freq = 0;
        if (c != null) {
            for (final Object test : c) {
                if (o == null) {
                    if (test != null) {
                        continue;
                    }
                }
                else if (!o.equals(test)) {
                    continue;
                }
                ++freq;
            }
        }
        return freq;
    }
    
    static {
        EMPTY_LIST = Collections.EMPTY_LIST;
    }
    
    public static final class EmptyEnumeration<E> implements Enumeration<E>
    {
        public boolean hasMoreElements() {
            return false;
        }
        
        public E nextElement() throws NoSuchElementException {
            throw new NoSuchElementException();
        }
    }
    
    private static final class CompoundEnumeration<E> implements Enumeration<E>
    {
        private final Enumeration<E> e1;
        private final Enumeration<E> e2;
        
        public CompoundEnumeration(final Enumeration<E> e1, final Enumeration<E> e2) {
            super();
            this.e1 = e1;
            this.e2 = e2;
        }
        
        public boolean hasMoreElements() {
            return this.e1.hasMoreElements() || this.e2.hasMoreElements();
        }
        
        public E nextElement() throws NoSuchElementException {
            if (this.e1.hasMoreElements()) {
                return this.e1.nextElement();
            }
            return this.e2.nextElement();
        }
    }
}
