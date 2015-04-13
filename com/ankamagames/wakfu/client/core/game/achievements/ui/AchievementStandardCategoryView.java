package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import gnu.trove.*;

public class AchievementStandardCategoryView extends AchievementCategoryView implements AchievementContainer
{
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String ROOT_ICON_URL_FIELD = "rootIconUrl";
    public static final String PROGRESSION_TEXT_FIELD = "progressionText";
    public static final String PROGRESSION_VALUE_FIELD = "progressionValue";
    public static final String IS_FOLLOWED = "isFollowed";
    public static final String IS_COMPLETED = "isCompleted";
    public static final String IS_VISIBLE = "isVisible";
    public static final String[] FIELDS;
    private final Category m_category;
    private final TIntObjectHashMap<AchievementStandardCategoryView> m_subCategories;
    private final ArrayList<AchievementView> m_achievements;
    private final int m_rootCategoryId;
    
    public AchievementStandardCategoryView(final long characterId, final Category category, final int rootCategoryId) {
        super(characterId);
        this.m_subCategories = new TIntObjectHashMap<AchievementStandardCategoryView>();
        this.m_achievements = new ArrayList<AchievementView>();
        this.m_category = category;
        this.m_selectedSubCategoryId = category.getId();
        this.m_rootCategoryId = rootCategoryId;
    }
    
    public void addAchievement(final AchievementView achievementView) {
        if (!this.m_achievements.contains(achievementView)) {
            this.m_achievements.add(achievementView);
        }
    }
    
    public ArrayList<AchievementView> getAchievements() {
        return this.m_achievements;
    }
    
    @Override
    public String[] getFields() {
        return AchievementStandardCategoryView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getAchievementCategoryIconUrl(this.m_category.getId());
        }
        if (fieldName.equals("rootIconUrl")) {
            return WakfuConfiguration.getInstance().getAchievementRootCategoryIconUrl("background" + this.m_category.getId());
        }
        if (fieldName.equals("progressionText")) {
            return this.countCompletedAchievements() + "/" + this.getAchievementsTotalSize();
        }
        if (fieldName.equals("progressionValue")) {
            return this.countCompletedAchievements() / this.getAchievementsTotalSize();
        }
        if (fieldName.equals("isFollowed")) {
            return this.isFollowed();
        }
        if (fieldName.equals("isCompleted")) {
            for (int i = this.m_achievements.size() - 1; i >= 0; --i) {
                if (!this.m_context.isAchievementComplete(this.m_achievements.get(i).getId())) {
                    return false;
                }
            }
            return true;
        }
        if (fieldName.equals("categories")) {
            return this.getSubCategories();
        }
        return super.getFieldValue(fieldName);
    }
    
    private boolean isFollowed() {
        for (int i = this.m_achievements.size() - 1; i >= 0; --i) {
            if (this.m_achievements.get(i).isFollowed()) {
                return true;
            }
        }
        return false;
    }
    
    protected AchievementCategoryView getSelectedFilterCategory() {
        return this.m_subCategories.get(this.m_selectedSubCategoryId);
    }
    
    @Override
    protected String getName() {
        return (this.m_category == null) ? WakfuTranslator.getInstance().getString("achievement.rootName") : WakfuTranslator.getInstance().getString(61, this.m_category.getId(), new Object[0]);
    }
    
    protected ArrayList<AchievementStandardCategoryView> getSubCategories() {
        final ArrayList<AchievementStandardCategoryView> achievementStandardCategoryViews = new ArrayList<AchievementStandardCategoryView>();
        final TIntObjectIterator<AchievementStandardCategoryView> it = this.m_subCategories.iterator();
        while (it.hasNext()) {
            it.advance();
            final AchievementStandardCategoryView categoryView = it.value();
            if (!categoryView.getAchievementFilteredList().isEmpty()) {
                achievementStandardCategoryViews.add(categoryView);
            }
        }
        Collections.sort(achievementStandardCategoryViews, new Comparator<AchievementStandardCategoryView>() {
            @Override
            public int compare(final AchievementStandardCategoryView o1, final AchievementStandardCategoryView o2) {
                if (o1.getId() == AchievementStandardCategoryView.this.getId()) {
                    return -1;
                }
                if (o2.getId() == AchievementStandardCategoryView.this.getId()) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        return achievementStandardCategoryViews;
    }
    
    @Override
    public boolean isHistory() {
        return false;
    }
    
    protected boolean isParent() {
        return this.m_subCategories != null && this.m_subCategories.size() > 0;
    }
    
    @Override
    public void addChildren(final AchievementStandardCategoryView achievementCategoryView) {
        this.m_subCategories.put(achievementCategoryView.getId(), achievementCategoryView);
    }
    
    @Override
    public TIntObjectHashMap<AchievementStandardCategoryView> getChildren() {
        return this.m_subCategories;
    }
    
    @Override
    protected ArrayList<AchievementView> getAchievementFilteredList() {
        final ArrayList<AchievementView> achievements = new ArrayList<AchievementView>();
        for (final AchievementView achievementView : this.m_achievements) {
            final int id = achievementView.getId();
            final AchievementStandardCategoryView selectedCategory = this.m_subCategories.get(this.m_selectedSubCategoryId);
            if (selectedCategory != null && !selectedCategory.equals(this) && !selectedCategory.contains(achievementView)) {
                continue;
            }
            final boolean active = this.m_context.isAchievementActive(id);
            final boolean complete = this.m_context.isAchievementComplete(id);
            final UIAchievementsFrame.AchievementFilter currentFilter = UIAchievementsFrame.getInstance().getCurrentFilter();
            if (!currentFilter.isAuthorized(active, complete)) {
                continue;
            }
            if (!achievementView.isVisible()) {
                continue;
            }
            achievements.add(achievementView);
        }
        return achievements;
    }
    
    public int getRootCategoryId() {
        return this.m_rootCategoryId;
    }
    
    @Override
    public int getAchievementsTotalSize() {
        int count = 0;
        for (int i = this.m_achievements.size() - 1; i >= 0; --i) {
            final AchievementView achievement = this.m_achievements.get(i);
            if (achievement.isInCount()) {
                ++count;
            }
        }
        return count;
    }
    
    @Override
    public int countCompletedAchievements() {
        int count = 0;
        for (final AchievementView achievementView : this.m_achievements) {
            if (this.m_context.isAchievementComplete(achievementView.getId())) {
                ++count;
            }
        }
        return count;
    }
    
    @Override
    public boolean contains(final AchievementView achievementView) {
        return this.m_achievements.contains(achievementView);
    }
    
    @Override
    public int getId() {
        return this.m_category.getId();
    }
    
    public AchievementView updateFollowedField(final int achievementId) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isFollowed");
        for (int i = 0, size = this.m_achievements.size(); i < size; ++i) {
            final AchievementView achievementView = this.m_achievements.get(i);
            if (achievementView.getId() == achievementId) {
                achievementView.updateFollowedField();
                return achievementView;
            }
        }
        return null;
    }
    
    static {
        FIELDS = new String[] { "name", "achievements", "iconUrl", "progressionText", "progressionValue", "isHistory", "achievementsQuickList" };
    }
}
