package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import java.util.*;

public class AchievementsHistoryCategoryView extends AchievementCategoryView
{
    public static final int HISTORY_CATEGORY_ID = -1;
    public static final String[] FIELDS;
    
    public AchievementsHistoryCategoryView(final long characterId) {
        super(characterId);
        this.m_selectedSubCategoryId = -1;
    }
    
    @Override
    public String[] getFields() {
        return AchievementsHistoryCategoryView.FIELDS;
    }
    
    @Override
    protected String getName() {
        return WakfuTranslator.getInstance().getString("achievement.history");
    }
    
    @Override
    public boolean isHistory() {
        return true;
    }
    
    @Override
    public int getId() {
        return -1;
    }
    
    @Override
    public void addChildren(final AchievementStandardCategoryView achievementCategoryView) {
    }
    
    @Override
    public TIntObjectHashMap<AchievementStandardCategoryView> getChildren() {
        return null;
    }
    
    @Override
    protected ArrayList<AchievementView> getAchievementFilteredList() {
        final ArrayList<AchievementView> achievementViews = new ArrayList<AchievementView>();
        for (final AchievementHistoryEntry achievement : this.m_context.getHistory()) {
            final AchievementView view = AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, achievement.getAchievementId());
            if (view != null && !AchievementsViewManager.INSTANCE.isQuest(this.m_characterId, view.getId())) {
                achievementViews.add(view);
            }
        }
        return achievementViews;
    }
    
    protected boolean isParent() {
        return false;
    }
    
    static {
        FIELDS = new String[] { "name", "achievements", "isHistory" };
    }
}
