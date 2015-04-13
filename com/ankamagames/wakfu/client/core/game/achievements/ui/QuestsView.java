package com.ankamagames.wakfu.client.core.game.achievements.ui;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.common.constants.*;
import org.apache.commons.lang3.*;
import java.util.*;

public class QuestsView extends AsbtractAchievementsView
{
    private static final Logger m_logger;
    
    public QuestsView(final long characterId, final ClientAchievementsContext context) {
        super(characterId);
        this.m_achievementsContext = context;
        final ArrayList<Category> rootCategories = AchievementsModel.INSTANCE.getRootCategories();
        Category questCategory = null;
        for (final Category category : rootCategories) {
            final int id = category.getId();
            if (id == 4) {
                questCategory = category;
                break;
            }
        }
        if (questCategory == null) {
            QuestsView.m_logger.error((Object)"Probl\u00e8me d'export, pas de cat\u00e9gorie des qu\u00eates !!!");
            return;
        }
        final ArrayList<Category> children = questCategory.getChildren();
        if (children == null) {
            QuestsView.m_logger.error((Object)"Probl\u00e8me d'export, la cat\u00e9gorie des qu\u00eates est vide !!!");
            return;
        }
        for (final Category subCategory : children) {
            final int subId = subCategory.getId();
            final boolean visible = !ArrayUtils.contains(AchievementConstants.HIDDEN_QUEST_CATEGORIES, subId);
            AchievementsViewManager.INSTANCE.createCategoryView(this.m_characterId, subCategory, null);
            if (visible) {
                final AchievementCategoryView categoryView = AchievementsViewManager.INSTANCE.getCategory(this.m_characterId, subCategory.getId());
                this.m_rootCategories.put(subId, categoryView);
                this.m_sortedRootCategories.add(categoryView);
            }
        }
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
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AchievementsView.class);
    }
}
