package com.ankamagames.framework.kernel.core.common.collections;

public class IntObjectPair<S>
{
    private int m_first;
    private S m_second;
    
    public IntObjectPair() {
        super();
    }
    
    public IntObjectPair(final int first, final S second) {
        super();
        this.m_first = first;
        this.m_second = second;
    }
    
    public int getFirst() {
        return this.m_first;
    }
    
    public void setFirst(final int first) {
        this.m_first = first;
    }
    
    public S getSecond() {
        return this.m_second;
    }
    
    public void setSecond(final S second) {
        this.m_second = second;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IntObjectPair)) {
            return false;
        }
        final IntObjectPair that = (IntObjectPair)obj;
        if (this.m_first != that.m_first) {
            return false;
        }
        if (this.m_second != null) {
            if (this.m_second.equals(that.m_second)) {
                return true;
            }
        }
        else if (that.m_second == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_first;
        result = 31 * result + ((this.m_second != null) ? this.m_second.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "IntObjectPair{m_first=" + this.m_first + ", m_second=" + this.m_second + '}';
    }
}
