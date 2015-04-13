package com.ankamagames.framework.net.soap.data;

import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.fileFormat.xml.*;

public final class NilData implements Data
{
    public static final NilData VALUE;
    
    @Override
    public DataType getDataType() {
        return DataType.NIL;
    }
    
    @Nullable
    @Override
    public Object getValue() {
        return null;
    }
    
    @Override
    public int getIntValue() {
        return 0;
    }
    
    @Override
    public long getLongValue() {
        return 0L;
    }
    
    @Override
    public float getFloatValue() {
        return 0.0f;
    }
    
    @Override
    public String getStringValue() {
        return "";
    }
    
    @Override
    public boolean getBooleanValue() {
        return false;
    }
    
    @Override
    public DocumentEntry generateNode() {
        final DocumentEntry parameterChild = new XMLDocumentNode(null, "null");
        parameterChild.addParameter(new XMLNodeAttribute("xsi:type", this.getDataType().getTag()));
        return parameterChild;
    }
    
    @Override
    public String toString() {
        return "NilData";
    }
    
    static {
        VALUE = new NilData();
    }
}
