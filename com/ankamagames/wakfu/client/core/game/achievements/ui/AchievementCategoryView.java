package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.framework.reflect.*;

public abstract class AchievementCategoryView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String CATEGORIES = "categories";
    public static final String ACHIEVEMENTS_FIELD = "achievements";
    public static final String IS_HISTORY_FIELD = "isHistory";
    public static final String ACHIEVEMENTS_QUICK_LIST_FIELD = "achievementsQuickList";
    protected int m_selectedSubCategoryId;
    protected long m_characterId;
    protected ClientAchievementsContext m_context;
    
    protected AchievementCategoryView(final long characterId) {
        super();
        this.m_characterId = characterId;
        this.m_context = AchievementContextManager.INSTANCE.getContext(characterId);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("isHistory")) {
            return this.isHistory();
        }
        if (fieldName.equals("achievements")) {
            return this.getAchievementFilteredList();
        }
        if (fieldName.equals("achievementsQuickList")) {
            return this.getAchievementQuickList();
        }
        return null;
    }
    
    public AchievementView getPreviousQuickListAchievement() {
        final ArrayList<AchievementView> viewArrayList = this.getAchievementFilteredList();
        final AchievementView achievementView = (AchievementView)PropertiesProvider.getInstance().getObjectProperty("selectedAchievement");
        if (achievementView == null) {
            return null;
        }
        int index = viewArrayList.indexOf(achievementView) - 1;
        if (index < 0) {
            index = viewArrayList.size() - 1;
        }
        return viewArrayList.get(index);
    }
    
    public AchievementView getNextQuickListAchievement() {
        final ArrayList<AchievementView> viewArrayList = this.getAchievementFilteredList();
        final AchievementView achievementView = (AchievementView)PropertiesProvider.getInstance().getObjectProperty("selectedAchievement");
        if (achievementView == null) {
            return null;
        }
        int index = viewArrayList.indexOf(achievementView) + 1;
        if (index > viewArrayList.size() - 1) {
            index = 0;
        }
        return viewArrayList.get(index);
    }
    
    public ArrayList<AchievementView> getAchievementQuickList() {
        final ArrayList<AchievementView> viewArrayList = this.getAchievementFilteredList();
        final AchievementView achievementView = (AchievementView)PropertiesProvider.getInstance().getObjectProperty("selectedAchievement");
        if (achievementView == null) {
            return null;
        }
        final int index = viewArrayList.indexOf(achievementView);
        if (index == -1) {
            return null;
        }
        final ArrayList<AchievementView> result = new ArrayList<AchievementView>();
        final int listSize = Math.min(3, viewArrayList.size());
        if (listSize < 1) {
            return null;
        }
        for (int i = index - listSize; i <= index + listSize; ++i) {
            int index2;
            if (i < 0) {
                index2 = viewArrayList.size() + i;
            }
            else if (i >= viewArrayList.size()) {
                index2 = i % viewArrayList.size();
            }
            else {
                index2 = i;
            }
            result.add(viewArrayList.get(index2));
        }
        return result;
    }
    
    protected abstract String getName();
    
    public abstract boolean isHistory();
    
    public abstract int getId();
    
    public abstract void addChildren(final AchievementStandardCategoryView p0);
    
    public abstract TIntObjectHashMap<AchievementStandardCategoryView> getChildren();
    
    protected abstract ArrayList<AchievementView> getAchievementFilteredList();
    
    public void setSelectedSubCategoryId(final int selectedSubCategoryId) {
        this.m_selectedSubCategoryId = selectedSubCategoryId;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "achievements", "achievementsQuickList");
    }
    
    public void updateAchievementListProperty() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "achievements", "achievementsQuickList");
    }
}
