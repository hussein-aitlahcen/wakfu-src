package com.ankamagames.wakfu.common.game.characterInfo.action;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public abstract class AbstractMonsterAction implements SerializableObject
{
    protected final long m_id;
    protected final byte m_typeId;
    protected final SimpleCriterion m_criterion;
    protected final boolean m_criteriaOnNpc;
    private final boolean m_movePlayer;
    protected final long m_duration;
    protected final boolean m_canBeTriggeredWhenBusy;
    
    protected AbstractMonsterAction(final long id, final byte typeId, final SimpleCriterion criterion, final boolean criteriaOnNpc, final boolean movePlayer, final long duration, final boolean canBeTriggeredWhenBusy) {
        super();
        this.m_id = id;
        this.m_typeId = typeId;
        this.m_criterion = criterion;
        this.m_criteriaOnNpc = criteriaOnNpc;
        this.m_movePlayer = movePlayer;
        this.m_duration = duration;
        this.m_canBeTriggeredWhenBusy = canBeTriggeredWhenBusy;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public byte getTypeId() {
        return this.m_typeId;
    }
    
    public boolean isMovePlayer() {
        return this.m_movePlayer;
    }
}
