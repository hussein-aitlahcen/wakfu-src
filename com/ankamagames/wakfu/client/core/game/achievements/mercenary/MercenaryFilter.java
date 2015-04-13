package com.ankamagames.wakfu.client.core.game.achievements.mercenary;

import com.google.common.base.*;

public enum MercenaryFilter
{
    ALL((Predicate<Integer>)new AllAchievementsValidator()), 
    AVAILABLE((Predicate<Integer>)new AvailableAchievementsValidator()), 
    COMPLETE((Predicate<Integer>)new CompleteAchievementsValidator());
    
    private final Predicate<Integer> m_achievementValidator;
    
    private MercenaryFilter(final Predicate<Integer> achievementValidator) {
        this.m_achievementValidator = achievementValidator;
    }
    
    public boolean isValid(final int achievementId) {
        return this.m_achievementValidator.apply((Object)achievementId);
    }
}
