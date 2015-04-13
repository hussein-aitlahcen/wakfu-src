package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.datas.*;

public interface PlayerTriggered
{
    long getTriggererId();
    
    BasicCharacterInfo getTriggerer();
}
