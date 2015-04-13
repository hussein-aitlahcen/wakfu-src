package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.types.*;
import java.util.*;

public class LazyResourceCollectionWrapper extends AbstractResourceCollectionWrapper
{
    private final List<Resource> cachedResources;
    private FilteringIterator filteringIterator;
    
    public LazyResourceCollectionWrapper() {
        super();
        this.cachedResources = new ArrayList<Resource>();
    }
    
    protected Iterator<Resource> createIterator() {
        Iterator<Resource> iterator;
        if (this.isCache()) {
            if (this.filteringIterator == null) {
                this.filteringIterator = new FilteringIterator(this.getResourceCollection().iterator());
            }
            iterator = new CachedIterator(this.filteringIterator);
        }
        else {
            iterator = new FilteringIterator(this.getResourceCollection().iterator());
        }
        return iterator;
    }
    
    protected int getSize() {
        final Iterator<Resource> it = this.createIterator();
        int size = 0;
        while (it.hasNext()) {
            it.next();
            ++size;
        }
        return size;
    }
    
    protected boolean filterResource(final Resource r) {
        return false;
    }
    
    private class FilteringIterator implements Iterator<Resource>
    {
        Resource next;
        boolean ended;
        protected final Iterator<Resource> it;
        
        public FilteringIterator(final Iterator<Resource> it) {
            super();
            this.next = null;
            this.ended = false;
            this.it = it;
        }
        
        public boolean hasNext() {
            if (this.ended) {
                return false;
            }
            while (this.next == null) {
                if (!this.it.hasNext()) {
                    this.ended = true;
                    return false;
                }
                this.next = this.it.next();
                if (!LazyResourceCollectionWrapper.this.filterResource(this.next)) {
                    continue;
                }
                this.next = null;
            }
            return true;
        }
        
        public Resource next() {
            if (!this.hasNext()) {
                throw new UnsupportedOperationException();
            }
            final Resource r = this.next;
            this.next = null;
            return r;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    private class CachedIterator implements Iterator<Resource>
    {
        int cusrsor;
        private final Iterator<Resource> it;
        
        public CachedIterator(final Iterator<Resource> it) {
            super();
            this.cusrsor = 0;
            this.it = it;
        }
        
        public boolean hasNext() {
            synchronized (LazyResourceCollectionWrapper.this.cachedResources) {
                if (LazyResourceCollectionWrapper.this.cachedResources.size() > this.cusrsor) {
                    return true;
                }
                if (!this.it.hasNext()) {
                    return false;
                }
                final Resource r = this.it.next();
                LazyResourceCollectionWrapper.this.cachedResources.add(r);
            }
            return true;
        }
        
        public Resource next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            synchronized (LazyResourceCollectionWrapper.this.cachedResources) {
                return LazyResourceCollectionWrapper.this.cachedResources.get(this.cusrsor++);
            }
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
