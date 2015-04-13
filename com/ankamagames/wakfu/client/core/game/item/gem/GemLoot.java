package com.ankamagames.wakfu.client.core.game.item.gem;

import com.ankamagames.wakfu.common.game.item.loot.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class GemLoot implements Loot
{
    private int m_refId;
    
    public GemLoot(final int refId) {
        super();
        this.m_refId = refId;
    }
    
    @Override
    public SimpleCriterion getCriterion() {
        return null;
    }
    
    @Override
    public int getReferenceId() {
        return this.m_refId;
    }
    
    @Override
    public short getNbRoll() {
        return 1;
    }
    
    @Override
    public short getDropQty() {
        return 1;
    }
    
    @Override
    public double getDropRate() {
        return 0.0;
    }
    
    @Override
    public int getMinProspection() {
        return 0;
    }
}
