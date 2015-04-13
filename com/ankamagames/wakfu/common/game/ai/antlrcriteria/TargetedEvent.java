package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

public interface TargetedEvent
{
    long getTargetId();
    
    EventTargetable getTarget();
    
    int getTargetRefId();
}
