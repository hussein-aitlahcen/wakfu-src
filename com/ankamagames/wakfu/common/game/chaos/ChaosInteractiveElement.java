package com.ankamagames.wakfu.common.game.chaos;

public interface ChaosInteractiveElement
{
    void setState(short p0);
    
    void notifyChangesListeners();
    
    ChaosIEParameter getChaosIEParameter();
    
    AbstractChaosInteractiveElementHandler getChaosElementHandler();
}
