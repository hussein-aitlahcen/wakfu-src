package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

import gnu.trove.*;

public class AchievementListManager
{
    public static final AchievementListManager INSTANCE;
    private final TIntObjectHashMap<AchievementList> m_listsById;
    private final TIntObjectHashMap<AchievementList> m_listsByAchievementId;
    
    private AchievementListManager() {
        super();
        this.m_listsById = new TIntObjectHashMap<AchievementList>();
        this.m_listsByAchievementId = new TIntObjectHashMap<AchievementList>();
    }
    
    public void addList(final AchievementList list) {
        this.m_listsById.put(list.getId(), list);
        for (int i = 0, size = list.size(); i < size; ++i) {
            this.m_listsByAchievementId.put(list.getAchievement(i), list);
        }
    }
    
    public AchievementList getListById(final int id) {
        return this.m_listsById.get(id);
    }
    
    public AchievementList getListByAchievementId(final int achievementId) {
        return this.m_listsByAchievementId.get(achievementId);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AchievementListManager");
        sb.append("{m_listsById=").append(this.m_listsById);
        sb.append(", m_listsByAchievementId=").append(this.m_listsByAchievementId);
        sb.append('}');
        return sb.toString();
    }
    
    static {
        INSTANCE = new AchievementListManager();
    }
}
