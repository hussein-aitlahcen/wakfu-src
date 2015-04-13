package com.ankamagames.wakfu.client.core.game.challenge;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import gnu.trove.*;

public class ChallengeData
{
    private static Logger m_logger;
    public TByteLongHashMap m_varValues;
    public TIntByteHashMap m_actionStatus;
    private Point3 m_target;
    private byte m_state;
    public ChallengeDataModel m_model;
    private GameDateConst m_startDate;
    private boolean m_isProposed;
    private boolean m_isFinished;
    private boolean m_isLaunched;
    private boolean m_isActivated;
    private boolean m_isFailed;
    private boolean m_isSuccess;
    private ChallengeReward m_selectedReward;
    private ChallengeReward[] m_rewards;
    private ChallengeReward[] m_failures;
    private ChallengeReward m_finalReward;
    private short m_ranking;
    private String m_winnerName;
    private int m_winnerScore;
    private int m_endReason;
    private int m_entityCount;
    private int m_protectorId;
    private AbstractChallengeView m_view;
    
    public ChallengeData() {
        super();
        this.m_varValues = new TByteLongHashMap();
        this.m_actionStatus = new TIntByteHashMap();
        this.m_ranking = -1;
    }
    
    public ChallengeData copy() {
        final ChallengeData data = new ChallengeData();
        if (this.m_varValues != null) {
            final TByteLongIterator it = this.m_varValues.iterator();
            while (it.hasNext()) {
                it.advance();
                data.m_varValues.put(it.key(), it.value());
            }
        }
        if (this.m_actionStatus != null) {
            final TIntByteIterator it2 = this.m_actionStatus.iterator();
            while (it2.hasNext()) {
                it2.advance();
                data.m_actionStatus.put(it2.key(), it2.value());
            }
        }
        data.m_target = this.m_target;
        data.m_state = this.m_state;
        data.m_model = this.m_model;
        data.m_startDate = this.m_startDate;
        data.m_isProposed = this.m_isProposed;
        data.m_isFinished = this.m_isFinished;
        data.m_isLaunched = this.m_isLaunched;
        data.m_isActivated = this.m_isActivated;
        data.m_isFailed = this.m_isFailed;
        data.m_isSuccess = this.m_isSuccess;
        data.m_selectedReward = this.m_selectedReward;
        if (this.m_rewards != null) {
            data.m_rewards = new ChallengeReward[this.m_rewards.length];
            for (int i = 0, size = this.m_rewards.length; i < size; ++i) {
                data.m_rewards[i] = this.m_rewards[i];
            }
        }
        if (this.m_failures != null) {
            data.m_failures = new ChallengeReward[this.m_failures.length];
            for (int i = 0, size = this.m_failures.length; i < size; ++i) {
                data.m_failures[i] = this.m_failures[i];
            }
        }
        data.m_finalReward = this.m_finalReward;
        data.m_ranking = this.m_ranking;
        data.m_winnerName = this.m_winnerName;
        data.m_winnerScore = this.m_winnerScore;
        data.m_endReason = this.m_endReason;
        data.m_entityCount = this.m_entityCount;
        data.m_protectorId = this.m_protectorId;
        return data;
    }
    
    public ChallengeDataModel getModel() {
        return this.m_model;
    }
    
    public int getId() {
        return this.m_model.getId();
    }
    
    public ChallengeCategory getCategory() {
        return this.m_model.getCategory();
    }
    
    public ChallengeUsersType getUserType() {
        return this.m_model.getUserType();
    }
    
    public byte getState() {
        return this.m_state;
    }
    
    public void setState(final byte state) {
        this.m_state = state;
    }
    
    public boolean isProposed() {
        return this.m_isProposed;
    }
    
    public void setProposed(final boolean proposed) {
        this.m_isProposed = proposed;
    }
    
    public boolean islaunched() {
        return this.m_isLaunched;
    }
    
    public void setLaunched(final boolean launched) {
        this.m_isLaunched = launched;
    }
    
    public boolean isFinished() {
        return this.m_isFinished;
    }
    
    public void setFinished(final boolean finished) {
        if (this.m_isFinished == finished) {
            return;
        }
        this.m_isFinished = finished;
    }
    
