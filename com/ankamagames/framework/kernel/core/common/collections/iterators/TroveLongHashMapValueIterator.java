package com.ankamagames.framework.kernel.core.common.collections.iterators;

import java.util.*;
import gnu.trove.*;

public class TroveLongHashMapValueIterator<T> implements Iterator<T>
{
    private final TLongObjectIterator<T> m_iterator;
    
    public TroveLongHashMapValueIterator(final TLongObjectHashMap<T> map) {
        super();
        this.m_iterator = map.iterator();
    }
    
    @Override
    public boolean hasNext() {
        return this.m_iterator.hasNext();
    }
    
    @Override
    public T next() {
        this.m_iterator.advance();
        return this.m_iterator.value();
    }
    
    @Override
    public void remove() {
        this.m_iterator.remove();
    }
}
