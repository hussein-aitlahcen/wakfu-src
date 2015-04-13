package com.ankamagames.framework.net.soap.data;

import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.document.*;

public interface Data
{
    public static final String TYPE_PARAM = "xsi:type";
    
    DataType getDataType();
    
    @Nullable
    Object getValue();
    
    float getFloatValue();
    
    int getIntValue();
    
    long getLongValue();
    
    String getStringValue();
    
    boolean getBooleanValue();
    
    DocumentEntry generateNode();
}
