package com.ankamagames.wakfu.common.game.fight.time.timescore;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.time.buff.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class TimeScoreGaugesImpl implements TimeScoreGauges
{
    private static final Logger m_logger;
    private static final int NOT_ANY_INFO_ABOUT_TURN_DURATION = -1;
    private int m_timePointGap;
    private final TLongIntHashMap m_timeScoreGauges;
    private final TIntObjectHashMap<TimelineBuffList<WakfuEffect>> m_buffListsByTeamId;
    private final TIntObjectHashMap<List<WakfuEffect>> m_availableEffectsPerTeam;
    private final TLongObjectHashMap<WakfuEffect> m_effectToApplyByFighterId;
    private final TLongHashSet m_fighterNotAllowedToChoose;
    private final TLongObjectHashMap<ObjectPair<Integer, Integer>> m_fighterRemainingSeconds;
    
    public TimeScoreGaugesImpl() {
        super();
        this.m_timeScoreGauges = new TLongIntHashMap();
        this.m_buffListsByTeamId = new TIntObjectHashMap<TimelineBuffList<WakfuEffect>>();
        this.m_availableEffectsPerTeam = new TIntObjectHashMap<List<WakfuEffect>>();
        this.m_effectToApplyByFighterId = new TLongObjectHashMap<WakfuEffect>();
        this.m_fighterNotAllowedToChoose = new TLongHashSet();
        this.m_fighterRemainingSeconds = new TLongObjectHashMap<ObjectPair<Integer, Integer>>();
    }
    
    @Override
    public void setTeamEffects(final int teamId, final List<WakfuEffect> effects) {
        final TimelineBuffList<WakfuEffect> buffList = new TimelineBuffListImpl<WakfuEffect>();
        for (int i = 0, n = effects.size(); i < n; ++i) {
            final WakfuEffect wakfuEffect = effects.get(i);
            buffList.addEffect(wakfuEffect);
        }
        buffList.sort(new Comparator<WakfuEffect>() {
            @Override
            public int compare(final WakfuEffect o1, final WakfuEffect o2) {
                if (o1 == o2) {
                    return 0;
                }
                return o1.getEffectId() - o2.getEffectId();
            }
        });
        this.m_buffListsByTeamId.put(teamId, buffList);
    }
    
    @Override
    public int getTimePointGap() {
        return this.m_timePointGap;
    }
    
    @Override
    public void setTimePointGap(final int timePointGap) {
        this.m_timePointGap = timePointGap;
    }
    
    @Override
    public int getTimeScore(final long fighterId) {
        return this.m_timeScoreGauges.get(fighterId);
    }
    
    @Override
    public void updateTimeScore(final long fighterId, final int gain) {
        if (gain == -1) {
            return;
        }
        final int previousScore = this.m_timeScoreGauges.get(fighterId);
        this.setTimeScore(fighterId, previousScore + gain);
    }
    
    public void setTimeScore(final long fighterId, final int score) {
        final int positiveGain = Math.max(0, score);
        final int timePointBefore = this.getLastReachedTimePointIndex(fighterId);
        if (!this.m_timeScoreGauges.containsKey(fighterId)) {
            this.m_fighterNotAllowedToChoose.add(fighterId);
        }
        this.m_timeScoreGauges.put(fighterId, positiveGain);
        final int timePointAfter = this.getLastReachedTimePointIndex(fighterId);
        if (timePointAfter > timePointBefore) {
            this.m_fighterNotAllowedToChoose.remove(fighterId);
        }
    }
    
    public int getLastReachedTimePointIndex(final long fighterId) {
        final int gap = Math.max(1, this.m_timePointGap);
        return this.getTimeScore(fighterId) / gap - 1;
    }
    
    @Override
    public boolean selectEffect(final long fighterId, final int effectId, final int teamId) {
        return this.selectEffect(fighterId, this.getEffectById(effectId), teamId);
    }
    
    private boolean selectEffect(final long fighterId, final WakfuEffect effect, final int teamId) {
        final List<WakfuEffect> buffList = this.getOrCreateAvailableEffects(teamId);
        if (buffList == null) {
            return false;
        }
        if (!buffList.contains(effect)) {
            return false;
        }
        final WakfuEffect effectAlreadySelected = this.m_effectToApplyByFighterId.get(fighterId);
        if (effectAlreadySelected != null) {
            buffList.add(effectAlreadySelected);
        }
        this.m_effectToApplyByFighterId.put(fighterId, effect);
        this.cancelFighterChoosePermission(fighterId);
        buffList.remove(effect);
        return true;
    }
    
    @Nullable
    @Override
    public WakfuEffect getEffectById(final int effectId) {
        final int[] keys = this.m_buffListsByTeamId.keys();
        for (int i = 0; i < keys.length; ++i) {
            final int key = keys[i];
            final TimelineBuffList<WakfuEffect> buffList = this.m_buffListsByTeamId.get(key);
            final WakfuEffect effect = buffList.getEffectById(effectId);
            if (effect != null) {
                return effect;
            }
        }
        return null;
    }
    
    protected List<WakfuEffect> getOrCreateAvailableEffects(final int teamId) {
        final List<WakfuEffect> effectList = this.m_availableEffectsPerTeam.get(teamId);
        if (effectList != null && !effectList.isEmpty()) {
            return effectList;
        }
        final List<WakfuEffect> wakfuEffects = new ArrayList<WakfuEffect>();
        final TimelineBuffList<WakfuEffect> buffList = this.m_buffListsByTeamId.get(teamId);
        if (buffList == null) {
            return Collections.emptyList();
        }
        wakfuEffects.addAll(buffList.getEffects());
        this.m_availableEffectsPerTeam.put(teamId, wakfuEffects);
        return wakfuEffects;
    }
    
    @Override
    public List<WakfuEffect> getEffectsAvailableForSelection(final long fighterId, final int teamId) {
        return Collections.unmodifiableList((List<? extends WakfuEffect>)this.getOrCreateAvailableEffects(teamId));
    }
    
    @Override
    public void executeTimePointEffects(final BasicCharacterInfo fighter, final EffectContext context) {
        if (fighter == null || context == null) {
            return;
        }
        final WakfuEffect timePointsEffectToApply = this.m_effectToApplyByFighterId.get(fighter.getId());
        if (timePointsEffectToApply != null) {
            timePointsEffectToApply.execute(TimelineBuffListManager.getContainerForEffect(timePointsEffectToApply.getEffectId(), timePointsEffectToApply), fighter, context, RunningEffectConstants.getInstance(), fighter.getWorldCellX(), fighter.getWorldCellY(), fighter.getWorldCellAltitude(), fighter, null, false);
        }
        this.m_effectToApplyByFighterId.remove(fighter.getId());
        this.resetRemainingSeconds(fighter.getId());
    }
    
    @Override
    public void removeEffectToBeApplied(final long fighterId) {
        this.m_effectToApplyByFighterId.remove(fighterId);
    }
    
    @Override
    public void resetRemainingSeconds(final long fighterId) {
        final ObjectPair<Integer, Integer> pair = this.m_fighterRemainingSeconds.get(fighterId);
        if (pair == null) {
            return;
        }
        pair.setFirst(0);
        pair.setSecond(0);
    }
    
    @Override
    public List<WakfuEffect> getTeamEffects(final int teamId) {
        final TimelineBuffList<WakfuEffect> buffList = this.m_buffListsByTeamId.get(teamId);
        if (buffList == null) {
            return Collections.emptyList();
        }
        return buffList.getEffects();
    }
    
    @Override
    public TIntObjectHashMap<TimelineBuffList<WakfuEffect>> getBuffListsByTeamId() {
        return this.m_buffListsByTeamId;
    }
    
    @Override
    public WakfuEffect getEffectToBeAppliedForPlayer(final long fighterId) {
        return this.m_effectToApplyByFighterId.get(fighterId);
    }
    
    @Override
    public boolean hasEffectToBeExecuted(final long fighterId) {
        return this.m_effectToApplyByFighterId.containsKey(fighterId);
    }
    
    @Override
    public int addTurnRemainingSeconds(final long fighterId, final int remainingSeconds) {
        ObjectPair<Integer, Integer> pair = this.m_fighterRemainingSeconds.get(fighterId);
        if (pair == null) {
            pair = new ObjectPair<Integer, Integer>(0, 0);
            this.m_fighterRemainingSeconds.put(fighterId, pair);
        }
        pair.setFirst(pair.getFirst() + remainingSeconds);
        pair.setSecond(pair.getSecond() + 1);
        return remainingSeconds;
    }
    
    @Override
    public int getAverageRemainingSecondsByTurn(final long fighterId) {
        final ObjectPair<Integer, Integer> remainingSecondsAndTotalTurns = this.m_fighterRemainingSeconds.get(fighterId);
        if (remainingSecondsAndTotalTurns == null) {
            return -1;
        }
        if (remainingSecondsAndTotalTurns.getSecond() == 0) {
            return 0;
        }
        return remainingSecondsAndTotalTurns.getFirst() / remainingSecondsAndTotalTurns.getSecond();
    }
    
    @Override
    public boolean isFighterAllowedToSelectBonus(final long fighterId) {
        return this.m_timeScoreGauges.containsKey(fighterId) && !this.m_fighterNotAllowedToChoose.contains(fighterId);
    }
    
    @Override
    public void cancelFighterChoosePermission(final long fighterId) {
        this.m_fighterNotAllowedToChoose.add(fighterId);
    }
    
    @Override
    public int size() {
        return this.m_timeScoreGauges.size();
    }
    
    public TLongIntHashMap getTimeScoreGauges() {
        return this.m_timeScoreGauges;
    }
    
    public TIntObjectHashMap<List<WakfuEffect>> getAvailableEffectsPerTeam() {
        return this.m_availableEffectsPerTeam;
    }
    
    @Override
    public byte[] serialize(final int currentTurn) {
        return TimeScoreGaugeImplSerializer.serialize(this, currentTurn);
    }
    
    @Override
    public void unserialize(final byte[] data) {
        TimeScoreGaugeImplSerializer.unserialize(data, this);
    }
    
    static {
        m_logger = Logger.getLogger((Class)TimeScoreGaugesImpl.class);
    }
}
