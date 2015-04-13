package com.ankamagames.framework.net.soap.data;

import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.fileFormat.xml.*;

public class IntData implements Data
{
    private final int m_value;
    
    public IntData(final int value) {
        super();
        this.m_value = value;
    }
    
    @Override
    public DataType getDataType() {
        return DataType.INT;
    }
    
    @Override
    public Object getValue() {
        return this.m_value;
    }
    
    @Override
    public int getIntValue() {
        return this.m_value;
    }
    
    @Override
    public long getLongValue() {
        return this.m_value;
    }
    
    @Override
    public float getFloatValue() {
        return this.m_value;
    }
    
    @Override
    public String getStringValue() {
        return String.valueOf(this.m_value);
    }
    
    @Override
    public boolean getBooleanValue() {
        throw new UnsupportedOperationException("getBooleanValue sur IntData");
    }
    
    @Override
    public DocumentEntry generateNode() {
        final DocumentEntry parameterChild = new XMLDocumentNode(null, String.valueOf(this.m_value));
        parameterChild.addParameter(new XMLNodeAttribute("xsi:type", this.getDataType().getTag()));
        return parameterChild;
    }
    
    @Override
    public String toString() {
        return "IntData{m_value=" + this.m_value + '}';
    }
}
