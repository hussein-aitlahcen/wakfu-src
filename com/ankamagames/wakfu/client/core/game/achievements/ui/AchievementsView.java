package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import java.util.*;

public class AchievementsView extends AsbtractAchievementsView
{
    public AchievementsView(final long characterId, final ClientAchievementsContext context) {
        super(characterId);
        this.m_achievementsContext = context;
        final ArrayList<Category> rootCategories = AchievementsModel.INSTANCE.getRootCategories();
        for (final Category category : rootCategories) {
            final int id = category.getId();
            if (id == 4) {
                continue;
            }
            final AchievementStandardCategoryView categoryView = AchievementsViewManager.INSTANCE.createCategoryView(characterId, category, null);
            this.m_rootCategories.put(id, categoryView);
            this.m_sortedRootCategories.add(categoryView);
        }
        this.m_historyCategoryView = AchievementsViewManager.INSTANCE.createHistoryCategoryView(characterId);
        this.m_rootCategories.put(this.m_historyCategoryView.getId(), this.m_historyCategoryView);
        this.m_selectedRootCategory = this.m_sortedRootCategories.get(0);
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isQuestView")) {
            return this.isQuestView();
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public boolean isQuestView() {
        return false;
    }
}
