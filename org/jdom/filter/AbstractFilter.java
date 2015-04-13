package org.jdom.filter;

public abstract class AbstractFilter implements Filter
{
    private static final String CVS_ID = "@(#) $RCSfile: AbstractFilter.java,v $ $Revision: 1.5 $ $Date: 2004/02/27 11:32:58 $";
    
    public Filter and(final Filter filter) {
        return new AndFilter(this, filter);
    }
    
    public abstract boolean matches(final Object p0);
    
    public Filter negate() {
        return new NegateFilter(this);
    }
    
    public Filter or(final Filter filter) {
        return new OrFilter(this, filter);
    }
}
