package com.ankamagames.wakfu.common.game.spell;

import gnu.trove.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;

public class State implements WakfuEffectContainer
{
    private static final int DEAD_OCCUPATION_STATE_ID = 981;
    public static short DEFAULT_AGGRO_WEIGHT;
    public static short DEFAULT_ALLY_EFFICACITY;
    public static short DEFAULT_FOE_EFFICACITY;
    protected short m_maxlevel;
    protected short m_stateBaseId;
    protected short m_durationTurnTable;
    protected float m_durationTurnTableIncrement;
    protected boolean m_isDurationInFullTurns;
    protected boolean m_endsAtEndOfTurn;
    protected int m_durationMs;
    protected int m_durationMsIncrement;
    protected boolean m_isTransmigrable;
    protected boolean m_inTurnInFight;
    protected boolean m_replacable;
    protected boolean m_isCumulable;
    protected boolean m_durationIsInCasterTurn;
    protected boolean m_durationInRealTime;
    protected TIntHashSet m_stateImmunities;
    protected SimpleCriterion m_applyCriterions;
    private final BitSet m_endTriggers;
    private boolean m_inamovable;
    protected boolean m_decursable;
    protected byte m_stateType;
    protected StatePowerType m_statePowerType;
    protected List<HMIAction> m_HMIActions;
    protected boolean m_stateShouldBeSaved;
    protected short m_level;
    protected int m_uniqueId;
    protected GrowingArray<WakfuEffect> m_effects;
    private boolean m_fecaArmor;
    protected boolean m_reapplyEvenAtMaxLevel;
    
    public State() {
        super();
        this.m_durationIsInCasterTurn = false;
        this.m_durationInRealTime = false;
        this.m_endTriggers = new BitSet();
        this.m_HMIActions = null;
        this.m_stateShouldBeSaved = false;
        this.m_effects = new GrowingArray<WakfuEffect>();
        this.m_fecaArmor = false;
        this.m_reapplyEvenAtMaxLevel = false;
    }
    
    public static int getUniqueIdFromBasicInformation(final short baseId, final short level) {
        return (baseId << 16) + (level & 0xFFFF);
    }
    
    public static short getBasicIdFromUniqueId(final int uniqueId) {
        return (short)(uniqueId >> 16);
    }
    
    public SimpleCriterion getApplyCriterions() {
        return this.m_applyCriterions;
    }
    
