package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.script.*;
import com.ankamagames.framework.script.*;

public class MonsterBehaviourAction extends ScriptedAction
{
    private final NonPlayerCharacter m_npc;
    private final CharacterInfo m_target;
    private final long m_resourcePosition;
    
    public MonsterBehaviourAction(final int uniqueId, final int actionType, final int actionId, final int scriptId, final NonPlayerCharacter npc, final CharacterInfo target) {
        super(uniqueId, actionType, actionId);
        this.m_npc = npc;
        this.m_target = target;
        this.m_resourcePosition = -1L;
        this.addJavaFunctionsLibrary(new MonsterBehaviourFunctionsLibrary(this));
        this.setScriptFileId(scriptId);
    }
    
    public MonsterBehaviourAction(final int uniqueId, final int actionType, final int actionId, final int scriptId, final NonPlayerCharacter npc, final long target) {
        super(uniqueId, actionType, actionId);
        this.m_npc = npc;
        this.m_target = null;
        this.m_resourcePosition = target;
        this.addJavaFunctionsLibrary(new MonsterBehaviourFunctionsLibrary(this));
        this.setScriptFileId(scriptId);
    }
    
    public NonPlayerCharacter getNpc() {
        return this.m_npc;
    }
    
    public CharacterInfo getTarget() {
        return this.m_target;
    }
    
    public long getResourcePosition() {
        return this.m_resourcePosition;
    }
    
    @Override
    public long onRun() {
        final long executionTime = super.onRun();
        if (executionTime != -1L || this.m_fireActionFinishedOnScriptFinished) {
            this.fireActionFinishedEvent();
            this.m_fireActionFinishedOnScriptFinished = false;
        }
        return -1L;
    }
    
    @Override
    public void onLuaScriptFinished(final LuaScript script) {
        super.onLuaScriptFinished(script);
        if (this.m_npc != null) {
            this.m_npc.setCurrentBehaviourAction(null);
        }
        if (this.m_target != null && this.m_target instanceof NonPlayerCharacter) {
            ((NonPlayerCharacter)this.m_target).setCurrentBehaviourAction(null);
        }
    }
    
    public void forceActionEnd() {
        if (this.m_script != null) {
            this.m_script.forceClose();
        }
    }
}
