package com.ankamagames.framework.kernel.core.maths;

public class Interval
{
    private int m_min;
    private int m_max;
    
    public Interval(final int min, final int max) {
        super();
        this.m_min = min;
        this.m_max = max;
    }
    
    public int getMin() {
        return this.m_min;
    }
    
    public void setMin(final int min) {
        this.m_min = min;
    }
    
    public int getMax() {
        return this.m_max;
    }
    
    public void setMax(final int max) {
        this.m_max = max;
    }
    
    public boolean isIn(final int value) {
        return value >= this.m_min && value <= this.m_max;
    }
    
    public boolean isAbove(final int value) {
        return value > this.m_max;
    }
    
    public boolean isUnder(final int value) {
        return value < this.m_min;
    }
    
    public int getCenter() {
        return this.m_min + (this.m_max - this.m_min) / 2;
    }
}
