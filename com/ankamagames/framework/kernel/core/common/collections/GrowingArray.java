package com.ankamagames.framework.kernel.core.common.collections;

import java.lang.reflect.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import gnu.trove.*;

public class GrowingArray<T> implements Iterable<T>
{
    private T[] m_elements;
    
    public GrowingArray() {
        super();
        this.m_elements = (Object[])Array.newInstance(Object.class, 0);
    }
    
    public GrowingArray(final T[] elements) {
        super();
        this.m_elements = elements.clone();
    }
    
    public void add(final T object) {
        if (object == null) {
            return;
        }
        final int effectsCount = this.m_elements.length;
        final T[] newArray = (T[])Array.newInstance(Object.class, effectsCount + 1);
        System.arraycopy(this.m_elements, 0, newArray, 0, effectsCount);
        newArray[effectsCount] = object;
        this.m_elements = newArray;
    }
    
    public void add(final T[] elements) {
        if (elements == null || elements.length == 0) {
            return;
        }
        final int effectsCount = this.m_elements.length;
        final T[] newArray = (T[])Array.newInstance(Object.class, effectsCount + elements.length);
        if (effectsCount > 0) {
            System.arraycopy(this.m_elements, 0, newArray, 0, effectsCount);
        }
        System.arraycopy(elements, 0, newArray, effectsCount, elements.length);
        this.m_elements = newArray;
    }
    
    public void set(final T[] elements) {
        if (elements == null) {
            this.m_elements = (Object[])Array.newInstance(Object.class, 0);
            return;
        }
        this.m_elements = elements;
    }
    
    public void set(final int index, final T element) {
        if (index < 0) {
            return;
        }
        if (index >= this.m_elements.length) {
            final T[] newArray = (T[])Array.newInstance(Object.class, index + 1);
            System.arraycopy(this.m_elements, 0, newArray, 0, this.m_elements.length);
            this.m_elements = newArray;
        }
        this.m_elements[index] = element;
    }
    
    public T get(final int index) {
        if (index < 0 || index >= this.m_elements.length) {
            return null;
        }
        return (T)this.m_elements[index];
    }
    
    public int size() {
        return this.m_elements.length;
    }
    
    public void clear() {
        this.m_elements = (Object[])Array.newInstance(Object.class, 0);
    }
    
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<T>((T[])this.m_elements, false);
    }
    
    public void foreach(final TObjectProcedure<T> procedure) {
        for (int i = 0; i < this.m_elements.length; ++i) {
            if (!procedure.execute((T)this.m_elements[i])) {
                return;
            }
        }
    }
}
