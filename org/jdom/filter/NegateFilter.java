package org.jdom.filter;

final class NegateFilter extends AbstractFilter
{
    private static final String CVS_ID = "@(#) $RCSfile: NegateFilter.java,v $ $Revision: 1.3 $ $Date: 2004/02/06 09:28:31 $";
    private Filter filter;
    
    public NegateFilter(final Filter filter) {
        super();
        this.filter = filter;
    }
    
    public boolean equals(final Object obj) {
        return this == obj || (obj instanceof NegateFilter && this.filter.equals(((NegateFilter)obj).filter));
    }
    
    public int hashCode() {
        return ~this.filter.hashCode();
    }
    
    public boolean matches(final Object obj) {
        return this.filter.matches(obj) ^ true;
    }
    
    public Filter negate() {
        return this.filter;
    }
    
    public String toString() {
        return new StringBuffer(64).append("[NegateFilter: ").append(this.filter.toString()).append("]").toString();
    }
}
