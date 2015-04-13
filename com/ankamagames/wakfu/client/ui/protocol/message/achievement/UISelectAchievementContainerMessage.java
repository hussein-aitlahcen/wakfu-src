package com.ankamagames.wakfu.client.ui.protocol.message.achievement;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;

public class UISelectAchievementContainerMessage extends UIMessage
{
    private final AchievementCategoryView m_achievementCategoryView;
    
    public UISelectAchievementContainerMessage(final AchievementCategoryView achievementCategoryView) {
        super();
        this.m_achievementCategoryView = achievementCategoryView;
    }
    
    @Override
    public int getId() {
        return 16136;
    }
    
    public AchievementCategoryView getAchievementCategoryView() {
        return this.m_achievementCategoryView;
    }
}
