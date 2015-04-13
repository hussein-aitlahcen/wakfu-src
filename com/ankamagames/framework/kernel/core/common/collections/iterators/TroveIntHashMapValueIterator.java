package com.ankamagames.framework.kernel.core.common.collections.iterators;

import java.util.*;
import gnu.trove.*;

public class TroveIntHashMapValueIterator<T> implements Iterator<T>
{
    private TIntObjectIterator<T> m_iterator;
    
    public TroveIntHashMapValueIterator(final TIntObjectHashMap<T> map) {
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
