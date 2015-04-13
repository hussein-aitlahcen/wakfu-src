package com.ankamagames.xulor2.util.xmlToJava;

public interface DocumentVariableAccessor
{
    boolean isVarDefined(String p0);
    
    void setVarDefined(String p0);
    
    String getUnusedVarName();
}
