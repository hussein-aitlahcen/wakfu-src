package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.script.*;

public class MonsterActionAction extends ScriptedAction
{
    private final NonPlayerCharacter m_npc;
    private final PlayerCharacter m_player;
    
    public MonsterActionAction(final int uniqueId, final int actionType, final int actionId, final int scriptId, final NonPlayerCharacter npc, final PlayerCharacter player) {
        super(uniqueId, actionType, actionId);
        this.m_npc = npc;
        this.m_player = player;
        this.addJavaFunctionsLibrary(new MonsterActionFunctionsLibrary(this));
        this.setScriptFileId(scriptId);
    }
    
    public NonPlayerCharacter getNpc() {
        return this.m_npc;
    }
    
    public PlayerCharacter getPlayer() {
        return this.m_player;
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
}
