package com.ankamagames.wakfu.client.core.game.challenge;

import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class ChallengeDrop implements Dropable
{
    private final int m_id;
    private final short m_dropWeight;
    private final SimpleCriterion m_dropCriterion;
    
    public ChallengeDrop(final int id, final short dropWeight, final SimpleCriterion dropCriterion) {
        super();
        this.m_id = id;
        this.m_dropWeight = dropWeight;
        this.m_dropCriterion = dropCriterion;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public short getDropWeight() {
        return this.m_dropWeight;
    }
    
    @Override
    public SimpleCriterion getCriterion() {
        return this.m_dropCriterion;
    }
}
