package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.fighter.specialEvent.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class StateRunningEffect extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private long m_lastTransmigrationDate;
    private long m_startDate;
    private boolean m_mustBeExecuted;
    private long m_originalCasterid;
    private boolean m_forFecaArmor;
    private final BitSet m_endTriggers;
    private State m_state;
    private boolean m_isTransmigrating;
    private BinarSerialPart TRANSMIGRATIONNAL_DATAS;
    
    public StateRunningEffect() {
        super();
        this.m_mustBeExecuted = false;
        this.m_endTriggers = new BitSet();
        this.TRANSMIGRATIONNAL_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(StateRunningEffect.this.m_lastTransmigrationDate);
                buffer.putLong(StateRunningEffect.this.m_remainingTimeInMs);
                buffer.putLong(StateRunningEffect.this.getUniqueId());
                buffer.putInt(StateRunningEffect.this.m_value);
                buffer.put((byte)(StateRunningEffect.this.m_forFecaArmor ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                StateRunningEffect.this.m_lastTransmigrationDate = buffer.getLong();
                StateRunningEffect.this.m_remainingTimeInMs = buffer.getLong();
                StateRunningEffect.this.setUid(buffer.getLong());
                StateRunningEffect.this.m_value = buffer.getInt();
                StateRunningEffect.this.m_forFecaArmor = (buffer.get() == 1);
                StateRunningEffect.this.checkForStateCreation();
            }
            
            @Override
            public int expectedSize() {
                return 29;
            }
        };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return StateRunningEffect.PARAMETERS_LIST_SET;
    }
    
    public static StateRunningEffect checkOut(final EffectContext<WakfuEffect> context, final EffectUser caster, final WakfuEffectContainer container, final int stateId) {
        final StateRunningEffect re = new StateRunningEffect();
        re.m_state = null;
        re.m_id = RunningEffectConstants.RUNNING_STATE.getId();
        re.m_status = RunningEffectConstants.RUNNING_STATE.getObject().getRunningEffectStatus();
        re.setTriggersToExecute();
        re.m_caster = caster;
        if (caster != null) {
            re.m_originalCasterid = caster.getId();
        }
        else {
            re.m_originalCasterid = 0L;
        }
        re.m_context = (EffectContext<FX>)context;
        re.m_endTriggers.clear();
        re.m_value = stateId;
        re.m_mustBeExecuted = true;
        final boolean stateExists = re.checkForStateCreation();
        if (!stateExists) {
            StateRunningEffect.m_logger.error((Object)("Unable to checkout a state with id " + stateId + " : unknown ID"));
            return null;
        }
        re.m_remainingTimeInMs = re.m_state.getMsDuration();
        re.m_maxExecutionCount = -1;
        re.m_isTransmigrating = false;
        re.m_lastTransmigrationDate = -1L;
        re.m_startDate = 0L;
        return re;
    }
    
    public void setInamovable() {
        this.checkForStateCreation();
        if (this.m_state != null) {
            this.m_state.setInamovable(true);
        }
    }
    
    @Override
    public StateRunningEffect newInstance() {
        final StateRunningEffect re = new StateRunningEffect();
        re.m_endTriggers.clear();
        re.m_endTriggers.or(this.m_endTriggers);
        re.m_state = this.m_state;
        re.m_mustBeExecuted = true;
        re.m_remainingTimeInMs = this.m_remainingTimeInMs;
        re.m_originalCasterid = this.m_originalCasterid;
        re.m_isTransmigrating = this.m_isTransmigrating;
        if (this.m_isTransmigrating) {
            re.m_uid = this.m_uid;
        }
        re.m_forFecaArmor = this.m_forFecaArmor;
        re.m_lastTransmigrationDate = this.m_lastTransmigrationDate;
        re.m_startDate = this.m_startDate;
        return re;
    }
    
    @Override
    protected void cloneParameters(final RunningEffect<WakfuEffect, WakfuEffectContainer> re) {
        super.cloneParameters(re);
        if (this.m_isTransmigrating) {
            this.m_uid = re.getUniqueId();
        }
        this.m_startDate = ((StateRunningEffect)re).m_startDate;
    }
    
    public StateRunningEffect transmigrate(final EffectContext<WakfuEffect> context) {
        this.setTransmigrating(true);
        final StateRunningEffect effect = (StateRunningEffect)this.newParameterizedInstance();
        effect.m_uid = this.m_uid;
        effect.m_context = (EffectContext<FX>)context;
        effect.checkForStateCreation();
        if (this.m_startDate > 0L) {
            return effect;
        }
        if (context.getContextType() == 0 || context.getContextType() == 3) {
            if (effect.m_remainingTimeInMs == -1L && this.m_context.getContextType() == 1 && this.m_context.getTimeline() != null && this.getEndTime() != null) {
                final TurnBasedTimeline timeline = this.m_context.getTimeline();
                effect.m_remainingTimeInMs = timeline.howLongInTurnsUntil(this.getEndTime()) * 30000;
                effect.m_lastTransmigrationDate = -1L;
            }
            else if (effect.m_lastTransmigrationDate <= 0L) {
                effect.m_remainingTimeInMs = this.m_state.getTableTurnDuration() * 30000;
            }
            else if (effect.m_lastTransmigrationDate > 0L) {
                final int elapsedTime = (int)(System.currentTimeMillis() - effect.m_lastTransmigrationDate);
                final long remainingTime = effect.m_remainingTimeInMs - elapsedTime;
                effect.m_remainingTimeInMs = Math.max(1L, remainingTime);
                effect.m_lastTransmigrationDate = -1L;
            }
        }
        else {
            effect.m_lastTransmigrationDate = System.currentTimeMillis();
            final RunningEffectManager manager = this.getManagerWhereIamStored();
            if (manager != null && manager instanceof TimedRunningEffectManager) {
                final long realRemainingTime = ((TimedRunningEffectManager)manager).getRemainingTimeForEffect(this);
                effect.m_remainingTimeInMs = (int)Math.max(1L, realRemainingTime);
            }
            else {
                effect.m_remainingTimeInMs = this.m_remainingTimeInMs;
            }
        }
        return effect;
    }
    
    public State getState() {
        return this.m_state;
    }
    
    public boolean isTransmigrating() {
        return this.m_isTransmigrating;
    }
    
    public void setTransmigrating(final boolean transmigrating) {
        this.m_isTransmigrating = transmigrating;
    }
    
    public void addEndTriggers(final BitSet endTriggers) {
        if (endTriggers != null) {
            this.m_endTriggers.or(endTriggers);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_value > 0 && this.m_mustBeExecuted) {
            if (this.m_target != null && this.m_target.getRunningEffectManager() != null) {
                this.checkForStateCreation();
                if (this.m_state != null) {
                    if (this.isValueComputationEnabled()) {
                        if (this.isTransmigrating()) {
                            this.setNotified(true);
                        }
                        if (!this.isNotified()) {
                            this.notifyExecution(linkedRE, trigger);
                        }
                        if (this.m_state.isDurationInRealTime() && this.m_startDate <= 0L) {
                            this.m_startDate = WakfuGameCalendar.getInstance().getNewDate().toLong();
                        }
                        for (final WakfuEffect effect : this.m_state) {
                            final WakfuRunningEffect re = (WakfuRunningEffect)RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId()).newParameterizedInstance();
                            if (this.m_target == null) {
                                StateRunningEffect.m_logger.error((Object)("Impossible d'executer l'effet " + effect.getEffectId() + " on a plus de cible"));
                                return;
                            }
                            ((RunningEffect<WakfuEffect, State>)re).setParameters(effect, this.m_state, this.m_context, this.m_caster, null, this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), this.getParams());
                            if (this.isNotifyForced()) {
                                re.setNotifyForced(true);
                            }
                            if (this.m_context.getContextType() == 1 && this.hasExecutionDelay()) {
                                re.pushRunningEffectDelayedTimeEventInTimeline();
                                break;
                            }
                            if (this.useTarget()) {
                                re.m_endTime = this.m_endTime;
                                re.applyOnTargets(this.m_target);
                            }
                            re.release();
                        }
                    }
                    if (this.m_target != null && this.m_target instanceof BasicFighter) {
                        ((BasicFighter)this.m_target).onSpecialFighterEvent(new EffectAppliedEvent(this));
                    }
                }
                else {
                    StateRunningEffect.m_logger.error((Object)"State inconnu pour le client, mais vraisemblablement pas pour le serveur");
                }
            }
            this.m_mustBeExecuted = false;
        }
        else {
            this.setNotified(true);
        }
        this.m_isTransmigrating = false;
    }
    
    @Override
    public boolean mustBeTriggered() {
        return false;
    }
    
    @Override
    public boolean hasDuration() {
        this.checkForStateCreation();
        return this.isInfinite() || (this.m_state != null && (this.m_state.getTableTurnDuration() > 0 || this.m_state.getMsDuration() > 0));
    }
    
    @Override
    public boolean isInfinite() {
        this.checkForStateCreation();
        if (this.m_state == null) {
            return false;
        }
        if (this.m_state.isTransmigrable()) {
            return this.m_state.getTableTurnDuration() < 0 && this.m_state.getMsDuration() < 0;
        }
        return this.m_state.getTableTurnDuration() < 0 || this.m_state.getMsDuration() < 0;
    }
    
    @Override
    public long getRemainingTimeInMs() {
        if (this.m_state != null && this.m_state.isDurationInRealTime()) {
            return this.getRemainingRealTime();
        }
        if (this.m_remainingTimeInMs >= 0L) {
            return this.m_remainingTimeInMs;
        }
        this.checkForStateCreation();
        if (this.m_state != null) {
            return this.m_state.getMsDuration();
        }
        return 0L;
    }
    
    private long getRemainingRealTime() {
        if (this.m_startDate > 0L) {
            final int msDuration = this.m_state.getMsDuration();
            final GameDate now = WakfuGameCalendar.getInstance().getNewDate();
            final long elapsedTime = now.toLong() - this.m_startDate;
            return Math.max(0L, msDuration - elapsedTime);
        }
        return this.m_state.getMsDuration();
    }
    
    @Override
    public boolean hasDurationInMs() {
        return this.m_remainingTimeInMs > 0L && this.m_lastTransmigrationDate < 0L;
    }
    
    private boolean checkForStateCreation() {
        if (this.m_state != null) {
            return true;
        }
        final State state = StateManager.getInstance().getBasicStateFromUniqueId(this.m_value);
        if (state == null) {
            return false;
        }
        (this.m_state = state.instanceAnother(State.getLevelFromUniqueId(this.m_value))).setFecaArmor(this.m_forFecaArmor);
        this.m_endTriggers.or(this.m_state.getEndTriggers());
        return true;
    }
    
    @Override
    protected Long getIdOfFighterToToAttachTo() {
        if (this.m_state != null) {
            if (this.m_state.isDurationIsInCasterTurn() && this.m_caster != null) {
                return this.m_caster.getId();
            }
            if (!this.m_state.isDurationIsInCasterTurn() && this.m_target != null) {
                return this.m_target.getId();
            }
        }
        return super.getIdOfFighterToToAttachTo();
    }
    
    @Override
    public void pushRunningEffectDurationTimeEventInTimeline() {
        this.checkForStateCreation();
        if (this.m_state == null) {
            return;
        }
        if (this.m_context == null || this.m_context.getTimeline() == null) {
            return;
        }
        final RelativeFightTimeInterval timeInterval = this.m_state.getFightDuration();
        if (!this.m_state.isTransmigrable()) {
            this.pushDeactivationTimeEventAtTime(timeInterval);
        }
    }
    
    @Override
    public void unapplyOverride() {
        super.unapplyOverride();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    public BitSet getListeningTriggerForUnapplication() {
        return this.m_endTriggers;
    }
    
    @Override
    public boolean unapplicationMustBeNotified() {
        return super.unapplicationMustBeNotified();
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    public boolean staysOffFight() {
        this.checkForStateCreation();
        return this.m_state.isTransmigrable();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.TRANSMIGRATIONNAL_DATAS;
    }
    
    public long getOriginalCasterid() {
        return this.m_originalCasterid;
    }
    
    public boolean isTransmigrable() {
        return this.m_state != null && this.m_state.isTransmigrable();
    }
    
    public void setForFecaArmor(final boolean forFecaArmor) {
        this.m_forFecaArmor = forFecaArmor;
    }
    
    public long getStartDate() {
        return this.m_startDate;
    }
    
    public void setStartDate(final long startDate) {
        this.m_startDate = startDate;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_forFecaArmor = false;
        this.m_lastTransmigrationDate = -1L;
        this.m_startDate = 0L;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("param\u00e8tre d'etat", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
