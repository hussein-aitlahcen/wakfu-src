package com.ankamagames.wakfu.client.core.game.characterInfo.monsters;

import com.ankamagames.wakfu.common.datas.Breed.*;

public class MonsterEvolutionStep extends AbstractMonsterEvolutionStep
{
    private final int m_scriptId;
    
    public MonsterEvolutionStep(final int id, final short evolvedBreedId, final int scriptId) {
        super(id, evolvedBreedId);
        this.m_scriptId = scriptId;
    }
    
    public int getScriptId() {
        return this.m_scriptId;
    }
}
