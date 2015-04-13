package com.ankamagames.wakfu.client.core.game.challenge;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.challenge.*;
import java.util.*;
import java.util.regex.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.constants.*;
import org.apache.commons.lang3.*;

public abstract class AbstractChallengeView extends AbstractQuestView
{
    private static final Logger m_logger;
    public static final String SCORE = "score";
    public static final String FIRST_RANK_REWARD = "firstRankReward";
    public static final String SECOND_RANK_REWARD = "secondRankReward";
    public static final String THIRD_RANK_REWARD = "thirdRankReward";
    public static final String FINAL_REWARD = "finalReward";
    public static final String HAS_REWARD = "hasRewards";
    public static final String HAS_MORE_THAN_ONE_REWARD = "hasMoreThanOneReward";
    public static final String RESULT_DESCRIPTION_ICON_URL = "resultDescriptionIconUrl";
    private long m_score;
    private ChallengeReward m_reward;
    
    public AbstractChallengeView() {
        super();
        this.m_reward = null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("score")) {
            return this.m_score;
        }
        if (fieldName.equals("resultDescriptionIconUrl")) {
            final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.getChallengeId());
            final ChallengeData data = this.getChallengeData();
            if (model == null || !model.getUserType().isDisplayRanking()) {
                return WakfuConfiguration.getInstance().getIconUrl("challengeResultQualityIconsPath", "defaultIconPath", ChallengeRankingQuality.TOP.getId());
            }
            final short ranking = data.getRanking();
            if (ranking <= 0) {
                return WakfuConfiguration.getInstance().getIconUrl("challengeResultQualityIconsPath", "defaultIconPath", ChallengeRankingQuality.POOR.getId());
            }
            if (ranking == 1) {
                return WakfuConfiguration.getInstance().getIconUrl("challengeResultQualityIconsPath", "defaultIconPath", ChallengeRankingQuality.TOP.getId());
            }
            final float ratio = (ranking - 3) / (data.getEntityCount() - 3);
            if (ranking == 2 || ranking == 3) {
                return WakfuConfiguration.getInstance().getIconUrl("challengeResultQualityIconsPath", "defaultIconPath", ChallengeRankingQuality.VERY_GOOD.getId());
            }
            if (ratio <= 0.5) {
                return WakfuConfiguration.getInstance().getIconUrl("challengeResultQualityIconsPath", "defaultIconPath", ChallengeRankingQuality.MEDIUM.getId());
            }
            return WakfuConfiguration.getInstance().getIconUrl("challengeResultQualityIconsPath", "defaultIconPath", ChallengeRankingQuality.POOR.getId());
        }
        else if (fieldName.equals("firstRankReward")) {
            if (isMoneyChallenge(this.getChallengeId())) {
                return new MoneyChallengeReward();
            }
            final ChallengeData data2 = this.getChallengeData();
            if (data2 != null) {
                return data2.getReward(0);
            }
            final ChallengeDataModel model2 = ChallengeManager.getInstance().getChallengeDataModel(this.getChallengeId());
            final ArrayList<ChallengeRewardModel> models = model2.getRewards();
            if (models == null || models.size() < 1) {
                return null;
            }
            final ChallengeRewardModel rewardModel = models.get(0);
            return ChallengeViewManager.INSTANCE.getChallengeReward(rewardModel.getId(), rewardModel);
        }
        else if (fieldName.equals("secondRankReward")) {
            final ChallengeData data2 = this.getChallengeData();
            if (data2 != null) {
                return data2.getReward(1);
            }
            final ChallengeDataModel model2 = ChallengeManager.getInstance().getChallengeDataModel(this.getChallengeId());
            final ArrayList<ChallengeRewardModel> models = model2.getRewards();
            if (models == null || models.size() < 2) {
                return null;
            }
            final ChallengeRewardModel rewardModel = models.get(1);
            return ChallengeViewManager.INSTANCE.getChallengeReward(rewardModel.getId(), rewardModel);
        }
        else if (fieldName.equals("thirdRankReward")) {
            final ChallengeData data2 = this.getChallengeData();
            if (data2 != null) {
                return data2.getReward(2);
            }
            final ChallengeDataModel model2 = ChallengeManager.getInstance().getChallengeDataModel(this.getChallengeId());
            final ArrayList<ChallengeRewardModel> models = model2.getRewards();
            if (models == null || models.size() < 3) {
                return null;
            }
            final ChallengeRewardModel rewardModel = models.get(2);
            return ChallengeViewManager.INSTANCE.getChallengeReward(rewardModel.getId(), rewardModel);
        }
        else if (fieldName.equals("finalReward")) {
            if (isMoneyChallenge(this.getChallengeId())) {
                return new MoneyChallengeReward();
            }
            final ChallengeData data2 = this.getChallengeData();
            return data2.getFinalReward();
        }
        else {
            if (fieldName.equals("hasRewards")) {
                final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.getChallengeId());
                return !model.getRewards().isEmpty();
            }
            if (fieldName.equals("hasMoreThanOneReward")) {
                final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.getChallengeId());
                return model.getRewards().size() > 1;
            }
            return super.getFieldValue(fieldName);
        }
    }
    
    public void computeScore() {
        final ChallengeGoalData goal = this.getDisplayedGoal(this.getChallengeId());
        if (goal != null) {
            if (goal.getJaugeVarName() != null && goal.getJaugeVarName().length() > 0) {
                this.m_score = this.getChallengeData().getVarValue(goal.getJaugeVarName());
            }
            final String rawText = goal.getFormattedString();
            final Matcher match = ChallengeGoalView.GOAL_VAR_PATTERN.matcher(rawText);
            if (match.find()) {
                this.m_score = this.getChallengeData().getVarValue(match.group().substring(1, match.group().length() - 1));
            }
        }
    }
    
    @Override
    protected String getName() {
        final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.getChallengeId());
        return model.getChallengeTitle();
    }
    
    @Override
    protected String getIconUrl() {
        final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.getChallengeId());
        try {
            return String.format(WakfuConfiguration.getInstance().getString("challengeCategoryIconsPath"), model.getCategory().getId());
        }
        catch (PropertyException e) {
            AbstractChallengeView.m_logger.warn((Object)e.getMessage());
            return null;
        }
    }
    
    @Override
    public int getId() {
        return this.getChallengeId();
    }
    
    @Override
    public int getType() {
        return 2;
    }
    
    @Override
    protected String getRemainingTime() {
        final ChallengeData data = this.getChallengeData();
        if (data == null) {
            return null;
        }
        final short remaining = data.getRemainingTime();
        return (remaining > 0) ? formatTime(remaining) : null;
    }
    
    @Override
    protected ArrayList<AbstractQuestGoalView> getGoals() {
        final int challengeId = this.getChallengeId();
        final ChallengeGoalData goal = this.getDisplayedGoal(challengeId);
        final ArrayList<AbstractQuestGoalView> list = new ArrayList<AbstractQuestGoalView>();
        if (goal != null) {
            final ChallengeGoalView view = ChallengeViewManager.INSTANCE.getChallengeGoalView(challengeId, goal.getId());
            list.add(view);
        }
        else {
            list.add(new ChallengeGenericGoalView(challengeId));
        }
        return list;
    }
    
    private ChallengeGoalData getDisplayedGoal(final int challengeId) {
        ChallengeGoalData goal = ChallengeManager.getInstance().getFirstLoadedGoal(challengeId);
        if (goal == null) {
            goal = ChallengeManager.getInstance().getLastCompletedGoal(challengeId);
        }
        return goal;
    }
    
    @Override
    protected String getStyle() {
        final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.getChallengeId());
        return "challengeCategory" + model.getCategory().getId();
    }
    
    @Override
    protected String getRanking() {
        final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.getChallengeId());
        if (model == null || !model.getUserType().isDisplayRanking()) {
            return null;
        }
        final ChallengeData data = this.getChallengeData();
        if (data == null) {
            return null;
        }
        final short ranking = data.getRanking();
        if (ranking == -2) {
            return WakfuTranslator.getInstance().getString("challenge.ranking.loser");
        }
        if (ranking == -1) {
            return WakfuTranslator.getInstance().getString("challenge.ranking.unknown");
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(ranking).append("/").append(data.getEntityCount());
        return sb.finishAndToString();
    }
    
    private static String formatTime(final short time) {
        final int minutes = time / 60;
        final int seconds = time - 60 * minutes;
        return String.format("%d:%s", minutes, (seconds > 9) ? seconds : ("0" + seconds));
    }
    
    public void updateProperty() {
        this.updateGoals();
        this.updateRanking();
        this.updateRemainingTime();
    }
    
    public void updateFinalReward() {
    }
    
    public void updateWinnerName() {
    }
    
    public void updateWinnerScore() {
    }
    
    protected abstract ChallengeData getChallengeData();
    
    public abstract int getChallengeId();
    
    @Override
    protected String getBackgroundText() {
        final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.getChallengeId());
        return model.getChallengeLongDescription();
    }
    
    private static boolean isMoneyChallenge(final int challengeId) {
        return ArrayUtils.contains(ChallengeConstants.MONEY_CHALLENGES, challengeId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractChallengeView.class);
    }
}
