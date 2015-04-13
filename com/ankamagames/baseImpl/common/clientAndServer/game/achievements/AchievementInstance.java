package com.ankamagames.baseImpl.common.clientAndServer.game.achievements;

public interface AchievementInstance extends AchievementDefinition
{
    void setLastCompletedTime(long p0);
    
    long getLastCompletedTime();
    
    long getStartTime();
    
    boolean isComplete();
    
    boolean isActive();
    
    void setActive(boolean p0);
}
