package com.ankamagames.wakfu.common.game.companion;

public interface CompanionModelListener
{
    void xpChanged(CompanionModel p0, long p1);
    
    void nameChanged(CompanionModel p0);
    
    void idChanged(CompanionModel p0);
    
    void onCurrentHpChanged(CompanionModel p0);
    
    void onMaxHpChanged(CompanionModel p0);
    
    void onUnlockedChanged(CompanionModel p0);
}
