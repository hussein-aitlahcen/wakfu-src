package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

import gnu.trove.*;

public class AchievementList
{
    private final int m_id;
    private final TIntArrayList m_achievements;
    
    public AchievementList(final int id) {
        super();
        this.m_achievements = new TIntArrayList();
        this.m_id = id;
    }
    
    public void addElements(final int[] achievementIds) {
        this.m_achievements.add(achievementIds);
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getAchievement(final int index) {
        return this.m_achievements.get(index);
    }
    
    public int size() {
        return this.m_achievements.size();
    }
}
