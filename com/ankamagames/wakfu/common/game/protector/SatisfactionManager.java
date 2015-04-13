package com.ankamagames.wakfu.common.game.protector;

public interface SatisfactionManager
{
    int getMonsterSatisfaction();
    
    int getResourceSatisfaction();
    
    ProtectorSatisfactionLevel getGlobalSatisfaction();
}
