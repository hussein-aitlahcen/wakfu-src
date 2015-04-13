package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.aptitude.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class AchievementRewardView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String NAME_FIELD = "description";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String QUANTITY = "quantity";
    public static final String[] FIELDS;
    private final Reward m_reward;
    
    public AchievementRewardView(final Reward reward) {
        super();
        this.m_reward = reward;
    }
    
    @Override
    public String[] getFields() {
        return AchievementRewardView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("description")) {
            final int[] parameters = this.m_reward.getParameters();
            final int param1 = (parameters != null && parameters.length > 0) ? parameters[0] : 0;
            final RewardType rewardType = this.m_reward.getType();
            if (rewardType == AchievementRewardType.GIVE_PET_EQUIPMENT || rewardType == AchievementRewardType.GIVE_PLAYER_ITEM) {
                final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(param1);
                return referenceItem.getName();
            }
            if (rewardType == AchievementRewardType.GIVE_PLAYER_TITLE) {
                return WakfuTranslator.getInstance().getString("unlockedTitle", WakfuTranslator.getInstance().getString(34, param1, new Object[0]));
            }
            if (rewardType == AchievementRewardType.GIVE_PLAYER_EMOTE) {
                return WakfuTranslator.getInstance().getString(80, param1, new Object[0]);
            }
            if (rewardType == AchievementRewardType.GIVE_PLAYER_LANDMARK) {
                return WakfuTranslator.getInstance().getString("landmark.gfx");
            }
            if (rewardType == AchievementRewardType.GIVE_PLAYER_XP) {
                return WakfuTranslator.getInstance().getString("xpGain", param1);
            }
            if (rewardType == AchievementRewardType.GIVE_PLAYER_XP_IN_PERCENT) {
                return WakfuTranslator.getInstance().getString("xpPercentGain", param1);
            }
            if (rewardType == AchievementRewardType.GIVE_PLAYER_APTITUDE_POINTS) {
                final byte aptitudeTypeId = (byte)param1;
                final int nbPoints = this.m_reward.getParameters()[1];
                final AptitudeType aptitudeType = AptitudeType.getFromId(aptitudeTypeId);
                final String key = "aptitude.points.gain." + aptitudeType;
                return WakfuTranslator.getInstance().getString(key, nbPoints);
            }
            if (rewardType == AchievementRewardType.SPELLS_RESTAT) {
                return WakfuTranslator.getInstance().getString("reward.spellRestat");
            }
            if (rewardType == AchievementRewardType.APPLY_STATE_EFFECTS) {
                return WakfuTranslator.getInstance().getString(8, this.m_reward.getParameters()[0], new Object[0]);
            }
            if (rewardType == AchievementRewardType.LEARN_RECIPE) {
                return WakfuTranslator.getInstance().getString("reward.learnRecipe");
            }
            if (rewardType == AchievementRewardType.GIVE_KAMAS) {
                return WakfuTranslator.getInstance().getString("kama.shortGain", param1);
            }
            if (rewardType == AchievementRewardType.GIVE_GUILD_POINTS) {
                return WakfuTranslator.getInstance().getString("guild.pointsGain", param1);
            }
            return null;
        }
        else {
            if (fieldName.equals("iconUrl")) {
                final int[] parameters = this.m_reward.getParameters();
                final int objectId = (parameters != null && parameters.length > 0) ? parameters[0] : 0;
                final AchievementRewardType rewardType2 = (AchievementRewardType)this.m_reward.getType();
                switch (rewardType2) {
                    case GIVE_PET_EQUIPMENT:
                    case GIVE_PLAYER_ITEM: {
                        final RefItemFieldProvider referenceItem2 = (RefItemFieldProvider)ReferenceItemManager.getInstance().getReferenceItem(objectId);
                        return referenceItem2.getReferenceItemDisplayer().getFieldValue("iconUrl");
                    }
                    case GIVE_PLAYER_TITLE: {
                        return WakfuConfiguration.getInstance().getTitleIconUrl(objectId);
                    }
                    case GIVE_PLAYER_EMOTE: {
                        return WakfuConfiguration.getInstance().getEmoteUrl(objectId);
                    }
                    case GIVE_PLAYER_LANDMARK: {
                        return WakfuConfiguration.getInstance().getIconUrl("pointsOfInterestIconPath", "defaultIconPath", objectId);
                    }
                    default: {
                        try {
                            return String.format(WakfuConfiguration.getInstance().getString("rewardTypeIconsPath"), rewardType2.getId());
                        }
                        catch (PropertyException e) {
                            AchievementRewardView.m_logger.warn((Object)e.getMessage(), (Throwable)e);
                            return null;
                        }
                        break;
                    }
                }
            }
            if (!fieldName.equals("quantity")) {
                return null;
            }
            if (this.m_reward.getType() == AchievementRewardType.GIVE_PLAYER_ITEM || this.m_reward.getType() == AchievementRewardType.GIVE_PET_EQUIPMENT) {
                final int quantity = this.m_reward.getParameters()[1];
                return quantity;
            }
            return 1;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AchievementRewardView.class);
        FIELDS = new String[] { "description", "iconUrl" };
    }
}
