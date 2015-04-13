package com.ankamagames.framework.fileFormat.xml;

import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;

public class XMLNodeAttribute implements DocumentEntry
{
    private String m_name;
    private String m_value;
    
    public XMLNodeAttribute(final String name, final String value) {
        super();
        this.m_name = ((name != null) ? name.intern() : null);
        this.m_value = ((value != null) ? value.intern() : null);
    }
    
    @Override
    public int getId() {
        return 0;
    }
    
    @Override
    public void setId(final int id) {
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public void setName(final String name) {
        this.m_name = ((name != null) ? name.intern() : null);
    }
    
    @Override
    public String getStringValue() {
        return this.m_value;
    }
    
    @Override
    public boolean getBooleanValue() {
        return Boolean.parseBoolean(this.m_value);
    }
    
    @Override
    public byte getByteValue() {
        return Byte.parseByte(this.m_value);
    }
    
    @Override
    public short getShortValue() {
        return Short.parseShort(this.m_value);
    }
    
    @Override
    public int getIntValue() {
        return Integer.parseInt(this.m_value);
    }
    
    @Override
    public long getLongValue() {
        return Long.parseLong(this.m_value);
    }
    
    @Override
    public float getFloatValue() {
        return Float.parseFloat(this.m_value);
    }
    
    @Override
    public double getDoubleValue() {
        return Double.parseDouble(this.m_value);
    }
    
    @Override
    public void setStringValue(final String value) {
        this.m_value = ((value != null) ? value.intern() : null);
    }
    
    @Override
    public void setBooleanValue(final boolean value) {
        this.m_value = (value ? "true" : "false");
    }
    
    @Override
    public void setByteValue(final byte value) {
        this.m_value = ("" + value).intern();
    }
    
    @Override
    public void setIntValue(final int value) {
        this.m_value = ("" + value).intern();
    }
    
    @Override
    public void setLongValue(final long value) {
        this.m_value = ("" + value).intern();
    }
    
    @Override
    public void setFloatValue(final float value) {
        this.m_value = ("" + value).intern();
    }
    
    @Override
    public void setDoubleValue(final double value) {
        this.m_value = ("" + value).intern();
    }
    
    @Override
    public void addChild(final DocumentEntry entry) {
    }
    
    @Override
    public void removeChild(final DocumentEntry entry) {
    }
    
    @Override
    public ArrayList<? extends DocumentEntry> getChildren() {
        return null;
    }
    
    @Override
    public DocumentEntry getChildByName(final String name) {
        return null;
    }
    
    @Override
    public ArrayList<DocumentEntry> getChildrenByName(final String name) {
        return null;
    }
    
    @Override
    public ArrayList<DocumentEntry> getDirectChildrenByName(final String name) {
        return null;
    }
    
    @Override
    public DocumentEntry getParameterByName(final String name) {
        return null;
    }
    
    @Override
    public void addParameter(final DocumentEntry parameter) {
    }
    
    @Override
    public void removeParameter(final DocumentEntry parameter) {
    }
    
    @Override
    public <T extends DocumentEntry> ArrayList<T> getParameters() {
        return null;
    }
    
    @Override
    public String toString() {
        return this.m_name + '=' + this.m_value;
    }
}
