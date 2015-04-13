package com.ankamagames.wakfu.common.game.item.loot;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public interface Loot
{
    int getReferenceId();
    
    short getNbRoll();
    
    short getDropQty();
    
    double getDropRate();
    
    int getMinProspection();
    
    SimpleCriterion getCriterion();
}
