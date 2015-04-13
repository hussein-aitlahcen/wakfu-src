package com.ankamagames.baseImpl.common.clientAndServer.game.achievements;

import gnu.trove.*;

public interface AchievementsContext
{
    boolean hasObjective(int p0);
    
    boolean isObjectiveCompleted(int p0);
    
    boolean hasAchievement(int p0);
    
    boolean isAchievementActive(int p0);
    
    boolean isAchievementComplete(int p0);
    
    boolean isAchievementRepeatable(int p0);
    
    boolean isAchievementFailed(int p0);
    
    boolean isAchievementRunning(int p0);
    
    ObjectiveDefinition getObjective(int p0);
    
    AchievementVariable getVariableByName(String p0);
    
    long getVariableValue(int p0);
    
    boolean isFollowed(int p0);
    
    boolean setFollowed(int p0, boolean p1);
    
    TIntArrayList getFollowed();
    
    boolean canResetAchievement(int p0);
}
