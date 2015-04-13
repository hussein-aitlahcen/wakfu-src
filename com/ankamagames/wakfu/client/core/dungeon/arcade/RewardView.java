package com.ankamagames.wakfu.client.core.dungeon.arcade;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.dungeon.rewards.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.*;

public class RewardView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String DESCRIPTION_FIELD = "description";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String[] FIELDS;
    private Reward m_reward;
    
    public RewardView(final Reward reward) {
        super();
        this.m_reward = reward;
    }
    
    @Override
    public String[] getFields() {
        return RewardView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("description")) {
            return this.getDescription();
        }
        if (!fieldName.equals("iconUrl")) {
            return null;
        }
        if (this.m_reward.getType() == Reward.Type.Item) {
            final ReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_reward.getValue());
            return referenceItem.getFieldValue(fieldName);
        }
        if (this.m_reward.getType() == Reward.Type.Xp) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("rewardTypeIconsPath"), AchievementRewardType.GIVE_PLAYER_XP.getId());
            }
            catch (PropertyException e) {
                RewardView.m_logger.warn((Object)e.getMessage(), (Throwable)e);
                return null;
            }
        }
        return null;
    }
    
    public String getDescription() {
        if (this.m_reward.getType() == Reward.Type.Item) {
            final ReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_reward.getValue());
            return referenceItem.getName();
        }
        return WakfuTranslator.getInstance().getString("xpGain", this.m_reward.getValue());
    }
    
    public Reward getReward() {
        return this.m_reward;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RewardView.class);
        FIELDS = new String[] { "description", "iconUrl" };
    }
}
