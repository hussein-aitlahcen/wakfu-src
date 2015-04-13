package com.ankamagames.xulor2.util;

import org.apache.log4j.*;

public class Percentage implements Cloneable
{
    protected static final Logger m_logger;
    private double m_value;
    
    public Percentage(final int value) {
        super();
        this.m_value = value;
    }
    
    public Percentage(final double value) {
        super();
        this.m_value = value;
    }
    
    public static Percentage valueOf(final String value) {
        if (value.charAt(value.length() - 1) != '%') {
            return null;
        }
        final double percent = Double.valueOf(value.substring(0, value.length() - 1));
        return new Percentage(percent);
    }
    
    public double getValue() {
        return this.m_value;
    }
    
    public void setValue(final double value) {
        this.m_value = value;
    }
    
    public boolean equals(final Percentage p) {
        return p != null && p.m_value == this.m_value;
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            Percentage.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)Percentage.class);
    }
}
