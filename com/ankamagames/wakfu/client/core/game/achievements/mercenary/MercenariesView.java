package com.ankamagames.wakfu.client.core.game.achievements.mercenary;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import java.util.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.achievement.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.google.common.collect.*;
import org.jetbrains.annotations.*;
import com.google.common.base.*;

public class MercenariesView extends ImmutableFieldProvider
{
    public static final String ACHIEVEMENTS = "achievements";
    public static final String FILTERS = "filters";
    public static final String SELECTED_FILTER = "selectedFilter";
    private final int m_achievementCategoryId;
    private final Collection<AchievementView> m_views;
    private final List<AchievementView> m_filteredViews;
    private final List<MercenaryFilterView> m_filters;
    private static final Ordering<AchievementView> ORDERING;
    private AchievementView m_selectedAchievement;
    private MercenaryFilterView m_selectedFilter;
    
    public MercenariesView(final int achievementCategoryId) {
        super();
        this.m_views = new ArrayList<AchievementView>();
        this.m_filteredViews = new ArrayList<AchievementView>();
        this.m_filters = new ArrayList<MercenaryFilterView>();
        this.m_achievementCategoryId = achievementCategoryId;
        this.m_selectedAchievement = null;
        this.init();
    }
    
    private void init() {
        for (final MercenaryFilter filter : MercenaryFilter.values()) {
            this.m_filters.add(MercenaryFilterView.getView(filter));
        }
        this.m_selectedFilter = this.m_filters.get(0);
        final AchievementStandardCategoryView category = (AchievementStandardCategoryView)AchievementsViewManager.INSTANCE.getCategory(WakfuGameEntity.getInstance().getLocalPlayer().getId(), this.m_achievementCategoryId);
        this.m_views.addAll(category.getAchievements());
        this.filterViews();
    }
    
    private void filterViews() {
        this.m_filteredViews.clear();
        for (final AchievementView view : this.m_views) {
            if (this.m_selectedFilter.getFilter().isValid(view.getId())) {
                this.m_filteredViews.add(view);
            }
        }
        Collections.sort(this.m_filteredViews, (Comparator<? super AchievementView>)MercenariesView.ORDERING);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "achievements");
        if (!this.m_filteredViews.contains(this.m_selectedAchievement)) {
            this.setSelectedAchievement(this.m_filteredViews.isEmpty() ? null : this.m_filteredViews.get(0));
        }
    }
    
    public void activateSelectedAchievement() {
        if (this.m_selectedAchievement == null) {
            return;
        }
        final Message msg = new AchievementActivationAnswerMessage(this.m_selectedAchievement.getId(), -1L, true);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    public void setSelectedAchievement(final AchievementView achievement) {
        if (this.m_selectedAchievement == achievement) {
            return;
        }
        this.m_selectedAchievement = achievement;
        if (Xulor.getInstance().isLoaded("mercenaryDialog")) {
            PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", this.m_selectedAchievement, "mercenaryDialog");
        }
    }
    
    public AchievementView getSelectedAchievement() {
        return this.m_selectedAchievement;
    }
    
    public void setSelectedFilter(final MercenaryFilterView filter) {
        if (this.m_selectedFilter == filter) {
            return;
        }
        this.m_selectedFilter = filter;
        this.filterViews();
    }
    
    @Override
    public String[] getFields() {
        return MercenariesView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("achievements")) {
            return ImmutableList.copyOf((Collection)this.m_filteredViews);
        }
        if (fieldName.equals("filters")) {
            return ImmutableList.copyOf((Collection)this.m_filters);
        }
        if (fieldName.equals("selectedFilter")) {
            return this.m_selectedFilter;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "MercenariesView{m_achievementCategoryId=" + this.m_achievementCategoryId + ", m_views=" + this.m_views + ", m_filteredViews=" + this.m_filteredViews + ", m_filters=" + this.m_filters + ", m_selectedAchievement=" + this.m_selectedAchievement + ", m_selectedFilter=" + this.m_selectedFilter + '}';
    }
    
    static {
        ORDERING = Ordering.natural().onResultOf((Function)new Function<AchievementView, Integer>() {
            @javax.annotation.Nullable
            public Integer apply(@javax.annotation.Nullable final AchievementView input) {
                return (input == null) ? 0 : input.getOrder();
            }
        });
    }
}