    public static short getLevelFromUniqueId(final int uniqueId) {
        return (short)(uniqueId & 0xFFFF);
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    public State instanceAnother(final short level) {
        final State state = new State();
        this.copyParameters(level, state);
        return state;
    }
    
    protected void copyParameters(final short level, final State state) {
        state.m_stateBaseId = this.m_stateBaseId;
        state.m_maxlevel = this.m_maxlevel;
        state.m_durationTurnTable = this.m_durationTurnTable;
        state.m_durationTurnTableIncrement = this.m_durationTurnTableIncrement;
        state.m_durationMs = this.m_durationMs;
        state.m_isDurationInFullTurns = this.m_isDurationInFullTurns;
        state.m_endsAtEndOfTurn = this.m_endsAtEndOfTurn;
        state.m_durationMsIncrement = this.m_durationMsIncrement;
        state.m_effects = this.m_effects;
        state.addEndTriggers(this.getEndTriggers());
        state.m_level = (short)Math.min(level, this.m_maxlevel);
        state.m_uniqueId = getUniqueIdFromBasicInformation(this.m_stateBaseId, level);
        state.m_isTransmigrable = this.m_isTransmigrable;
        state.m_inTurnInFight = this.m_inTurnInFight;
        state.m_replacable = this.m_replacable;
        state.m_stateImmunities = this.m_stateImmunities;
        state.m_isCumulable = this.m_isCumulable;
        state.m_durationIsInCasterTurn = this.m_durationIsInCasterTurn;
        state.m_durationInRealTime = this.m_durationInRealTime;
        state.m_stateShouldBeSaved = this.m_stateShouldBeSaved;
        state.m_decursable = this.m_decursable;
        state.m_fecaArmor = this.m_fecaArmor;
        state.m_stateType = this.m_stateType;
        state.m_reapplyEvenAtMaxLevel = this.m_reapplyEvenAtMaxLevel;
    }
    
    public int getTableTurnDuration() {
        return this.getTableTurnDuration(this.m_level);
    }
    
    public RelativeFightTimeInterval getFightDuration() {
        return this.getFightDuration(this.m_level);
    }
    
    public RelativeFightTimeInterval getFightDuration(final short level) {
        final short nTurns = (short)this.getTableTurnDuration(level);
        return RelativeFightTimeInterval.turnsFromNow(nTurns).inFullTurns(this.m_isDurationInFullTurns).atEndOfTurn(this.m_endsAtEndOfTurn).withPriority((short)(this.m_durationTurnTable + 1));
    }
    
    public int getTableTurnDuration(final int level) {
        return this.m_durationTurnTable + (int)Math.floor(((level < this.m_maxlevel) ? level : this.m_maxlevel) * this.m_durationTurnTableIncrement);
    }
    
    public int getMsDuration() {
        return this.m_durationMs + ((this.m_level < this.m_maxlevel) ? this.m_level : this.m_maxlevel) * this.m_durationMsIncrement;
    }
    
    public short getStateBaseId() {
        return this.m_stateBaseId;
    }
    
    public int getUniqueId() {
        return this.m_uniqueId;
    }
    
    public void addEffect(final WakfuEffect effect) {
        this.m_effects.add(effect);
    }
    
    @Override
    public Iterator<WakfuEffect> iterator() {
        return this.getEffectsForLevelAsList(this.getLevel()).iterator();
    }
    
    public ArrayList<WakfuEffect> getEffectsForLevelAsList(final short level) {
        final int size = this.m_effects.size();
        final ArrayList<WakfuEffect> result = new ArrayList<WakfuEffect>(size);
        for (int i = 0; i < size; ++i) {
            final WakfuEffect effect = this.m_effects.get(i);
            if (level >= effect.getContainerMinLevel() && level <= effect.getContainerMaxLevel()) {
                result.add(effect);
            }
        }
        return result;
    }
    
    public BitSet getEndTriggers() {
        return this.m_endTriggers;
    }
    
    public void addEndTriggers(final BitSet bs) {
        this.m_endTriggers.or(bs);
    }
    
    @Override
    public int getContainerType() {
        return 1;
    }
    
    @Override
    public long getEffectContainerId() {
        return this.m_uniqueId;
    }
    
    public boolean isTransmigrable() {
        return this.m_isTransmigrable && !this.m_inamovable;
    }
    
    public Iterator<HMIAction> getHMIActions() {
        return (this.m_HMIActions != null) ? this.m_HMIActions.iterator() : new EmptyIterator<HMIAction>();
    }
    
    public boolean isReplacable() {
        return this.m_replacable;
    }
    
    @Override
    public short getAggroWeight() {
        return State.DEFAULT_AGGRO_WEIGHT;
    }
    
    @Override
    public short getAllyEfficacity() {
        return State.DEFAULT_ALLY_EFFICACITY;
    }
    
    @Override
    public short getFoeEfficacity() {
        return State.DEFAULT_FOE_EFFICACITY;
    }
    
    public boolean isInamovable() {
        return this.m_inamovable;
    }
    
    public void setInamovable(final boolean inamovable) {
        this.m_inamovable = inamovable;
    }
    
    public boolean isDecursable() {
        return this.m_decursable;
    }
    
    public TIntHashSet getStateImmunities() {
        return this.m_stateImmunities;
    }
    
    public boolean hasInfiniteDurations() {
        return this.getMsDuration() < 0 && this.getTableTurnDuration() < 0;
    }
    
    public boolean isCumulable() {
        return this.m_isCumulable;
    }
    
    public boolean isDurationIsInCasterTurn() {
        return this.m_durationIsInCasterTurn;
    }
    
    public boolean isDurationInRealTime() {
        return this.m_durationInRealTime;
    }
    
    public void setDurationInRealTime(final boolean durationInRealTime) {
        this.m_durationInRealTime = durationInRealTime;
    }
    
    public short getMaxlevel() {
        return this.m_maxlevel;
    }
    
    public boolean isStateShouldBeSaved() {
        return this.m_stateShouldBeSaved;
    }
    
    public String getName() {
        return "state-" + this.getStateBaseId();
    }
    
    public void setFecaArmor(final boolean fecaArmor) {
        this.m_fecaArmor = fecaArmor;
    }
    
    public boolean isFecaArmor() {
        return this.m_fecaArmor;
    }
    
    public boolean isStateForDeath() {
        return this.m_stateBaseId == 981;
    }
    
    public StatePowerType getStatePowerType() {
        return this.m_statePowerType;
    }
    
    public StateType getStateType() {
        return StateType.getFromValue(this.m_stateType);
    }
    
    public boolean reapplyEvenAtMaxLevel() {
        return this.m_reapplyEvenAtMaxLevel;
    }
    
    static {
        State.DEFAULT_AGGRO_WEIGHT = 0;
        State.DEFAULT_ALLY_EFFICACITY = 0;
        State.DEFAULT_FOE_EFFICACITY = 0;
    }
}
