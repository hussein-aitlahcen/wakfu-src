package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.script.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class MonsterEvolutionAction extends ScriptedAction
{
    private final NonPlayerCharacter m_npc;
    private final short m_evolvedBreedId;
    private final short m_evolvedLevel;
    
    public MonsterEvolutionAction(final int uniqueId, final int actionType, final int actionId, final int scriptId, final long npcId, final short evolvedBreedId, final short evolvedLevel) {
        super(uniqueId, actionType, actionId);
        this.m_npc = (NonPlayerCharacter)CharacterInfoManager.getInstance().getCharacter(npcId);
        this.m_evolvedBreedId = evolvedBreedId;
        this.m_evolvedLevel = evolvedLevel;
        this.addJavaFunctionsLibrary(new MonsterEvolutionFunctionsLibrary(this));
        this.setScriptFileId(scriptId);
    }
    
    @Override
    public long onRun() {
        super.onRun();
        return -1L;
    }
    
    @Override
    protected void onActionFinished() {
        super.onActionFinished();
        if (this.m_npc != null) {
            final Short breedId = this.m_npc.getBreedId();
            if (breedId != this.m_evolvedBreedId) {
                MonsterEvolutionAction.m_logger.error((Object)("Oublie de script LUA d\u00e9finissant une \u00e9volution pour le breedId=" + breedId));
                this.m_npc.evolve(this.m_evolvedBreedId, this.m_evolvedLevel);
            }
        }
    }
    
    public long getNpcId() {
        return this.m_npc.getId();
    }
    
    public short getEvolvedBreedId() {
        return this.m_evolvedBreedId;
    }
    
    public short getEvolvedLevel() {
        return this.m_evolvedLevel;
    }
    
    public CharacterInfo getNpc() {
        return this.m_npc;
    }
}
