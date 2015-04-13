package com.ankamagames.framework.kernel.core.common.collections.iterators;

import java.util.*;

public class EmptyIterator<T> implements Iterator<T>
{
    @Override
    public boolean hasNext() {
        return false;
    }
    
    @Override
    public T next() {
        throw new NoSuchElementException();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
