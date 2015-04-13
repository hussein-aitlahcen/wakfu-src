package com.ankamagames.framework.kernel.core.maths;

import org.apache.log4j.*;

public class RoundedNumber extends Number
{
    public static final Logger m_logger;
    private final double m_rawValue;
    private final Long m_roundedValue;
    
    public RoundedNumber(final long roundedValue, final double rawValue) {
        super();
        this.m_rawValue = rawValue;
        this.m_roundedValue = roundedValue;
    }
    
    public double getNotRoundedValue() {
        return this.m_rawValue;
    }
    
    @Override
    public int intValue() {
        return (int)(Object)this.m_roundedValue;
    }
    
    @Override
    public long longValue() {
        return this.m_roundedValue;
    }
    
    @Override
    public float floatValue() {
        return this.m_roundedValue;
    }
    
    @Override
    public double doubleValue() {
        return this.m_roundedValue;
    }
    
    @Override
    public String toString() {
        return this.m_roundedValue.toString();
    }
    
    @Override
    public int hashCode() {
        return this.m_roundedValue.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof RoundedNumber) {
            return this.m_roundedValue == ((RoundedNumber)obj).m_roundedValue;
        }
        return obj instanceof Long && this.m_roundedValue.equals(obj);
    }
    
    static {
        m_logger = Logger.getLogger((Class)RoundedNumber.class);
    }
}
