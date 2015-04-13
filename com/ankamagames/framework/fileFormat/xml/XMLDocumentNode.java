package com.ankamagames.framework.fileFormat.xml;

import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;

public class XMLDocumentNode implements DocumentEntry
{
    public static final String TEXT_KEY = "#text";
    public static final String CDATA_KEY = "#cdata-section";
    public static final String COMMENT_KEY = "#comment";
    private String m_name;
    private String m_value;
    private final ArrayList<XMLNodeAttribute> m_attributes;
    private final ArrayList<XMLDocumentNode> m_children;
    
    public XMLDocumentNode(final String name, final String value) {
        super();
        this.m_attributes = new ArrayList<XMLNodeAttribute>();
        this.m_children = new ArrayList<XMLDocumentNode>();
        this.m_name = ((name != null) ? name.intern() : null);
        this.m_value = ((value != null) ? value.intern() : null);
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
    public int getId() {
        return 0;
    }
    
    @Override
    public void setId(final int id) {
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
    public DocumentEntry getParameterByName(final String name) {
        if (this.m_attributes != null) {
            for (int i = 0, size = this.m_attributes.size(); i < size; ++i) {
                final XMLNodeAttribute attribute = this.m_attributes.get(i);
                if (attribute.getName().equalsIgnoreCase(name)) {
                    return attribute;
                }
            }
        }
        return null;
    }
    
    @Override
    public void addParameter(final DocumentEntry parameter) {
        if (!this.m_attributes.contains(parameter)) {
            this.m_attributes.add((XMLNodeAttribute)parameter);
        }
    }
    
    public void addAllParameters(final ArrayList<? extends DocumentEntry> parameters) {
        for (final DocumentEntry parameter : parameters) {
            this.addParameter(parameter);
        }
    }
    
    @Override
    public void removeParameter(final DocumentEntry parameter) {
        this.m_attributes.remove(parameter);
    }
    
    @Override
    public <T extends DocumentEntry> ArrayList<T> getParameters() {
        return (ArrayList<T>)this.m_attributes;
    }
    
    @Override
    public DocumentEntry getChildByName(final String name) {
        if (this.m_name.equalsIgnoreCase(name)) {
            return this;
        }
        for (final DocumentEntry child : this.m_children) {
            final DocumentEntry ret = child.getChildByName(name);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }
    
    @Override
    public ArrayList<DocumentEntry> getChildrenByName(final String name) {
        final ArrayList<DocumentEntry> list = new ArrayList<DocumentEntry>();
        if (this.m_name.equalsIgnoreCase(name)) {
            list.add(this);
        }
        else {
            for (final DocumentEntry child : this.m_children) {
                final ArrayList<DocumentEntry> subList = child.getChildrenByName(name);
                if (subList != null) {
                    list.addAll(subList);
                }
            }
        }
        return list.isEmpty() ? null : list;
    }
    
    @Override
    public ArrayList<DocumentEntry> getDirectChildrenByName(final String name) {
        final ArrayList<DocumentEntry> list = new ArrayList<DocumentEntry>();
        for (final DocumentEntry child : this.m_children) {
            if (child.getName().equalsIgnoreCase(name)) {
                list.add(child);
            }
        }
        return list.isEmpty() ? null : list;
    }
    
    @Override
    public void addChild(final DocumentEntry entry) {
        if (!this.m_children.contains(entry)) {
            this.m_children.add((XMLDocumentNode)entry);
        }
    }
    
    @Override
    public void removeChild(final DocumentEntry entry) {
        this.m_children.remove(entry);
    }
    
    @Override
    public ArrayList<? extends DocumentEntry> getChildren() {
        return this.m_children;
    }
    
    @Override
    public String toString() {
        return this.m_name + ' ' + this.m_value;
    }
}
