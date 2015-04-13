package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class AchievementListView extends ImmutableFieldProvider
{
    public static final String QUESTS = "quests";
    public static final String ACHIEVEMENT_INDEX_TEXT = "achievementIndexText";
    private final ArrayList<AchievementView> m_achievementsViews;
    private int m_currentListId;
    private int m_currentAchievementIndex;
    
    public AchievementListView() {
        super();
        this.m_achievementsViews = new ArrayList<AchievementView>();
        this.m_currentListId = -1;
        this.m_currentAchievementIndex = -1;
    }
    
    public void setAchievementList(final AchievementList list) {
        if (list.getId() == this.m_currentListId) {
            return;
        }
        this.m_currentListId = list.getId();
        this.m_achievementsViews.clear();
        for (int i = 0, size = list.size(); i < size; ++i) {
            this.m_achievementsViews.add(AchievementsViewManager.INSTANCE.getAchievement(UIAchievementsFrame.getInstance().getCharacterId(), list.getAchievement(i)));
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "quests");
    }
    
    public void setCurrentAchievement(final int id) {
        if (this.m_currentListId == -1) {
            return;
        }
        this.m_currentAchievementIndex = -1;
        for (int i = 0, size = this.m_achievementsViews.size(); i < size; ++i) {
            final AchievementView achievementView = this.m_achievementsViews.get(i);
            if (achievementView.getId() == id) {
                this.m_currentAchievementIndex = i;
                break;
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "achievementIndexText");
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("quests")) {
            return this.m_achievementsViews;
        }
        if (fieldName.equals("achievementIndexText")) {
            return String.valueOf(this.m_currentAchievementIndex + 1);
        }
        return null;
    }
}
