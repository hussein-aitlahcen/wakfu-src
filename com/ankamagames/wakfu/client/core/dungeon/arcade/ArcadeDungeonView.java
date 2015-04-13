package com.ankamagames.wakfu.client.core.dungeon.arcade;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.dungeon.loader.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.dungeon.rewards.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.dungeon.ranks.*;
import gnu.trove.*;

public class ArcadeDungeonView extends ImmutableFieldProvider implements Runnable, ScoreProvider
{
    public static final String NAME_FIELD = "name";
    public static final String CURRENT_SCORE_FIELD = "currentScore";
    public static final String CHRONO_FIELD = "chrono";
    public static final String ROUND_NUMBER_FIELD = "roundNumber";
    public static final String CHALLENGES_FIELD = "challenges";
    public static final String REWARDS_FIELD = "rewards";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String[] FIELDS;
    private short m_roundCount;
    private int m_currentScore;
    private final TIntObjectHashMap<DungeonChallengeView> m_challenges;
    private DungeonDefinition m_dungeonDefinition;
    private long m_startTime;
    private int m_challengeTotalScore;
    private int m_challengeCompletedCount;
    private int m_monsterCompletedCount;
    private int m_monsterTotalScore;
    private int m_eventCompletedCount;
    private int m_eventTotalScore;
    private short m_waveCount;
    
    public ArcadeDungeonView(final DungeonDefinition dungeonDefinition) {
        super();
        this.m_challenges = new TIntObjectHashMap<DungeonChallengeView>();
        (this.m_dungeonDefinition = dungeonDefinition).foreachChallenge(new TObjectProcedure<ChallengeDefinition>() {
            @Override
            public boolean execute(final ChallengeDefinition challenge) {
                ArcadeDungeonView.this.m_challenges.put(challenge.getId(), new DungeonChallengeView(challenge));
                return true;
            }
        });
        this.m_roundCount = 0;
        this.m_currentScore = 0;
    }
    
    public void startChrono() {
        this.m_startTime = System.currentTimeMillis();
        ProcessScheduler.getInstance().schedule(this, 1000L);
    }
    
    public void stopChrono() {
        ProcessScheduler.getInstance().remove(this);
    }
    
    @Override
    public String[] getFields() {
        return ArcadeDungeonView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_dungeonDefinition.getName();
        }
        if (fieldName.equals("currentScore")) {
            return WakfuTranslator.getInstance().getString("arcadeDungeon.points", this.m_currentScore);
        }
        if (fieldName.equals("chrono")) {
            final long remainingSeconds = this.getRemainingTime();
            if (remainingSeconds > 0L) {
                return formatTime(remainingSeconds);
            }
            return "-";
        }
        else {
            if (fieldName.equals("roundNumber")) {
                return this.m_roundCount;
            }
            if (fieldName.equals("challenges")) {
                final Object[] values = this.m_challenges.getValues();
                return (values.length == 0) ? null : values;
            }
            if (fieldName.equals("rewards")) {
                return this.getRewards();
            }
            if (fieldName.equals("description")) {
                return this.m_dungeonDefinition.getDescription();
            }
            return null;
        }
    }
    
    private ArrayList<RewardView> getRewards() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final int localPlayerLevelAtStart = UIArcadeDungeonFrame.getInstance().getLocalPlayerLevelAtStart();
        final ArrayList<RewardView> rewards = new ArrayList<RewardView>();
        this.m_dungeonDefinition.forEachReward(new TObjectProcedure<Reward>() {
            @Override
            public boolean execute(final Reward reward) {
                rewards.add(new RewardView(reward));
                return true;
            }
        }, (localPlayerLevelAtStart == -1) ? localPlayer.getLevel() : localPlayerLevelAtStart);
        return (rewards.size() > 0) ? rewards : null;
    }
    
    public int getRewardsSize() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final int localPlayerLevelAtStart = UIArcadeDungeonFrame.getInstance().getLocalPlayerLevelAtStart();
        final ArrayList<RewardView> rewards = new ArrayList<RewardView>();
        this.m_dungeonDefinition.forEachValidReward(new TObjectProcedure<Reward>() {
            @Override
            public boolean execute(final Reward reward) {
                rewards.add(new RewardView(reward));
                return true;
            }
        }, (localPlayerLevelAtStart == -1) ? localPlayer.getLevel() : localPlayerLevelAtStart, this);
        return rewards.size();
    }
    
    public static String formatTime(final long time) {
        final long minutes = time / 60L;
        final long seconds = time - 60L * minutes;
        return String.format("%d:%s", minutes, (seconds > 9L) ? seconds : ("0" + seconds));
    }
    
    private long getRemainingTime() {
        return (System.currentTimeMillis() - this.m_startTime) / 1000L;
    }
    
    public void onRoundBegin() {
        ++this.m_roundCount;
        this.m_waveCount = 0;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "roundNumber");
        this.updateChallengesScore();
    }
    
    public int getRoundCount() {
        return this.m_roundCount;
    }
    
    @Override
    public void run() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "chrono");
    }
    
    public DungeonChallengeView getChallenge(final int challengeId) {
        return this.m_challenges.get(challengeId);
    }
    
    public void addMonsterScore(final int score) {
        ++this.m_monsterCompletedCount;
        this.m_monsterTotalScore += score;
    }
    
    public void addEventScore(final int score) {
        ++this.m_eventCompletedCount;
        this.m_eventTotalScore += score;
    }
    
    public void addChallengeScore(final int score) {
        ++this.m_challengeCompletedCount;
        this.m_challengeTotalScore += score;
    }
    
    public void setTotalScore(final int totalScore) {
        this.m_currentScore = totalScore;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentScore");
    }
    
    private void updateChallengesScore() {
        final TIntObjectIterator<DungeonChallengeView> it = this.m_challenges.iterator();
        while (it.hasNext()) {
            it.advance();
            PropertiesProvider.getInstance().firePropertyValueChanged(it.value(), "score");
        }
    }
    
    public int calculateTotalScore() {
        return this.m_monsterTotalScore + this.m_eventTotalScore + this.m_challengeTotalScore;
    }
    
    public int getChallengeTotalScore() {
        return this.m_challengeTotalScore;
    }
    
    public int getChallengeCompletedCount() {
        return this.m_challengeCompletedCount;
    }
    
    public int getMonsterCompletedCount() {
        return this.m_monsterCompletedCount;
    }
    
    public int getMonsterTotalScore() {
        return this.m_monsterTotalScore;
    }
    
    public int getEventCompletedCount() {
        return this.m_eventCompletedCount;
    }
    
    public int getEventTotalScore() {
        return this.m_eventTotalScore;
    }
    
    public int getCurrentRoundTotalScore() {
        return this.m_dungeonDefinition.getRoundScore(MathHelper.clamp(this.m_roundCount - 1, 0, 32767));
    }
    
    @Override
    public int getTotalScore() {
        return this.m_currentScore;
    }
    
    @Override
    public int getMonsterScore() {
        return this.getMonsterTotalScore();
    }
    
    @Override
    public int getChallengeScore() {
        return this.getChallengeTotalScore();
    }
    
    public int getCurrentScore() {
        return this.m_currentScore;
    }
    
    public void onNewWave() {
        ++this.m_waveCount;
    }
    
    public short getWaveCount() {
        return this.m_waveCount;
    }
    
    public Rank getRank() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return this.m_dungeonDefinition.getRank(localPlayer.getLevel(), this);
    }
    
    static {
        FIELDS = new String[] { "name", "currentScore", "chrono", "roundNumber", "challenges", "rewards", "description" };
    }
}
