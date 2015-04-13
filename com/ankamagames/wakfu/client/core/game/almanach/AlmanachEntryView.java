package com.ankamagames.wakfu.client.core.game.almanach;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.almanach.*;

public class AlmanachEntryView extends ImmutableFieldProvider
{
    public static final String QUEST = "quest";
    private final int m_id;
    
    public AlmanachEntryView(final int id) {
        super();
        this.m_id = id;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("quest")) {
            final AlmanachEntry entry = AlmanachEntryManager.INSTANCE.getEntry(this.m_id);
            return entry.isNull() ? null : AchievementsViewManager.INSTANCE.getAchievement(WakfuGameEntity.getInstance().getLocalPlayer().getId(), entry.getAchievementId());
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AlmanachEntryView");
        sb.append("{m_id=").append(this.m_id);
        sb.append('}');
        return sb.toString();
    }
}
