package com.ankamagames.baseImpl.common.clientAndServer.game.dialog;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class DialogChoice
{
    private final int m_id;
    private final SimpleCriterion m_criterion;
    private final byte m_typeId;
    private final boolean m_clientOnly;
    
    public DialogChoice(final int id, final SimpleCriterion criterion, final byte typeId, final boolean clientOnly) {
        super();
        this.m_id = id;
        this.m_criterion = criterion;
        this.m_typeId = typeId;
        this.m_clientOnly = clientOnly;
    }
    
    public boolean isClientOnly() {
        return this.m_clientOnly;
    }
    
    public boolean isValid(final DialogUser user, final DialogSource source) {
        return this.m_criterion == null || this.m_criterion.isValid(user, source, this, user.getAppropriateContext());
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public byte getTypeId() {
        return this.m_typeId;
    }
}
