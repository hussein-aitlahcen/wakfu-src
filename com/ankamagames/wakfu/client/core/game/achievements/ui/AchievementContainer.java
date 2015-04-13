package com.ankamagames.wakfu.client.core.game.achievements.ui;

public interface AchievementContainer
{
    int getAchievementsTotalSize();
    
    int countCompletedAchievements();
    
    boolean contains(AchievementView p0);
}
