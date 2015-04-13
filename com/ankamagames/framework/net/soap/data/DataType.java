package com.ankamagames.framework.net.soap.data;

import java.util.*;

public enum DataType
{
    NIL(""), 
    BOOLEAN("xsd:boolean"), 
    INT("xsd:int"), 
    LONG("xsd:long"), 
    FLOAT("xsd:float"), 
    STRING("xsd:string"), 
    MAP("ns2:Map"), 
    ARRAY("SOAP-ENC:Array");
    
    private static final Map<String, DataType> TYPE_MAP;
    private final String m_tag;
    
    private DataType(final String tag) {
        this.m_tag = tag;
    }
    
    public static DataType getType(final String tag) {
        return DataType.TYPE_MAP.get(tag);
    }
    
    public String getTag() {
        return this.m_tag;
    }
    
    static {
        TYPE_MAP = new HashMap<String, DataType>();
        for (final DataType type : values()) {
            DataType.TYPE_MAP.put(type.m_tag, type);
        }
    }
}
