package org.jdom;

import org.jdom.filter.*;
import java.util.*;

class FilterIterator implements Iterator
{
    private Iterator iterator;
    private Filter filter;
    private Object nextObject;
    private static final String CVS_ID = "@(#) $RCSfile: FilterIterator.java,v $ $Revision: 1.5 $ $Date: 2004/08/31 19:36:12 $ $Name: jdom_1_0 $";
    
    public FilterIterator(final Iterator iterator, final Filter filter) {
        super();
        if (iterator == null || filter == null) {
            throw new IllegalArgumentException("null parameter");
        }
        this.iterator = iterator;
        this.filter = filter;
    }
    
    public boolean hasNext() {
        if (this.nextObject != null) {
            return true;
        }
        while (this.iterator.hasNext()) {
            final Object obj = this.iterator.next();
            if (this.filter.matches(obj)) {
                this.nextObject = obj;
                return true;
            }
        }
        return false;
    }
    
    public Object next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        final Object obj = this.nextObject;
        this.nextObject = null;
        return obj;
    }
    
    public void remove() {
        this.iterator.remove();
    }
}
