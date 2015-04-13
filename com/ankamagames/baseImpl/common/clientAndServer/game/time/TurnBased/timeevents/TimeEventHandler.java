package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

public interface TimeEventHandler
{
    void handleRunningEffectActivationEvent(RunningEffectActivationEvent p0);
    
    void handleEffectAreaActivationEvent(EffectAreaActivationEvent p0);
    
    void handleRunningEffectDeactivationEvent(RunningEffectDeactivationEvent p0);
    
    void handlePlacementEndEvent(PlacementEndEvent p0);
    
    void handleTableTurnStartEvent(TableTurnStartEvent p0);
    
    void handleTableTurnEndEvent(TableTurnEndEvent p0);
    
    void handleFighterTurnStartEvent(FighterTurnStartEvent p0);
    
    void handleFighterTurnEndEvent(FighterTurnEndEvent p0);
    
    void handleBeforeFighterTurnEndEvent(long p0);
}
