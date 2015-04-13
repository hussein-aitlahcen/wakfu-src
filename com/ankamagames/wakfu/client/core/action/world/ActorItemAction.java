package com.ankamagames.wakfu.client.core.action.world;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.script.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.script.*;

public class ActorItemAction extends ScriptedAction
{
    private final PlayerCharacter m_character;
    private long m_clientVarValue;
    private LuaScriptEventListener m_listener;
    
    public ActorItemAction(final int scriptId, final PlayerCharacter character, @Nullable final String varName, final long varValue) {
        super(TimedAction.getNextUid(), 0, 0);
        this.m_clientVarValue = Long.MIN_VALUE;
        this.m_character = character;
        this.addJavaFunctionsLibrary(new ItemActionFunctionsLibrary(this));
        if (varName != null) {
            final THashMap<String, Object> map = new THashMap<String, Object>(1);
            map.put(varName, varValue);
            this.setContextVariables(map);
        }
        this.setScriptFileId(scriptId);
    }
    
    public void setListener(final LuaScriptEventListener listener) {
        this.m_listener = listener;
    }
    
    public PlayerCharacter getCharacter() {
        return this.m_character;
    }
    
    public void setClientVarValue(final long clientVarValue) {
        this.m_clientVarValue = clientVarValue;
    }
    
    public long getClientVarValue() {
        return this.m_clientVarValue;
    }
    
    @Override
    public void onLuaScriptError(final LuaScript script, final LuaScriptErrorType errorType, final String message) {
        if (this.m_listener != null) {
            this.m_listener.onLuaScriptError(script, errorType, message);
        }
        super.onLuaScriptError(script, errorType, message);
    }
    
    @Override
    public void onLuaScriptLoaded(final LuaScript script) {
        if (this.m_listener != null) {
            this.m_listener.onLuaScriptLoaded(script);
        }
        super.onLuaScriptLoaded(script);
    }
    
    @Override
    public void onLuaScriptFinished(final LuaScript script) {
        if (this.m_listener != null) {
            this.m_listener.onLuaScriptFinished(script);
        }
        super.onLuaScriptFinished(script);
    }
}
