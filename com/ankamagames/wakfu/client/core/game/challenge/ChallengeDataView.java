package com.ankamagames.wakfu.client.core.game.challenge;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class ChallengeDataView extends ChallengeDataModelView
{
    protected static final Logger m_logger;
    private static final long CHALLENGE_MASK = 131072L;
    public static final String REMAINING_TIME_FIELD = "remainingTime";
    public static final String REWARDS_FIELD = "rewards";
    public static final String FAILURES_FIELD = "failures";
    public static final String GOAL_FIELD = "goal";
    public static final String IS_FINISHED_FIELD = "isFinished";
    public static final String IS_LAUNCHED_FIELD = "isLaunched";
    public static final String IS_ACTIVATED_FIELD = "isActivated";
    public static final String IS_FAILED_FIELD = "isFailed";
    public static final String SELECTED_REWARD_FIELD = "selectedReward";
    public static final String IS_PROPOSED_FIELD = "isProposed";
    public static final String NEED_ACTIVATION = "needActivation";
    public static final String COMPASS = "compass";
    public static final String STATE_TEXT_FIELD = "stateText";
    public static final String COLORED_TITLE_FIELD = "coloredTitle";
    public static final String[] DATA_FIELDS;
    public static final String[] ALL_FIELDS;
    protected ChallengeData m_data;
    private String m_linkedDialogId;
    private byte m_varForInputId;
    
    public ChallengeDataView(final ChallengeData data) {
        super(data.getModel());
        this.m_data = data;
    }
    
    public ChallengeDataView(final ChallengeDataView challengeDataView) {
        super(challengeDataView.getModel());
        this.m_data = challengeDataView.getChallengeData();
    }
    
    public ChallengeData getChallengeData() {
        return this.m_data;
    }
    
    public String getLinkedDialogId() {
        return this.m_linkedDialogId;
    }
    
    public void setLinkedDialogId(final String linkedDialogId) {
        this.m_linkedDialogId = linkedDialogId;
    }
    
    @Override
    public String[] getFields() {
        return ChallengeDataView.ALL_FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isModel")) {
            return false;
        }
        if (fieldName.equals("goal")) {
            final ArrayList goalslist = new ArrayList();
            final ArrayList<ChallengeGoalData> goals = this.m_model.getGoals();
            for (int max = goals.size(), i = 0; i < max; ++i) {}
            return goalslist;
        }
        if (fieldName.equals("rewards")) {
            return (this.m_data.getRewards().length > 0) ? this.m_data.getRewards() : null;
        }
        if (fieldName.equals("failures")) {
            return (this.m_data.getFailures() != null && this.m_data.getFailures().length > 0) ? this.m_data.getFailures() : null;
        }
        if (fieldName.equals("isFinished")) {
            return this.m_data.isFinished();
        }
        if (fieldName.equals("isLaunched")) {
            return this.m_data.islaunched();
        }
        if (fieldName.equals("isActivated")) {
            return this.m_data.isActivated();
        }
        if (fieldName.equals("selectedReward")) {
            return this.m_data.getSelectedReward();
        }
        if (fieldName.equals("isProposed")) {
            return this.m_data.isProposed();
        }
        if (fieldName.equals("remainingTime")) {
            final short remaining = this.m_data.getRemainingTime();
            return (remaining > 0) ? formatTime(remaining) : null;
        }
        if (fieldName.equals("needActivation")) {
            return !this.m_data.isProposed() && !this.m_model.isAuto() && !this.m_data.isActivated();
        }
        if (fieldName.equals("isFailed")) {
            return !this.m_data.isFinished() && this.m_data.isFailed();
        }
        if (fieldName.equals("stateText")) {
            return this.getStateText();
        }
        if (!fieldName.equals("coloredTitle")) {
            return super.getFieldValue(fieldName);
        }
        final String title = this.m_model.getChallengeTitle();
        if (title != null && title.length() == 0) {
            return null;
        }
        if (this.isProposed()) {
            return "<text color=\"7ad1d6\">" + title + "</text>";
        }
        if (this.isLaunched()) {
            return title;
        }
        return "<text color=\"7ad1d6\">" + title + "</text>";
    }
    
    public static String formatTime(final short time) {
        final int minutes = time / 60;
        final int seconds = time - 60 * minutes;
        return String.format("%d:%s", minutes, (seconds > 9) ? seconds : ("0" + seconds));
    }
    
    public short getDuration() {
        return this.m_data.getDuration();
    }
    
    public void setStartDate(final GameDateConst date) {
        this.m_data.setStartDate(date);
    }
    
    public void setSelectedReward(final ChallengeReward reward) {
        this.m_data.setSelectedReward(reward);
    }
    
    public ChallengeReward getSelectedReward() {
        return this.m_data.getSelectedReward();
    }
    
    public boolean hasNoneRewardValid() {
        return this.m_data.hasNoneRewardValid();
    }
    
    @Override
    public ChallengeCategory getCategory() {
        return this.m_data.getCategory();
    }
    
    public boolean isFinished() {
        return this.m_data.isFinished();
    }
    
    public void setFinished(final boolean finished) {
        this.m_data.setFinished(finished);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isFinished");
    }
    
    public boolean isFailed() {
        return this.m_data.isFailed();
    }
    
    public void setFailed(final boolean failed) {
        this.m_data.setFailed(failed);
    }
    
    public boolean isActivated() {
        return this.m_data.isActivated();
    }
    
    public void setActivated(final boolean ac) {
        this.m_data.setActivated(ac);
    }
    
    public boolean isLaunched() {
        return this.m_data.islaunched();
    }
    
    public void setLaunched(final boolean launched) {
        this.m_data.setLaunched(launched);
    }
    
    public boolean isExpired() {
        return this.m_data.isExpired();
    }
    
    public boolean isProposed() {
        return this.m_data.isProposed();
    }
    
    public void setProposed(final boolean proposed) {
        this.m_data.setProposed(proposed);
    }
    
    public boolean isTimed() {
        return this.m_data.isTimed();
    }
    
    public int getId() {
        return this.m_data.getId();
    }
    
    public String getTitle() {
        return this.m_data.getTitle();
    }
    
    public String getStateText() {
        if (this.isProposed()) {
            return "<text color=\"7ad1d6\">" + WakfuTranslator.getInstance().getString("challenge.state.proposal") + "</text>";
        }
        if (this.isActivated()) {
            if (this.isLaunched()) {
                return "<text color=\"7ad1d6\">" + WakfuTranslator.getInstance().getString("challenge.state.activatedAndLaunched") + "</text>";
            }
            return "<text color=\"BB0606\">" + WakfuTranslator.getInstance().getString("challenge.state.activatedNotLaunched") + "</text>";
        }
        else {
            if (this.isFinished()) {
                return "<text color=\"14C800\">" + WakfuTranslator.getInstance().getString("challenge.state.complete") + "</text>";
            }
            if (this.isFailed()) {
                return "<text color=\"BB0606\">" + WakfuTranslator.getInstance().getString("challenge.state.failed") + "</text>";
            }
            if (this.isLaunched()) {
                return "<text color=\"BB0606\">" + WakfuTranslator.getInstance().getString("challenge.state.launchedWaitStart") + "</text>";
            }
            return "<text color=\"BB0606\">" + WakfuTranslator.getInstance().getString("challenge.state.activatedNotLaunched") + "</text>";
        }
    }
    
    public void updateProperty() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, ChallengeDataView.DATA_FIELDS);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, ChallengeDataView.FIELDS);
    }
    
    public void unloadLinkedDialog() {
        if (Xulor.getInstance().isLoaded(this.m_linkedDialogId)) {
            Xulor.getInstance().unload(this.m_linkedDialogId);
        }
    }
    
    public void updateTime() {
        if (Xulor.getInstance().isLoaded(this.m_linkedDialogId)) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "remainingTime");
        }
    }
    
    public void challengeChanged() {
        this.updateProperty();
    }
    
    public boolean isAreaChallenge() {
        return false;
    }
    
    @Override
    public String toString() {
        return this.getTitle();
    }
    
    public void setVarForInputId(final byte varId) {
        this.m_varForInputId = varId;
    }
    
    public byte getVarForInputId() {
        return this.m_varForInputId;
    }
    
    public void updateRanking() {
    }
    
    public void updateFinalReward() {
    }
    
    public void updateFirstRankReward() {
    }
    
    public void updateSecondRankReward() {
    }
    
    public void updateThirdRankReward() {
    }
    
    public void updateWinnerName() {
    }
    
    public void updateWinnerScore() {
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChallengeDataView.class);
        DATA_FIELDS = new String[] { "remainingTime", "rewards", "goal", "isFinished", "isLaunched", "isActivated", "isFailed", "selectedReward", "isProposed", "needActivation", "stateText", "coloredTitle" };
        ALL_FIELDS = new String[ChallengeDataView.DATA_FIELDS.length + PlayerCharacter.ALL_FIELDS.length];
        System.arraycopy(ChallengeDataView.DATA_FIELDS, 0, ChallengeDataView.ALL_FIELDS, 0, ChallengeDataView.DATA_FIELDS.length);
        System.arraycopy(ChallengeDataModelView.FIELDS, 0, ChallengeDataView.ALL_FIELDS, ChallengeDataView.DATA_FIELDS.length, ChallengeDataModelView.FIELDS.length);
    }
}
