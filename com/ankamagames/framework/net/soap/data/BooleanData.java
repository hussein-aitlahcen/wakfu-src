package com.ankamagames.framework.net.soap.data;

import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.fileFormat.xml.*;

public class BooleanData implements Data
{
    private final boolean m_value;
    
    public BooleanData(final boolean value) {
        super();
        this.m_value = value;
    }
    
    @Override
    public DataType getDataType() {
        return DataType.BOOLEAN;
    }
    
    @Override
    public Object getValue() {
        return this.m_value;
    }
    
    @Override
    public float getFloatValue() {
        throw new UnsupportedOperationException("getFloatValue sur BooleanData");
    }
    
    @Override
    public int getIntValue() {
        throw new UnsupportedOperationException("IntValue sur BooleanData");
    }
    
    @Override
    public long getLongValue() {
        throw new UnsupportedOperationException("LongValue sur BooleanData");
    }
    
    @Override
    public String getStringValue() {
        return String.valueOf(this.m_value);
    }
    
    @Override
    public boolean getBooleanValue() {
        return this.m_value;
    }
    
    @Override
    public DocumentEntry generateNode() {
        final DocumentEntry parameterChild = new XMLDocumentNode(null, String.valueOf(this.m_value));
        parameterChild.addParameter(new XMLNodeAttribute("xsi:type", this.getDataType().getTag()));
        return parameterChild;
    }
    
    @Override
    public String toString() {
        return "BooleanData{m_value=" + this.m_value + '}';
    }
}
