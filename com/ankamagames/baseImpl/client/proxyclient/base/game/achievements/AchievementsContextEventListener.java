package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

public interface AchievementsContextEventListener
{
    void onAchievementCompleted(Achievement p0);
    
    void onAchievementFollowed(int p0, boolean p1);
    
    void onAchievementActivated(Achievement p0);
    
    void onObjectiveCompleted(Objective p0);
    
    void onAchievementActivationRequired(Achievement p0, long p1);
    
    void onAchievementFailed(Achievement p0);
    
    void onInitialize(ClientAchievementsContext p0);
    
    void onAchievementReset(Achievement p0);
    
    void onVarUpdated(Variable p0, long p1);
    
    void onTryToActivate(int p0);
}
