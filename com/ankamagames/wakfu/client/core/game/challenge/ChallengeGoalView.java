package com.ankamagames.wakfu.client.core.game.challenge;

import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.common.game.challenge.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.java.util.*;
import java.util.regex.*;

public class ChallengeGoalView extends AbstractQuestGoalView
{
    public static final Pattern GOAL_VAR_PATTERN;
    public static final Pattern GOAL_BREED_PATTERN;
    public static final Pattern GOAL_RESOURCE_PATTERN;
    private final int m_challengeId;
    private final int m_goalId;
    
    public ChallengeGoalView(final int challengeId, final int goalId) {
        super();
        this.m_goalId = goalId;
        this.m_challengeId = challengeId;
    }
    
    @Override
    protected String getDescription() {
        final ChallengeData challengeData = ChallengeManager.getInstance().getChallengeData(this.m_challengeId);
        final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.m_challengeId);
        final ChallengeGoalData goalData = model.getGoal(this.m_goalId);
        long score = 0L;
        final String jaugeVarName = goalData.getJaugeVarName();
        if (challengeData != null && jaugeVarName != null && jaugeVarName.length() > 0) {
            score = challengeData.getVarValue(jaugeVarName);
        }
        if (score == 0L || challengeData.isProposed()) {
            return getGoalDescription(model);
        }
        if (challengeData.getGoalStatus(goalData) == 1) {
            final String text = getGoalText(goalData, challengeData, model);
            if (text != null) {
                return text;
            }
        }
        return null;
    }
    
    @Override
    protected long getProgressionValue() {
        return -1L;
    }
    
    @Override
    protected boolean isCompleted() {
        final ChallengeData challengeData = ChallengeManager.getInstance().getChallengeData(this.m_challengeId);
        final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.m_challengeId);
        final ChallengeGoalData goalData = model.getGoal(this.m_goalId);
        return challengeData == null || challengeData.getGoalStatus(goalData) == 2;
    }
    
    @Override
    protected boolean isFailed() {
        return false;
    }
    
    protected static String getGoalDescription(final ChallengeDataModel model) {
        final String desc = model.getChallengeDescription();
        if (desc != null && desc.length() == 0) {
            return null;
        }
        return desc;
    }
    
    public static String getGoalText(final ChallengeGoalData goal, final ChallengeData data, final ChallengeDataModel model) {
        String rawText = goal.getFormattedString();
        if (rawText == null || rawText.length() == 0) {
            return null;
        }
        rawText = (String)ChallengeFormatter.extractValue(rawText, ChallengeFormatter.Type.STRING, model.getParams());
        for (Matcher match = ChallengeGoalView.GOAL_BREED_PATTERN.matcher(rawText); match.find(); match = ChallengeGoalView.GOAL_BREED_PATTERN.matcher(rawText)) {
            final String name = WakfuTranslator.getInstance().getString(7, PrimitiveConverter.getInteger(match.group(1)), new Object[0]);
            rawText = match.replaceFirst(name);
        }
        for (Matcher match = ChallengeGoalView.GOAL_RESOURCE_PATTERN.matcher(rawText); match.find(); match = ChallengeGoalView.GOAL_RESOURCE_PATTERN.matcher(rawText)) {
            final String name = WakfuTranslator.getInstance().getString(12, PrimitiveConverter.getInteger(match.group(1)), new Object[0]);
            rawText = match.replaceFirst(name);
        }
        for (Matcher match = ChallengeGoalView.GOAL_VAR_PATTERN.matcher(rawText); match.find(); match = ChallengeGoalView.GOAL_VAR_PATTERN.matcher(rawText)) {
            final Long value = data.getVarValue(match.group().substring(1, match.group().length() - 1));
            rawText = match.replaceFirst(value.toString());
        }
        return rawText;
    }
    
    @Override
    protected boolean isCompassed() {
        return false;
    }
    
    @Override
    protected boolean canBeCompassed() {
        return false;
    }
    
    @Override
    protected boolean canBeCompassedNow() {
        return false;
    }
    
    static {
        GOAL_VAR_PATTERN = Pattern.compile("\\[(([a-z]|[A-Z]|[0-9])+)\\]");
        GOAL_BREED_PATTERN = Pattern.compile("\\[b=([0-9]+)\\]");
        GOAL_RESOURCE_PATTERN = Pattern.compile("\\[r=([0-9]+)\\]");
    }
}
