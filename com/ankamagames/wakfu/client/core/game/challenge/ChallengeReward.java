package com.ankamagames.wakfu.client.core.game.challenge;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class ChallengeReward extends AbstractChallengeReward
{
    private static final Logger m_logger;
    private ChallengeRewardModel m_model;
    private boolean m_valid;
    
    public ChallengeReward(final ChallengeRewardModel model) {
        super();
        this.m_valid = true;
        this.m_model = model;
    }
    
    public int getId() {
        return this.m_model.getId();
    }
    
    @Override
    public String[] getFields() {
        return ChallengeReward.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("item")) {
            return ReferenceItemManager.getInstance().getReferenceItem(this.m_model.getItemId());
        }
        if (fieldName.equals("quantity")) {
            return this.m_model.getQuantity();
        }
        if (fieldName.equals("xp")) {
            if (this.m_model.getXp() <= 0) {
                return null;
            }
            return WakfuTranslator.getInstance().getString("xpGain", this.m_model.getXp());
        }
        else {
            if (fieldName.equals("kama")) {
                return WakfuTranslator.getInstance().getString("kama.gain", this.m_model.getKama());
            }
            if (fieldName.equals("xpIconUrl")) {
                return this.getXpIconUrl();
            }
            if (fieldName.equals("rankDescription")) {
                return WakfuTranslator.getInstance().getString("challenge.reward.rank." + this.m_model.getOrder());
            }
            return null;
        }
    }
    
    @Override
    protected AbstractReferenceItem getItem() {
        return ReferenceItemManager.getInstance().getReferenceItem(this.m_model.getItemId());
    }
    
    @Override
    protected int getQuantity() {
        return this.m_model.getQuantity();
    }
    
    @Override
    protected int getXp() {
        return this.m_model.getXp();
    }
    
    @Override
    protected int getKama() {
        return this.m_model.getKama();
    }
    
    @Override
    protected String getXpIconUrl() {
        try {
            return String.format(WakfuConfiguration.getInstance().getString("rewardTypeIconsPath"), AchievementRewardType.GIVE_PLAYER_XP.getId());
        }
        catch (PropertyException e) {
            ChallengeReward.m_logger.warn((Object)e.getMessage(), (Throwable)e);
            return null;
        }
    }
    
    @Override
    protected String getRankDescription() {
        return WakfuTranslator.getInstance().getString("challenge.reward.rank." + this.m_model.getOrder());
    }
    
    public void setValid(final boolean valid) {
        this.m_valid = valid;
    }
    
    public boolean isValid() {
        return this.m_valid;
    }
    
    public boolean isSuccess() {
        return this.m_model.isSuccess();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChallengeReward.class);
    }
}
