package com.ankamagames.wakfu.client.core.game.characterInfo.monsters;

import com.ankamagames.wakfu.common.datas.Breed.*;

public class MonsterBehaviour extends AbstractMonsterBehaviourModel
{
    private final int m_scriptId;
    private final boolean m_needsToWaitPathEnd;
    private final int m_type;
    
    public MonsterBehaviour(final int id, final int type, final int scriptId, final boolean needsToWaitPathEnd) {
        super(id);
        this.m_type = type;
        this.m_scriptId = scriptId;
        this.m_needsToWaitPathEnd = needsToWaitPathEnd;
    }
    
    public int getType() {
        return this.m_type;
    }
    
    public int getScriptId() {
        return this.m_scriptId;
    }
    
    public boolean needsToWaitPathEnd() {
        return this.m_needsToWaitPathEnd;
    }
}
