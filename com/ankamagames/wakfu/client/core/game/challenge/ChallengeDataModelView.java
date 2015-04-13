package com.ankamagames.wakfu.client.core.game.challenge;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.fileFormat.properties.*;
import java.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class ChallengeDataModelView implements FieldProvider
{
    protected final ChallengeDataModel m_model;
    public static final String IS_MODEL_FIELD = "isModel";
    public static final String IS_CURRENT_FIELD = "isCurrent";
    public static final String TYPE_FIELD = "type";
    public static final String TITLE_FIELD = "title";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String LONG_DESCRIPTION_FIELD = "longDescription";
    public static final String EXPIRY_FIELD = "expiry";
    public static final String DURATION_FIELD = "duration";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String REQUIREMENTS_FIELD = "requirements";
    public static final String MEETS_REQUIREMENTS_FIELD = "meetsRequirements";
    public static final String STATE_FIELD = "state";
    public static final String REWARDS_FIELD = "rewards";
    public static final String FIRST_RANK_REWARD_FIELD = "firstRankReward";
    public static final String SECOND_RANK_REWARD_FIELD = "secondRankReward";
    public static final String THIRD_RANK_REWARD_FIELD = "thirdRankReward";
    public static final String MIN_USERS_FIELD = "minUsers";
    public static final String MAX_USERS_FIELD = "maxUsers";
    public static final String IS_SELECTED_BY_PROTECTOR_FIELD = "isSelectedByProtector";
    public static final String HAS_REWARDS = "hasRewards";
    public static final String[] FIELDS;
    private boolean m_currentChallenge;
    protected static final Logger m_logger;
    
    public ChallengeDataModelView(final ChallengeDataModel model) {
        super();
        this.m_currentChallenge = false;
        this.m_model = model;
    }
    
    public ChallengeDataModel getModel() {
        return this.m_model;
    }
    
    @Override
    public String[] getFields() {
        return ChallengeDataModelView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isModel")) {
            return true;
        }
        if (fieldName.equals("isCurrent")) {
            return this.m_currentChallenge;
        }
        if (fieldName.equals("type")) {
            return this.m_model.getCategory();
        }
        if (fieldName.equals("title")) {
            final String title = this.m_model.getChallengeTitle();
            if (title != null && title.length() == 0) {
                return null;
            }
            return title;
        }
        else {
            if (fieldName.equals("description")) {
                return this.getGoalDescription();
            }
            if (fieldName.equals("longDescription")) {
                final String desc = this.m_model.getChallengeLongDescription();
                if (desc == null || desc.length() == 0) {
                    return null;
                }
                return "\"" + desc + "\"";
            }
            else if (fieldName.equals("requirements")) {
                final String requirement = this.m_model.getChallengeRequirementsText();
                if (requirement != null && requirement.length() == 0) {
                    return null;
                }
                final TextWidgetFormater sb = new TextWidgetFormater();
                if (!this.isJoinCriterionValid()) {
                    sb.openText().addColor(Color.RED.getRGBtoHex());
                }
                sb.append(requirement);
                return sb.finishAndToString();
            }
            else {
                if (fieldName.equals("meetsRequirements")) {
                    return this.isJoinCriterionValid();
                }
                if (fieldName.equals("expiry")) {
                    return this.getExpiryDateText();
                }
                if (fieldName.equals("duration")) {
                    if (this.m_model.getDuration() > 0) {
                        return String.format("%d:00:00", this.m_model.getDuration());
                    }
                }
                else {
                    if (fieldName.equals("iconUrl")) {
                        return this.getIconUrl();
                    }
                    if (fieldName.equals("state")) {
                        return this.m_model.getState();
                    }
                    if (fieldName.equals("hasRewards")) {
                        return this.m_model.getRewards().size() != 0;
                    }
                    if (fieldName.equals("rewards")) {
                        final ArrayList<ChallengeRewardModel> rewardsModel = this.m_model.getRewards();
                        final ArrayList<ChallengeReward> rewards = new ArrayList<ChallengeReward>();
                        for (int i = 0, size = rewardsModel.size(); i < size; ++i) {
                            rewards.add(new ChallengeReward(rewardsModel.get(i)));
                        }
                        return rewards;
                    }
                    if (fieldName.equals("firstRankReward")) {
                        final ArrayList<ChallengeRewardModel> models = this.m_model.getRewards();
                        if (models == null || models.size() < 1) {
                            return null;
                        }
                        return new ChallengeReward(models.get(0));
                    }
                    else if (fieldName.equals("secondRankReward")) {
                        final ArrayList<ChallengeRewardModel> models = this.m_model.getRewards();
                        if (models == null || models.size() < 2) {
                            return null;
                        }
                        return new ChallengeReward(models.get(1));
                    }
                    else if (fieldName.equals("thirdRankReward")) {
                        final ArrayList<ChallengeRewardModel> models = this.m_model.getRewards();
                        if (models == null || models.size() < 3) {
                            return null;
                        }
                        return new ChallengeReward(models.get(2));
                    }
                    else {
                        if (fieldName.equals("minUsers")) {
                            return this.m_model.getMinUsers();
                        }
                        if (fieldName.equals("maxUsers")) {
                            return this.m_model.getMaxUsers();
                        }
                        if (fieldName.equals("isSelectedByProtector")) {
                            return ProtectorView.getInstance().getProtector().getSelectedChallengeList().contains(this.m_model.getId());
                        }
                    }
                }
                return null;
            }
        }
    }
    
    protected Object getGoalDescription() {
        final String desc = this.m_model.getChallengeDescription();
        if (desc != null && desc.length() == 0) {
            return null;
        }
        return desc;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public void setAsCurrentChallenge(final boolean current) {
        if (this.m_currentChallenge != current) {
            this.m_currentChallenge = current;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "isCurrent");
        }
    }
    
    public String getIconUrl() {
        try {
            return String.format(WakfuConfiguration.getInstance().getString("challengeCategoryIconsPath"), this.m_model.getCategory().getId());
        }
        catch (PropertyException e) {
            ChallengeDataModelView.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    public String getExpiryDateText() {
        if (this.m_model.getExpiryDate() != null) {
            final DateFormat dateFormatter = DateFormat.getDateTimeInstance(1, 2, WakfuTranslator.getInstance().getLanguage().getActualLocale());
            return String.format("%s %s", WakfuTranslator.getInstance().getString("expire.in"), dateFormatter.format(this.m_model.getExpiryDate()));
        }
        return null;
    }
    
    public ChallengeCategory getCategory() {
        return this.m_model.getCategory();
    }
    
    public ChallengeUsersType getUserType() {
        return this.m_model.getUserType();
    }
    
    public boolean isJoinCriterionValid() {
        final SimpleCriterion crit = this.m_model.getJoinCriterion();
        return crit == null || crit.isValid(WakfuGameEntity.getInstance().getLocalPlayer(), WakfuGameEntity.getInstance().getLocalPlayer(), null, null);
    }
    
    static {
        FIELDS = new String[] { "isModel", "isCurrent", "type", "title", "description", "expiry", "duration", "iconUrl", "requirements", "meetsRequirements", "state", "rewards", "hasRewards" };
        m_logger = Logger.getLogger((Class)ChallengeDataModelView.class);
    }
}
