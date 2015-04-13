package com.ankamagames.baseImpl.common.clientAndServer.game.loot;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public interface Dropable
{
    int getId();
    
    short getDropWeight();
    
    SimpleCriterion getCriterion();
}
