package com.ankamagames.framework.kernel.core.common.collections;

public final class IntLongPair
{
    private int m_first;
    private long m_second;
    
    public IntLongPair() {
        super();
    }
    
    public IntLongPair(final int first, final long second) {
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
    
    public long getSecond() {
        return this.m_second;
    }
    
    public void setSecond(final long second) {
        this.m_second = second;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IntLongPair)) {
            return false;
        }
        final IntLongPair that = (IntLongPair)obj;
        return this.m_first == that.m_first && this.m_second == that.m_second;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_first;
        result = 31 * result + (int)(this.m_second ^ this.m_second >>> 32);
        return result;
    }
    
    @Override
    public String toString() {
        return "IntLongPair{m_first=" + this.m_first + ", m_second=" + this.m_second + '}';
    }
}
