package com.ankamagames.framework.kernel.core.common.collections;

public final class LongIntPair
{
    private long m_first;
    private int m_second;
    
    public LongIntPair() {
        super();
    }
    
    public LongIntPair(final long first, final int second) {
        super();
        this.m_first = first;
        this.m_second = second;
    }
    
    public long getFirst() {
        return this.m_first;
    }
    
    public void setFirst(final long first) {
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
        if (!(obj instanceof LongIntPair)) {
            return false;
        }
        final LongIntPair that = (LongIntPair)obj;
        return this.m_first == that.m_first && this.m_second == that.m_second;
    }
    
    @Override
    public int hashCode() {
        int result = (int)(this.m_first ^ this.m_first >>> 32);
        result = 31 * result + this.m_second;
        return result;
    }
    
    @Override
    public String toString() {
        return "LongIntPair{m_first=" + this.m_first + ", m_second=" + this.m_second + '}';
    }
}
