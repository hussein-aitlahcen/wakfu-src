package com.ankamagames.framework.kernel.utils;

public final class Accumulator
{
    private int m_value;
    
    public final void inc() {
        ++this.m_value;
    }
    
    public final void accumulate(final int increment) {
        this.m_value += increment;
    }
    
    public final void reset() {
        this.m_value = 0;
    }
    
    public final int getValue() {
        return this.m_value;
    }
}
