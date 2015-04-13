package com.ankamagames.framework.script;

public interface LuaScriptEventListener
{
    void onLuaScriptError(LuaScript p0, LuaScriptErrorType p1, String p2);
    
    void onLuaScriptFinished(LuaScript p0);
    
    void onLuaScriptLoaded(LuaScript p0);
}
