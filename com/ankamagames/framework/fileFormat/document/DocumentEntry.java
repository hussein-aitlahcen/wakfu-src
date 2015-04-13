package com.ankamagames.framework.fileFormat.document;

import java.util.*;

public interface DocumentEntry
{
    int getId();
    
    void setId(int p0);
    
    String getName();
    
    void setName(String p0);
    
    String getStringValue();
    
    boolean getBooleanValue();
    
    byte getByteValue();
    
    short getShortValue();
    
    int getIntValue();
    
    long getLongValue();
    
    float getFloatValue();
    
    double getDoubleValue();
    
    void setStringValue(String p0);
    
    void setBooleanValue(boolean p0);
    
    void setByteValue(byte p0);
    
    void setIntValue(int p0);
    
    void setLongValue(long p0);
    
    void setFloatValue(float p0);
    
    void setDoubleValue(double p0);
    
    void addChild(DocumentEntry p0);
    
    void removeChild(DocumentEntry p0);
    
    ArrayList<? extends DocumentEntry> getChildren();
    
    DocumentEntry getChildByName(String p0);
    
    ArrayList<DocumentEntry> getChildrenByName(String p0);
    
    ArrayList<DocumentEntry> getDirectChildrenByName(String p0);
    
    DocumentEntry getParameterByName(String p0);
    
    void addParameter(DocumentEntry p0);
    
    void removeParameter(DocumentEntry p0);
    
     <T extends DocumentEntry> ArrayList<T> getParameters();
}
