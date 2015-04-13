package com.ankamagames.wakfu.client.ui.protocol.message.achievement;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;

public class UISelectAchievementMessage extends UIMessage
{
    private final AchievementView m_achievementView;
    
    public UISelectAchievementMessage(final AchievementView achievementView) {
        super();
        this.m_achievementView = achievementView;
    }
    
    @Override
    public int getId() {
        return 16137;
    }
    
    public AchievementView getAchievementView() {
        return this.m_achievementView;
    }
}
