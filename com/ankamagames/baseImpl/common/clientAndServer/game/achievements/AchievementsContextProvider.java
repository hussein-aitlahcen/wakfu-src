package com.ankamagames.baseImpl.common.clientAndServer.game.achievements;

public interface AchievementsContextProvider<AchievementContextType extends AchievementsContext>
{
    long getId();
    
    long getOwnerId();
    
    AchievementContextType getAchievementsContext();
}
