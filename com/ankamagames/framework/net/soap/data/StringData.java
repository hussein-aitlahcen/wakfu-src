package com.ankamagames.framework.net.soap.data;

import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.fileFormat.xml.*;

public class StringData implements Data
{
    private final String m_value;
    
    public StringData(final String value) {
        super();
        this.m_value = value;
    }
    
    @Override
    public DataType getDataType() {
        return DataType.STRING;
    }
    
    @Override
    public String getValue() {
        return this.m_value;
    }
    
    @Override
    public int getIntValue() {
        throw new UnsupportedOperationException("getIntValue sur StringData");
    }
    
    @Override
    public long getLongValue() {
        throw new UnsupportedOperationException("LongValue sur StringData");
    }
    
    @Override
    public float getFloatValue() {
        throw new UnsupportedOperationException("getFloatValue sur StringData");
    }
    
    @Override
    public String getStringValue() {
        return this.m_value;
    }
    
    @Override
    public boolean getBooleanValue() {
        throw new UnsupportedOperationException("getBooleanValue sur StringData");
    }
    
    @Override
    public DocumentEntry generateNode() {
        final DocumentEntry parameterChild = new XMLDocumentNode(null, this.m_value);
        parameterChild.addParameter(new XMLNodeAttribute("xsi:type", this.getDataType().getTag()));
        return parameterChild;
    }
    
    @Override
    public String toString() {
        return "StringData{m_value='" + this.m_value + '\'' + '}';
    }
}