    public ChallengeReward getSelectedReward() {
        return this.m_selectedReward;
    }
    
    public void setSelectedReward(final ChallengeReward selectedReward) {
        this.m_selectedReward = selectedReward;
    }
    
    public static ChallengeData fromDataModel(final ChallengeDataModel model) {
        final ChallengeData data = new ChallengeData();
        data.m_model = model;
        final ArrayList<ChallengeRewardModel> rewards = data.m_model.getRewards();
        data.m_rewards = new ChallengeReward[rewards.size()];
        for (int i = 0; i < rewards.size(); ++i) {
            data.m_rewards[i] = new ChallengeReward(rewards.get(i));
        }
        final ArrayList<ChallengeRewardModel> failures = data.m_model.getFailures();
        if (failures != null) {
            data.m_failures = new ChallengeReward[failures.size()];
            for (int j = 0; j < failures.size(); ++j) {
                data.m_failures[j] = new ChallengeReward(failures.get(j));
            }
        }
        else {
            data.m_failures = null;
        }
        data.m_view = ChallengeViewManager.INSTANCE.getChallengeView(model.getId());
        return data;
    }
    
    public byte getGoalStatus(final ChallengeGoalData goal) {
        assert goal != null : "On essaye de r\u00e9cup\u00e9rer le status d'un goal null";
        return this.m_actionStatus.get(goal.getId());
    }
    
    public ChallengeReward[] getRewards() {
        return this.m_rewards;
    }
    
    public ChallengeReward[] getFailures() {
        return this.m_failures;
    }
    
    public void activateActions(final TIntArrayList actionsId) {
        for (int i = 0; i < actionsId.size(); ++i) {
            final int actionId = actionsId.get(i);
            if (this.m_actionStatus.get(actionId) != 1) {
                this.m_actionStatus.put(actionId, (byte)1);
            }
        }
        if (this.m_view != null) {
            this.m_view.updateGoals();
        }
    }
    
    public boolean setVar(final byte varId, final long varValue) {
        final boolean change = this.m_varValues.containsKey(varId) && this.m_varValues.get(varId) != varValue;
        this.m_varValues.put(varId, varValue);
        if (this.m_view != null) {
            this.m_view.updateGoals();
        }
        return change;
    }
    
    public long getVarValue(final String name) {
        return this.m_varValues.get(this.m_model.getVarId(name));
    }
    
    public String getTitle() {
        return this.m_model.getChallengeTitle();
    }
    
    public void completeAction(final int actionId) {
        if (this.m_actionStatus.get(actionId) != 1) {
            ChallengeData.m_logger.error((Object)("On essaie de terminer une action pas encore commenc\u00e9e, dans le scenario " + this.getId() + " action d'id " + actionId));
        }
        this.m_actionStatus.put(actionId, (byte)2);
        if (this.m_view != null) {
            this.m_view.updateGoals();
        }
    }
    
    public void setValidRewards(final TIntArrayList validRewards) {
        if (this.m_selectedReward != null && (validRewards == null || !validRewards.contains(this.m_selectedReward.getId()))) {
            this.m_selectedReward = null;
        }
        for (final ChallengeReward reward : this.m_rewards) {
            if (validRewards != null && validRewards.contains(reward.getId())) {
                reward.setValid(true);
                if (this.m_selectedReward == null) {
                    this.m_selectedReward = reward;
                }
            }
            else {
                reward.setValid(false);
            }
        }
    }
    
    public boolean isExpired() {
        final GameDateConst expiryDate = this.m_model.getExpiryDate();
        final GameDateConst currentDate = WakfuGameCalendar.getInstance().getDate();
        if (expiryDate != null && expiryDate.before(currentDate)) {
            return true;
        }
        if (this.m_startDate == null) {
            return false;
        }
        final short duration = this.m_model.getDuration();
        if (duration > 0) {
            final GameDate endDate = new GameDate(this.m_startDate);
            endDate.add(GameInterval.fromSeconds(duration));
            if (endDate.before(currentDate)) {
                return true;
            }
        }
        return false;
    }
    
