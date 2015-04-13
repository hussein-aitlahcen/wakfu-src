package com.ankamagames.baseImpl.graphics.ui.protocol.message;

import com.ankamagames.framework.kernel.core.common.message.*;

public abstract class AbstractUIMessage extends Message
{
    private int m_id;
    private boolean m_booleanValue;
    private byte m_byteValue;
    private short m_shortValue;
    private int m_intValue;
    private long m_longValue;
    private double m_doubleValue;
    private float m_floatValue;
    private String m_stringValue;
    private Object m_objectValue;
    
    public AbstractUIMessage(final MessageHandler handler) {
        super();
        this.setHandler(handler);
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        return true;
    }
    
    @Override
    public byte[] encode() {
        return null;
    }
    
    @Override
    public void setId(final int id) {
        this.m_id = id;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    public byte getByteValue() {
        return this.m_byteValue;
    }
    
    public void setByteValue(final byte byteValue) {
        this.m_byteValue = byteValue;
    }
    
    public int getIntValue() {
        return this.m_intValue;
    }
    
    public void setIntValue(final int intValue) {
        this.m_intValue = intValue;
    }
    
    public long getLongValue() {
        return this.m_longValue;
    }
    
    public void setLongValue(final long longValue) {
        this.m_longValue = longValue;
    }
    
    public short getShortValue() {
        return this.m_shortValue;
    }
    
    public void setShortValue(final short shortValue) {
        this.m_shortValue = shortValue;
    }
    
    public double getDoubleValue() {
        return this.m_doubleValue;
    }
    
    public void setDoubleValue(final double doubleValue) {
        this.m_doubleValue = doubleValue;
    }
    
    public float getFloatValue() {
        return this.m_floatValue;
    }
    
    public void setFloatValue(final float floatValue) {
        this.m_floatValue = floatValue;
    }
    
    public String getStringValue() {
        return this.m_stringValue;
    }
    
    public void setStringValue(final String stringValue) {
        this.m_stringValue = stringValue;
    }
    
    public boolean getBooleanValue() {
        return this.m_booleanValue;
    }
    
    public void setBooleanValue(final boolean booleanValue) {
        this.m_booleanValue = booleanValue;
    }
    
    public void setObjectValue(final Object value) {
        this.m_objectValue = value;
    }
    
    public <T> T getObjectValue() {
        return (T)this.m_objectValue;
    }
}
