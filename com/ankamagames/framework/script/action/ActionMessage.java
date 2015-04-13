package com.ankamagames.framework.script.action;

public interface ActionMessage
{
    byte getActionContextType();
    
    long getActionContextUniqueId();
}
