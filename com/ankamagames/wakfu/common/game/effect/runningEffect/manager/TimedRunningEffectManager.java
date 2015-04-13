package com.ankamagames.wakfu.common.game.effect.runningEffect.manager;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.wakfu.common.datas.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.runningEffectIterator.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.runningEffectWakfuIterator.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import org.jetbrains.annotations.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;

public class TimedRunningEffectManager extends RunningEffectManager implements MessageHandler
{
    private static boolean LOG_REM;
    private static final TLongProcedure REMOVE_CLOCK_PROCEDURE;
    private long m_validableId;
    private final EffectUser m_owner;
    private final TLongLongHashMap m_clocks;
    private final TLongLongHashMap m_endTimes;
    final RunningEffectManagerSerializer m_serializer;
    private final List<StateRunningEffect> m_runningEffectStateToRemove;
    
    public TimedRunningEffectManager(final EffectUser owner) {
        super();
        this.m_validableId = 10L;
        this.m_clocks = new TLongLongHashMap();
        this.m_endTimes = new TLongLongHashMap();
        this.m_serializer = new RunningEffectManagerSerializer(this);
        this.m_runningEffectStateToRemove = new ArrayList<StateRunningEffect>();
        this.m_owner = owner;
    }
    
    @Override
    protected void createTriggerHandler() {
        this.m_triggerHandler = new WakfuTriggerHandler(this);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (!(message instanceof ClockMessage)) {
            return false;
        }
        final long effectId = this.m_clocks.get(((ClockMessage)message).getClockId());
        if (effectId != 0L) {
            this.removeEffect(effectId, true);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.m_clocks.clear();
        this.m_endTimes.clear();
        super.clear();
    }
    
    @Override
    public void destroyAll() {
        this.m_clocks.forEachKey(TimedRunningEffectManager.REMOVE_CLOCK_PROCEDURE);
        this.m_clocks.clear();
        this.m_endTimes.clear();
        super.destroyAll();
    }
    
    @Override
    public long getId() {
        return this.m_validableId;
    }
    
    @Override
    public void setId(final long id) {
        this.m_validableId = id;
    }
    
    @Override
    public EffectUser getOwner() {
        return this.m_owner;
    }
    
    public long getRemainingTimeForEffect(final RunningEffect reffect) {
        final RunningEffect effect = reffect;
        if (this.hasEndTime(effect.getUniqueId())) {
            final long uniqueId = effect.getUniqueId();
            return this.getEndTime(uniqueId) - System.currentTimeMillis();
        }
        if (effect instanceof StateRunningEffect) {
            final State state = ((StateRunningEffect)effect).getState();
            if (!state.hasInfiniteDurations()) {
                TimedRunningEffectManager.m_logger.error((Object)("Le temps de fin pour un etat non infini n'est pas contenu dans la map des effets en cours : effect.uid=" + effect.getUniqueId() + ", effect.id=" + effect.getId() + ", stateId=" + state.getStateBaseId()));
            }
        }
        else {
            TimedRunningEffectManager.m_logger.error((Object)("Le temps de fin n'est pas contenu dans la map des effets en cours : effect.uid=" + effect.getUniqueId() + ", effect.id=" + effect.getId()));
        }
        return 0L;
    }
    
    @Override
    public void storeEffect(final RunningEffect reffect) {
        if (reffect == null) {
            return;
        }
        final WakfuRunningEffect effect = (WakfuRunningEffect)reffect;
        if (effect.getContext() == null || !effect.hasDuration()) {
            return;
        }
        if (!effect.isInfinite() && effect.hasDurationInMs()) {
            final long delay = effect.getRemainingTimeInMs();
            final long clockId = MessageScheduler.getInstance().addClock(this, delay, 0, 1);
            super.storeEffect(effect);
            this.m_clocks.put(clockId, effect.getUniqueId());
            this.m_endTimes.put(effect.getUniqueId(), System.currentTimeMillis() + delay);
        }
        else {
            super.storeEffect(effect);
        }
    }
    
    public void removeManager() {
        this.destroyAll();
    }
    
    public byte[] serializeInFight(final boolean forIa) {
        return this.m_serializer.serializeInFight(forIa);
    }
    
    public byte[] serializeFull() {
        return this.m_serializer.serializeFull();
    }
    
    long getEndTime(final long uniqueId) {
        return this.m_endTimes.get(uniqueId);
    }
    
    boolean hasEndTime(final long uniqueId) {
        return this.m_endTimes.containsKey(uniqueId);
    }
    
    boolean ownerIsCompanion() {
        return this.m_owner instanceof BasicCharacterInfo && ((BasicCharacterInfo)this.m_owner).getType() == 5;
    }
    
    public void unserializeInFight(final byte[] datas, final EffectContext context) {
        this.clear();
        final ByteBuffer buffer = ByteBuffer.wrap(datas);
        for (short effectCount = buffer.getShort(), i = 0; i < effectCount; ++i) {
            final StaticRunningEffect<WakfuEffect, WakfuEffectContainer> staticEffect = ((Constants<StaticRunningEffect<WakfuEffect, WakfuEffectContainer>>)RunningEffectConstants.getInstance()).getObjectFromId(buffer.getInt());
            if (staticEffect == null) {
                TimedRunningEffectManager.m_logger.error((Object)"Impossible d'instancier un runningEffect ");
            }
            final byte[] serializedRunningEffect = new byte[buffer.getShort()];
            buffer.get(serializedRunningEffect);
            if (staticEffect != null) {
                if (context == null) {
                    TimedRunningEffectManager.m_logger.error((Object)"contexte null au moment de d\u00e9s\u00e9rialiser un effet");
                }
                final WakfuRunningEffect runningEffect = (WakfuRunningEffect)staticEffect.newInstance(context, null);
                try {
                    runningEffect.fromBuild(serializedRunningEffect);
                }
                catch (Exception e) {
                    TimedRunningEffectManager.m_logger.error((Object)("Exception levee a la deserialisation d'un effet " + staticEffect.getId()), (Throwable)e);
                    this.logManagerEffects();
                    break;
                }
                final int remaining = buffer.getInt();
                if (remaining != 0) {
                    runningEffect.setRemainingTimeInMs(remaining);
                }
                this.storeEffect(runningEffect);
            }
        }
    }
    
    private void logManagerEffects() {
        this.m_effects.forEachValue((TObjectProcedure<RunningEffect>)new TObjectProcedure<RunningEffect>() {
            @Override
            public boolean execute(final RunningEffect effect) {
                final Effect genericEffect = effect.getGenericEffect();
                if (genericEffect == null) {
                    TimedRunningEffectManager.m_logger.info((Object)effect.getId());
                }
                else {
                    TimedRunningEffectManager.m_logger.info((Object)("Action Id " + effect.getId() + ", EffectId " + genericEffect.getEffectId()));
                }
                return true;
            }
        });
    }
    
    public boolean toRawStateRunningEffects(final RawStateRunningEffects rawEffects) {
        return this.toRawStateRunningEffects(rawEffects, true, false);
    }
    
    public boolean toRawStateRunningEffects(final RawStateRunningEffects rawEffects, final boolean onlyStateToSave, final boolean withoutStateToSave) {
        rawEffects.effects.clear();
        final TLongObjectIterator<RunningEffect> it = (TLongObjectIterator<RunningEffect>)this.m_effects.iterator();
        while (it.hasNext()) {
            it.advance();
            final RunningEffect effect = it.value();
            if (!(effect instanceof StateRunningEffect)) {
                continue;
            }
            final StateRunningEffect stateEffect = (StateRunningEffect)effect;
            final State state = stateEffect.getState();
            if (onlyStateToSave && !state.isStateShouldBeSaved()) {
                continue;
            }
            if (withoutStateToSave && state.isStateShouldBeSaved()) {
                continue;
            }
            boolean mustBeSaved = true;
            int duration = 0;
            if (this.hasEndTime(effect.getUniqueId())) {
                final long uniqueId = effect.getUniqueId();
                duration = (int)(this.getEndTime(uniqueId) - System.currentTimeMillis());
                if (duration < 0 && state.getMsDuration() != -1 && state.getTableTurnDuration() != -1) {
                    mustBeSaved = false;
                }
            }
            else if (state.getMsDuration() != -1 && state.getTableTurnDuration() != -1) {
                mustBeSaved = false;
            }
            if (!mustBeSaved) {
                continue;
            }
            final RawStateRunningEffects.StateRunningEffect rawStateEffect = new RawStateRunningEffects.StateRunningEffect();
            rawStateEffect.uid = effect.getUniqueId();
            rawStateEffect.stateBaseId = state.getStateBaseId();
            rawStateEffect.level = state.getLevel();
            rawStateEffect.remainingDurationInMs = duration;
            rawStateEffect.startDate = stateEffect.getStartDate();
            rawEffects.effects.add(rawStateEffect);
        }
        return true;
    }
    
    public boolean fromRawStateRunningEffects(final RawStateRunningEffects rawEffects, final EffectContext<WakfuEffect> context, final EffectUser user) {
        for (final RawStateRunningEffects.StateRunningEffect rawStateRunningEffect : rawEffects.effects) {
            final StaticRunningEffect staticEffect = ((Constants<StaticRunningEffect>)RunningEffectConstants.getInstance()).getObjectFromId(RunningEffectConstants.RUNNING_STATE.getId());
            if (staticEffect == null) {
                TimedRunningEffectManager.m_logger.error((Object)"On des\u00e9rialise un effet qui n'existe pas : RUNNING_STATE");
                return false;
            }
            final StateRunningEffect effect = StateRunningEffect.checkOut(context, user, null, State.getUniqueIdFromBasicInformation(rawStateRunningEffect.stateBaseId, rawStateRunningEffect.level));
            if (effect == null) {
                TimedRunningEffectManager.m_logger.error((Object)("Effect not unserialized because of an unknown state : " + rawStateRunningEffect.stateBaseId));
            }
            else {
                final State state = effect.getState();
                if (rawStateRunningEffect.remainingDurationInMs <= 0 && state.getMsDuration() != -1 && state.getTableTurnDuration() != -1) {
                    effect.release();
                }
                else {
                    effect.setUid(rawStateRunningEffect.uid);
                    effect.setRemainingTimeInMs(rawStateRunningEffect.remainingDurationInMs);
                    effect.setStartDate(rawStateRunningEffect.startDate);
                    effect.setTarget(user);
                    ((RunningEffect<DefaultWorldAndFightUsableEffect, EC>)effect).setGenericEffect(DefaultWorldAndFightUsableEffect.getInstance());
                    RunningEffect.resetLimitedApplyCount();
                    effect.askForExecution();
                    user.getRunningEffectManager().storeEffect(effect);
                    effect.onApplication();
                }
            }
        }
        return true;
    }
    
    public void initialize(final EffectUser target, final EffectContext context) {
        final List<WakfuRunningEffect> effectsToApply = new ArrayList<WakfuRunningEffect>();
        for (final WakfuRunningEffect effect : this) {
            if (!effect.isExecuted()) {
                effect.setContext(context);
                effectsToApply.add(effect);
            }
        }
        for (final WakfuRunningEffect effect2 : effectsToApply) {
            if (effect2.getRemainingTimeInMs() != 0L) {
                effect2.setTarget(target);
                this.storeEffect(effect2);
                RunningEffect.resetLimitedApplyCount();
                effect2.execute(null, false);
            }
        }
    }
    
    @Override
    public boolean removeEffect(final RunningEffect re) {
        return this.removeEffect(re.getUniqueId(), true);
    }
    
    public boolean removeEffect(final RunningEffect re, final boolean withoutException) {
        return this.removeEffect(re.getUniqueId(), withoutException);
    }
    
    public boolean removeEffect(final long uid, final boolean withoutException) {
        final RunningEffect re = this.m_effects.get(uid);
        if (re != null && re.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
            this.m_runningEffectStateToRemove.add((StateRunningEffect)re);
            this.cleanState(withoutException);
            return true;
        }
        return this.removeEffect(uid);
    }
    
    private void cleanState(final boolean cleanAll) {
        for (int i = 0, size = this.m_runningEffectStateToRemove.size(); i < size; ++i) {
            this.removeStateEffects(this.m_runningEffectStateToRemove.get(i));
        }
        if (cleanAll) {
            for (int i = 0, size = this.m_runningEffectStateToRemove.size(); i < size; ++i) {
                this.removeEffect(this.m_runningEffectStateToRemove.get(i).getUniqueId());
            }
        }
        this.m_runningEffectStateToRemove.clear();
    }
    
    private void removeStateEffects(final StateRunningEffect stateRunningEffect) {
        final State state = stateRunningEffect.getState();
        if (state == null) {
            TimedRunningEffectManager.m_logger.warn((Object)"On veut retirer les effets d'un \u00e9tat inconnu");
            return;
        }
        final LinkedToEffectContainerIterator it = this.getLinkedToContainerRunningEffects(state);
        final Collection<RunningEffect> effectsToRemove = new ArrayList<RunningEffect>();
        while (it.hasNext()) {
            effectsToRemove.add(it.next());
        }
        for (final RunningEffect effect : effectsToRemove) {
            try {
                final WakfuRunningEffect re = (WakfuRunningEffect)effect;
                if (re.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
                    continue;
                }
                if (stateRunningEffect.isForcedUnapplicationNotify()) {
                    re.setNotifyUnapplicationForced(true);
                }
                this.m_effects.remove(re.getUniqueId());
                this.onEffectRemoved(re);
            }
            catch (Exception e) {
                TimedRunningEffectManager.m_logger.error((Object)("Exception lors du retrait des effets lies a l'etat " + state.getStateBaseId()), (Throwable)e);
            }
        }
    }
    
    @Override
    public void removeLinkedToCaster(final EffectUser caster) {
        this.removeLinkedToCaster(caster, false);
    }
    
    public void removeLinkedToCaster(final EffectUser caster, final boolean fightOnly) {
        this.removeLinkedToCaster(caster, fightOnly, false);
    }
    
    public void removeLinkedToCaster(final EffectUser caster, final boolean fightOnly, final boolean goesOffPlay) {
        final LinkedToEffectUserIterator userRunningEffects = this.getLinkedToEffectUserRunningEffects(caster);
        while (userRunningEffects.hasNext()) {
            final WakfuRunningEffect re = (WakfuRunningEffect)userRunningEffects.next();
            if (re.getId() == RunningEffectConstants.FAKE_KO.getId()) {
                continue;
            }
            if (goesOffPlay && re.hasProperty(RunningEffectPropertyType.KEEP_WHEN_OFF_PLAY)) {
                continue;
            }
            if (goesOffPlay && re instanceof StateRunningEffect) {
                final State state = ((StateRunningEffect)re).getState();
                if (state != null && state.isDurationIsInCasterTurn()) {
                    re.setNotifyUnapplicationForced(true);
                    this.m_runningEffectStateToRemove.add((StateRunningEffect)re);
                    continue;
                }
            }
            if (re.getId() == RunningEffectConstants.RUNNING_STATE.getId() || !this.effectContainerIsNotState(re) || (fightOnly && (((RunningEffect<WakfuEffect, EC>)re).getGenericEffect() == null || ((RunningEffect<WakfuEffect, EC>)re).getGenericEffect().getEffectType() != 2))) {
                continue;
            }
            userRunningEffects.remove();
        }
        userRunningEffects.release();
        this.cleanState(true);
    }
    
    public void removeLinkedToCasterIncludingState(final EffectUser caster) {
        final LinkedToEffectUserIterator linkedToCaster = this.getLinkedToEffectUserRunningEffects(caster);
        this.removeFromIterator(linkedToCaster, true);
        linkedToCaster.release();
        this.cleanState(true);
    }
    
    public void removeLinkedToContainer(final EffectContainer container) {
        this.removeLinkedToContainer(container, false);
    }
    
    @Override
    public void removeLinkedToContainer(final EffectContainer container, final boolean withState) {
        final LinkedToEffectContainerIterator userRunningEffects = this.getLinkedToContainerRunningEffects(container);
        this.removeFromIterator(userRunningEffects);
        this.cleanState(withState);
    }
    
    public void removeLinkedToItem(final Item item, final boolean withState) {
        final Iterator it = new LinkedToItemIterator(this, (TLongObjectIterator<RunningEffect>)this.m_effects.iterator(), item);
        this.removeFromIterator(it);
        this.cleanState(withState);
    }
    
    private void removeFromIterator(final Iterator<RunningEffect> userRunningEffects) {
        this.removeFromIterator(userRunningEffects, false);
    }
    
    private void removeFromIterator(final Iterator<RunningEffect> it, final boolean notifyUnapply) {
        final Collection<RunningEffect> effectsToRemove = new ArrayList<RunningEffect>();
        while (it.hasNext()) {
            effectsToRemove.add(it.next());
        }
        this.removeEffects(effectsToRemove, notifyUnapply);
    }
    
    private void removeEffects(final Iterable<RunningEffect> userRunningEffects, final boolean notifyUnapply) {
        for (final RunningEffect effect : userRunningEffects) {
            final WakfuRunningEffect re = (WakfuRunningEffect)effect;
            if (notifyUnapply) {
                re.setNotifyUnapplicationForced(true);
            }
            if (re.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
                this.m_runningEffectStateToRemove.add((StateRunningEffect)re);
            }
            else {
                if (!this.effectContainerIsNotState(re)) {
                    continue;
                }
                this.m_effects.remove(re.getUniqueId());
                this.onEffectRemoved(re);
            }
        }
    }
    
    @Override
    public void removeLinkedToContainer(final EffectContainer container, final boolean withState, final boolean notifyUnapplication) {
        final LinkedToEffectContainerIterator userRunningEffects = this.getLinkedToContainerRunningEffects(container);
        while (userRunningEffects.hasNext()) {
            final WakfuRunningEffect re = (WakfuRunningEffect)userRunningEffects.next();
            if (re.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
                re.setNotifyUnapplicationForced(notifyUnapplication);
                this.m_runningEffectStateToRemove.add((StateRunningEffect)re);
            }
            else {
                if (!this.effectContainerIsNotState(re)) {
                    continue;
                }
                re.setNotifyUnapplicationForced(notifyUnapplication);
                userRunningEffects.remove();
            }
        }
        this.cleanState(withState);
    }
    
    private boolean effectContainerIsNotState(final WakfuRunningEffect re) {
        return ((RunningEffect<FX, WakfuEffectContainer>)re).getEffectContainer() == null || ((RunningEffect<FX, WakfuEffectContainer>)re).getEffectContainer().getContainerType() != 1;
    }
    
    public void removeLinkedToContainerType(final int containerType) {
        this.removeLinkedToContainerType(containerType, false, false);
    }
    
    public void removeLinkedToContainerType(final int containerType, final boolean withState, final boolean notifyUnapplication) {
        final List<EffectContainer> toRemove = new ArrayList<EffectContainer>();
        final TLongObjectIterator<RunningEffect> it = (TLongObjectIterator<RunningEffect>)this.m_effects.iterator();
        while (it.hasNext()) {
            it.advance();
            final RunningEffect effect = it.value();
            if (effect.getEffectContainer() != null && effect.getEffectContainer().getContainerType() == containerType && !toRemove.contains(effect.getEffectContainer())) {
                toRemove.add(effect.getEffectContainer());
            }
        }
        for (final EffectContainer containerToRemove : toRemove) {
            if (notifyUnapplication) {
                this.removeLinkedToContainer(containerToRemove, withState, notifyUnapplication);
            }
            else {
                this.removeLinkedToContainer(containerToRemove, withState);
            }
        }
    }
    
    public void removeLinkedToProperty(final FightPropertyType prop) {
        final List<EffectContainer> toRemove = new ArrayList<EffectContainer>();
        final TLongObjectIterator<RunningEffect> it = (TLongObjectIterator<RunningEffect>)this.m_effects.iterator();
        while (it.hasNext()) {
            it.advance();
            final RunningEffect effect = it.value();
            if (effect instanceof PropertyApply) {
                final PropertyApply applier = (PropertyApply)effect;
                if (applier.getProperty() != prop || toRemove.contains(((RunningEffect<FX, Object>)applier).getEffectContainer())) {
                    continue;
                }
                toRemove.add(((RunningEffect<FX, WakfuEffectContainer>)applier).getEffectContainer());
            }
        }
        for (final EffectContainer container : toRemove) {
            this.removeLinkedToContainer(container, true);
        }
    }
    
    public int removeStatesFromId(final int stateBaseId) {
        int count = 0;
        final TLongObjectIterator<RunningEffect> it = (TLongObjectIterator<RunningEffect>)this.m_effects.iterator();
        while (it.hasNext()) {
            it.advance();
            final RunningEffect effect = it.value();
            if (effect instanceof StateRunningEffect) {
                final StateRunningEffect runningEffect = (StateRunningEffect)effect;
                final State state = runningEffect.getState();
                if (state == null || state.getStateBaseId() != stateBaseId) {
                    continue;
                }
                this.m_runningEffectStateToRemove.add(runningEffect);
                ++count;
            }
        }
        this.cleanState(true);
        return count;
    }
    
    public List<StateRunningEffect> getRunningState() {
        final List<StateRunningEffect> states = new ArrayList<StateRunningEffect>();
        final TLongObjectIterator<RunningEffect> it = (TLongObjectIterator<RunningEffect>)this.m_effects.iterator();
        while (it.hasNext()) {
            it.advance();
            final RunningEffect effect = it.value();
            if (effect.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
                states.add((StateRunningEffect)effect);
            }
        }
        return states;
    }
    
    public StateRunningEffect getRunningState(final int stateId) {
        final TLongObjectIterator<RunningEffect> it = (TLongObjectIterator<RunningEffect>)this.m_effects.iterator();
        while (it.hasNext()) {
            it.advance();
            final RunningEffect effect = it.value();
            if (effect.getId() == RunningEffectConstants.RUNNING_STATE.getId() && ((StateRunningEffect)effect).getState().getStateBaseId() == stateId) {
                return (StateRunningEffect)effect;
            }
        }
        return null;
    }
    
    @Nullable
    public RunningEffect getFirstWithProperty(final RunningEffectPropertyType property) {
        for (final RunningEffect runningEffect : this) {
            if (((WakfuRunningEffect)runningEffect).hasProperty(property)) {
                return runningEffect;
            }
        }
        return null;
    }
    
    @Override
    public void onEffectRemoved(final RunningEffect removedEffect) {
        super.onEffectRemoved(removedEffect);
        final long uid = removedEffect.getUniqueId();
        final TLongLongIterator iterator = this.m_clocks.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            final long key = iterator.key();
            if (iterator.value() == uid) {
                MessageScheduler.getInstance().removeClock(key);
                iterator.remove();
            }
        }
        this.m_endTimes.remove(uid);
    }
    
    static {
        TimedRunningEffectManager.LOG_REM = true;
        REMOVE_CLOCK_PROCEDURE = new TLongProcedure() {
            @Override
            public boolean execute(final long clockId) {
                MessageScheduler.getInstance().removeClock(clockId);
                return true;
            }
        };
    }
}
