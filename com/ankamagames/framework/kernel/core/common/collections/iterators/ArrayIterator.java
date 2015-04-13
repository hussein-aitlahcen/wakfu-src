package com.ankamagames.framework.kernel.core.common.collections.iterators;

import java.util.*;

public class ArrayIterator<E> implements Iterator<E>
{
    private E[] m_array;
    private int m_arrayLength;
    private boolean m_bReturnsNull;
    private int m_nextIndex;
    
    public ArrayIterator(final E[] array, final boolean returnsNull) {
        super();
        this.m_nextIndex = -1;
        this.m_array = array;
        this.m_arrayLength = array.length;
        this.m_bReturnsNull = returnsNull;
        this.searchNextIndex();
    }
    
    @Override
    public boolean hasNext() {
        return this.m_nextIndex < this.m_arrayLength;
    }
    
    @Override
    public E next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException("Array end reached. Array Size : " + this.m_arrayLength);
        }
        final E val = (E)this.m_array[this.m_nextIndex];
        this.searchNextIndex();
        return val;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    private void searchNextIndex() {
        if (this.m_bReturnsNull) {
            ++this.m_nextIndex;
        }
        else {
            ++this.m_nextIndex;
            while (this.m_nextIndex < this.m_arrayLength && this.m_array[this.m_nextIndex] == null) {
                ++this.m_nextIndex;
            }
        }
    }
}