    public short getRemainingTime() {
        if (this.isFailed() || this.isFinished()) {
            return 0;
        }
        if (this.m_model.getDuration() == 0) {
            return -1;
        }
        if (this.m_startDate == null) {
            return this.m_model.getDuration();
        }
        final GameDate endDate = new GameDate(this.m_startDate);
        endDate.add(GameInterval.fromSeconds(this.m_model.getDuration()));
        final GameInterval remainingTime = WakfuGameCalendar.getInstance().getDate().timeTo(endDate);
        return (short)(remainingTime.isPositive() ? ((short)remainingTime.toSeconds()) : 0);
    }
    
    public void setActivated(final boolean activated) {
        this.m_isActivated = activated;
    }
    
    public void setStartDate(final GameDateConst startDate) {
        this.m_startDate = startDate;
    }
    
    public boolean isChaos() {
        return this.m_model.isChaos();
    }
    
    public boolean isSuccess() {
        return this.m_isSuccess;
    }
    
    public void setSuccess(final boolean success) {
        this.m_isSuccess = success;
    }
    
    public boolean isTimed() {
        return this.m_model.getDuration() > 0;
    }
    
    public boolean isActivated() {
        return this.m_isActivated;
    }
    
    public boolean hasTarget() {
        return this.m_target != null;
    }
    
    public boolean hasNoneRewardValid() {
        for (final ChallengeReward reward : this.m_rewards) {
            if (reward.isValid()) {
                return false;
            }
        }
        return true;
    }
    
    public Point3 getTarget() {
        return this.m_target;
    }
    
    public void setTarget(final Point3 target) {
        if (target == null) {
            MapManager.getInstance().removeCompassPointAndPositionMarker();
        }
        else {
            MapManager.getInstance().addCompassPointAndPositionMarker(target.getX(), target.getY(), target.getZ(), WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId(), this, true);
        }
    }
    
    public void setFailed(final boolean failed) {
        this.m_isFailed = failed;
    }
    
    public boolean isFailed() {
        return this.m_isFailed;
    }
    
    public short getDuration() {
        return this.m_model.getDuration();
    }
    
    public GameDateConst getStartDate() {
        return this.m_startDate;
    }
    
    public int getProtectorId() {
        return this.m_protectorId;
    }
    
    public void setProtectorId(final int protectorId) {
        this.m_protectorId = protectorId;
    }
    
    public short getRanking() {
        return this.m_ranking;
    }
    
    public void setRanking(final short ranking) {
        this.m_ranking = ranking;
        if (this.m_view != null) {
            this.m_view.updateRanking();
        }
    }
    
    @Override
    public String toString() {
        return this.getModel().getChallengeTitle();
    }
    
    public ChallengeReward getReward(final int rank) {
        if (this.m_rewards.length > rank) {
            return this.m_rewards[rank];
        }
        return null;
    }
    
    public void setFinalReward(final int index) {
        this.m_finalReward = ((index >= 0 && index < this.m_rewards.length) ? this.m_rewards[index] : null);
        if (this.m_view != null) {
            this.m_view.updateFinalReward();
        }
    }
    
    public ChallengeReward getFinalReward() {
        return this.m_finalReward;
    }
    
    public String getWinnerName() {
        return this.m_winnerName;
    }
    
    public void setWinnerName(final String winnerName) {
        this.m_winnerName = winnerName;
        if (this.m_view != null) {
            this.m_view.updateWinnerName();
        }
    }
    
    public int getWinnerScore() {
        return this.m_winnerScore;
    }
    
    public void setWinnerScore(final int winnerScore) {
        this.m_winnerScore = winnerScore;
        if (this.m_view != null) {
            this.m_view.updateWinnerScore();
        }
    }
    
    public int getEndReason() {
        return this.m_endReason;
    }
    
    public void setEndReason(final int endReason) {
        this.m_endReason = endReason;
    }
    
    public int getEntityCount() {
        return this.m_entityCount;
    }
    
    public void setEntityCount(final int entityCount) {
        this.m_entityCount = entityCount;
    }
    
    public void activateClock(final GameDateConst startDate) {
        if (!startDate.isNull()) {
            this.m_startDate = startDate;
            this.m_isActivated = true;
            MessageScheduler.getInstance().addClock(UIChallengeFrame.getInstance(), 1000L, this.getId());
        }
    }
    
    static {
        ChallengeData.m_logger = Logger.getLogger((Class)ChallengeData.class);
    }
}
