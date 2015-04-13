package com.ankamagames.framework.kernel.core.common.collections.iterators;

import java.util.*;

public class MergedIterator<T> implements Iterator<T>
{
    private final List<Iterator<? extends T>> m_iterators;
    private Iterator<? extends T> lastIterator;
    private int lastIteratorIndex;
    
    public MergedIterator(final Collection<? extends Iterator<? extends T>> its) {
        super();
        this.m_iterators = new ArrayList<Iterator<? extends T>>(2);
        this.lastIteratorIndex = -1;
        this.m_iterators.addAll(its);
        if (!this.m_iterators.isEmpty()) {
            this.lastIterator = this.m_iterators.get(0);
            this.lastIteratorIndex = 0;
        }
    }
    
    public MergedIterator() {
        this((Collection)Collections.emptyList());
    }
    
    public MergedIterator(final Iterator<? extends T>... its) {
        this(Arrays.asList(its));
    }
    
    public MergedIterator(final Iterator<? extends T> it) {
        this(Collections.singletonList(it));
    }
    
    public MergedIterator(final Iterator<? extends T> it1, final Iterator<? extends T> it2) {
        this((Collection)Arrays.asList(it1, it2));
    }
    
    public void merge(final Iterator<? extends T> it) {
        if (this.lastIterator == null) {
            this.lastIterator = it;
            this.lastIteratorIndex = this.m_iterators.size();
        }
        this.m_iterators.add(it);
    }
    
    @Override
    public boolean hasNext() {
        if (this.lastIterator == null) {
            return false;
        }
        if (this.lastIterator.hasNext()) {
            return true;
        }
        for (int iteratorsCount = this.m_iterators.size(), i = this.lastIteratorIndex + 1; i < iteratorsCount; ++i) {
            if (this.m_iterators.get(i).hasNext()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public T next() {
        if (this.lastIterator.hasNext()) {
            return (T)this.lastIterator.next();
        }
        for (int iteratorsCount = this.m_iterators.size(), i = this.lastIteratorIndex + 1; i < iteratorsCount; ++i) {
            this.lastIteratorIndex = i;
            this.lastIterator = this.m_iterators.get(i);
            if (this.lastIterator.hasNext()) {
                return (T)this.lastIterator.next();
            }
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
