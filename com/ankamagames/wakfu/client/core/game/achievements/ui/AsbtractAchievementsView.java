package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import gnu.trove.*;

public abstract class AsbtractAchievementsView extends ImmutableFieldProvider implements AchievementContainer
{
    public static final String TOTAL_PROGRESSION_TEXT_FIELD = "totalProgressionText";
    public static final String TOTAL_PROGRESSION_VALUE_FIELD = "totalProgressionValue";
    public static final String LAST_COMPLETED_ACHIEVEMENT_FIELD = "lastCompletedAchievement";
    public static final String ROOT_CATEGORIES_FIELD = "rootCategories";
    public static final String SELECTED_FILTER_FIELD = "selectedFilter";
    public static final String FILTERS_FIELD = "filters";
    public static final String SELECTED_ROOT_CATEGORY_FIELD = "selectedRootCategory";
    public static final String IS_QUEST_VIEW_FIELD = "isQuestView";
    public static final String QUEST_INVENTORY_FIELD = "questInventory";
    public static final String[] FIELDS;
    protected final TIntObjectHashMap<AchievementCategoryView> m_rootCategories;
    protected final ArrayList<AchievementCategoryView> m_sortedRootCategories;
    protected AchievementCategoryView m_selectedRootCategory;
    protected ClientAchievementsContext m_achievementsContext;
    protected AchievementsHistoryCategoryView m_historyCategoryView;
    protected long m_characterId;
    
    protected AsbtractAchievementsView(final long characterId) {
        super();
        this.m_rootCategories = new TIntObjectHashMap<AchievementCategoryView>();
        this.m_sortedRootCategories = new ArrayList<AchievementCategoryView>();
        this.m_characterId = characterId;
    }
    
    @Override
    public String[] getFields() {
        return AsbtractAchievementsView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("totalProgressionText")) {
            return this.countCompletedAchievements() + "/" + this.getAchievementsTotalSize();
        }
        if (fieldName.equals("totalProgressionValue")) {
            return this.countCompletedAchievements() / this.getAchievementsTotalSize();
        }
        if (fieldName.equals("lastCompletedAchievement")) {
            return this.getLastCompletedAchievement();
        }
        if (fieldName.equals("rootCategories")) {
            return this.m_sortedRootCategories;
        }
        if (fieldName.equals("filters")) {
            return UIAchievementsFrame.AchievementFilter.values();
        }
        if (fieldName.equals("selectedFilter")) {
            return UIAchievementsFrame.getInstance().getCurrentFilter();
        }
        if (fieldName.equals("selectedRootCategory")) {
            return this.m_selectedRootCategory;
        }
        return null;
    }
    
    public abstract boolean isQuestView();
    
    public AchievementView getLastCompletedAchievement() {
        if (this.m_historyCategoryView == null) {
            return null;
        }
        final ArrayList<AchievementView> history = this.m_historyCategoryView.getAchievementFilteredList();
        return (history.size() > 0) ? history.get(history.size() - 1) : null;
    }
    
    @Override
    public int getAchievementsTotalSize() {
        int count = 0;
        final TIntObjectIterator<Achievement> it = this.m_achievementsContext.getAchievementIterator();
        while (it.hasNext()) {
            it.advance();
            final Achievement achievement = it.value();
            final boolean isQuest = AchievementsViewManager.INSTANCE.isQuest(this.m_characterId, achievement.getId());
            if (!isQuest) {
                if (!achievement.isVisible(WakfuGameEntity.getInstance().getLocalPlayer())) {
                    continue;
                }
                ++count;
            }
        }
        return count;
    }
    
    @Override
    public int countCompletedAchievements() {
        int count = 0;
        final TIntObjectIterator<Achievement> it = this.m_achievementsContext.getAchievementIterator();
        while (it.hasNext()) {
            it.advance();
            final Achievement achievement = it.value();
            final boolean isQuest = AchievementsViewManager.INSTANCE.isQuest(this.m_characterId, achievement.getId());
            if (!isQuest) {
                if (!achievement.isVisible(WakfuGameEntity.getInstance().getLocalPlayer())) {
                    continue;
                }
                if (!this.m_achievementsContext.isAchievementComplete(achievement.getId())) {
                    continue;
                }
                ++count;
            }
        }
        return count;
    }
    
    @Override
    public boolean contains(final AchievementView achievementView) {
        return false;
    }
    
    public AchievementsHistoryCategoryView getHistoryCategoryView() {
        return this.m_historyCategoryView;
    }
    
    public AchievementCategoryView getRootCategoryFromId(final int categoryId) {
        return this.m_rootCategories.get(categoryId);
    }
    
    public AchievementCategoryView getSelectedRootCategory() {
        return this.m_selectedRootCategory;
    }
    
    public void setSelectedRootCategory(final AchievementCategoryView selectedRootCategory) {
        if (this.m_selectedRootCategory == selectedRootCategory) {
            return;
        }
        this.m_selectedRootCategory = selectedRootCategory;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedRootCategory");
    }
    
    public AchievementCategoryView getCategoryFromId(final int categoryId) {
        return this.getCategoryFromId(categoryId, this.m_rootCategories);
    }
    
    public AchievementCategoryView getCategoryFromId(final int categoryId, final TIntObjectHashMap categories) {
        if (categories == null) {
            return null;
        }
        final TIntObjectIterator<AchievementCategoryView> iterator = categories.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            final AchievementCategoryView value = iterator.value();
            if (value.getId() == categoryId) {
                return value;
            }
            final AchievementCategoryView categoryView = this.getCategoryFromId(categoryId, value.getChildren());
            if (categoryView != null) {
                return categoryView;
            }
        }
        return null;
    }
    
    static {
        FIELDS = new String[] { "totalProgressionText", "totalProgressionValue", "lastCompletedAchievement", "rootCategories", "selectedFilter", "filters", "selectedRootCategory", "isQuestView", "questInventory" };
    }
}
