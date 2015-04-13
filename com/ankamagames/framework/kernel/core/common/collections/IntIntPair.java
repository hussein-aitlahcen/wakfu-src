package com.ankamagames.framework.kernel.core.common.collections;

public final class IntIntPair
{
    private int m_first;
    private int m_second;
    
    public IntIntPair() {
        super();
    }
    
    public IntIntPair(final int first, final int second) {
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
    
    public int getSecond() {
        return this.m_second;
    }
    
    public void setSecond(final int second) {
        this.m_second = second;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IntIntPair)) {
            return false;
        }
        final IntIntPair that = (IntIntPair)obj;
        return this.m_first == that.m_first && this.m_second == that.m_second;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_first;
        result = 31 * result + this.m_second;
        return result;
    }
    
    @Override
    public String toString() {
        return "IntIntPair{m_first=" + this.m_first + ", m_second=" + this.m_second + '}';
    }
}
