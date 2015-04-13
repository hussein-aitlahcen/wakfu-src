package com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.external.*;
import org.apache.log4j.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;

public abstract class RunningEffect<FX extends Effect, EC extends EffectContainer> extends BinarSerial implements StaticRunningEffect<FX, EC>, Releasable, Parameterized
{
    private static final byte MAX_EFFECT_USER_PLAY_CHANGE_FOR_ONE_EFFECT = 3;
    protected static final Logger m_logger;
    protected ObjectPool m_pool;
    private byte m_referenceCount;
    protected FX m_genericEffect;
    protected EC m_effectContainer;
    protected EffectUser m_caster;
    protected EffectUser m_target;
    protected final Point3 m_targetCell;
    private EffectExecutionParameters m_params;
    private RunningEffectManager m_managerWhereIamStored;
    protected EffectContext<FX> m_context;
    protected int m_id;
    protected long m_uid;
    protected long m_baseUid;
    protected RunningEffectStatus m_status;
    protected int m_value;
    protected final BitSet m_triggers;
    protected int m_maxExecutionCount;
    private RunningEffect<FX, EC> m_parent;
    protected boolean m_valueComputationEnabled;
    private boolean m_unapplied;
    private static short m_limitedApplyCount;
    private static final short MAX_APPLY_FOR_ONE_RUN = 200;
    protected AbsoluteFightTime m_startTime;
    private static final boolean DEBUG_MODE = false;
    private boolean m_isPooled;
    private boolean m_cancelled;
    private RunningEffectEvent m_linkedEvent;
    private static long uid;
    private static RunningEffectUIDGenerator m_UIDGenerator;
    protected boolean m_forceInstant;
    protected EffectExecutionResult m_staticResult;
    private boolean m_tempStaticResultCreation;
    protected boolean m_isStatic;
    private static boolean m_useResult;
    private boolean m_dontTriggerAnythingForced;
    public BinarSerialPart CORE;
    public BinarSerialPart CASTER;
    public BinarSerialPart TARGET;
    
    public static void resetLimitedApplyCount() {
        RunningEffect.m_limitedApplyCount = 0;
    }
    
    public static void setUIDGenerator(final RunningEffectUIDGenerator UIDGenerator) {
        RunningEffect.m_UIDGenerator = UIDGenerator;
    }
    
    private static long getNextUID() {
        if (RunningEffect.uid < Long.MAX_VALUE) {
            return RunningEffect.uid++;
        }
        return RunningEffect.uid = 0L;
    }
    
    protected RunningEffect() {
        super();
        this.m_targetCell = new Point3();
        this.m_managerWhereIamStored = null;
        this.m_triggers = new BitSet();
        this.m_valueComputationEnabled = true;
        this.m_isPooled = false;
        this.m_forceInstant = false;
        this.m_tempStaticResultCreation = false;
        this.m_isStatic = false;
        this.m_dontTriggerAnythingForced = false;
        this.CORE = new BinarSerialPart(34) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(RunningEffect.this.getUniqueId());
                buffer.putLong(RunningEffect.this.getBaseUid());
                buffer.putInt((RunningEffect.this.m_genericEffect == null) ? RunningEffect.this.getDefaultEffect().getEffectId() : RunningEffect.this.m_genericEffect.getEffectId());
                if (RunningEffect.this.m_targetCell == null) {
                    buffer.putInt(0);
                    buffer.putInt(0);
                    buffer.putShort((short)0);
                }
                else {
                    buffer.putInt(RunningEffect.this.m_targetCell.getX());
                    buffer.putInt(RunningEffect.this.m_targetCell.getY());
                    buffer.putShort(RunningEffect.this.m_targetCell.getZ());
                }
                buffer.putInt(RunningEffect.this.m_value);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final long id = buffer.getLong();
                RunningEffect.this.setUid(id);
                final long buid = buffer.getLong();
                RunningEffect.this.m_baseUid = buid;
                final int genericEffectId = buffer.getInt();
                if (RunningEffect.this.m_context != null && RunningEffect.this.m_context.getEffectManager() != null) {
                    RunningEffect.this.m_genericEffect = RunningEffect.this.m_context.getEffectManager().getEffect(genericEffectId);
                    if (RunningEffect.this.m_genericEffect == null) {
                        RunningEffect$1.m_logger.error((Object)("Impossible de d\u00e9s\u00e9rialiser un WakfuRunningEffect : generic effet inconnu : " + genericEffectId));
                    }
                }
                RunningEffect.this.m_targetCell.set(buffer.getInt(), buffer.getInt(), buffer.getShort());
                RunningEffect.this.m_value = buffer.getInt();
            }
        };
        this.CASTER = new BinarSerialPart(8) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong((RunningEffect.this.m_caster != null) ? RunningEffect.this.m_caster.getId() : 0L);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final long casterId = buffer.getLong();
                if (casterId == 0L) {
                    RunningEffect.this.m_caster = null;
                }
                else if (RunningEffect.this.m_context != null && RunningEffect.this.m_context.getEffectUserInformationProvider() != null) {
                    RunningEffect.this.setCaster(RunningEffect.this.m_context.getEffectUserInformationProvider().getEffectUserFromId(casterId));
                    if (RunningEffect.this.m_caster == null) {}
                }
                else {
                    RunningEffect$2.m_logger.error((Object)("pas de contexte, impossible de r\u00e9cuperer la cible type de RE : " + RunningEffect.this.m_id));
                }
            }
        };
        this.TARGET = new BinarSerialPart(8) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong((RunningEffect.this.m_target != null) ? RunningEffect.this.m_target.getId() : 0L);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final long targetId = buffer.getLong();
                if (targetId == 0L) {
                    RunningEffect.this.m_target = null;
                }
                else if (RunningEffect.this.m_context != null && RunningEffect.this.m_context.getEffectUserInformationProvider() != null) {
                    RunningEffect.this.m_target = RunningEffect.this.m_context.getEffectUserInformationProvider().getEffectUserFromId(targetId);
                    if (RunningEffect.this.m_target == null) {}
                }
            }
        };
        this.unparametrize();
        this.m_isPooled = false;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public void setId(final int id) {
        this.m_id = id;
    }
    
    public long getUniqueId() {
        return this.m_uid;
    }
    
    public long getBaseUid() {
        return this.m_baseUid;
    }
    
    public void setUid(final long uid) {
        this.m_uid = uid;
    }
    
    @Override
    public RunningEffectStatus getRunningEffectStatus() {
        return this.m_status;
    }
    
    @Override
    public void setRunningEffectStatus(final RunningEffectStatus status) {
        this.m_status = status;
    }
    
    public FX getGenericEffect() {
        return this.m_genericEffect;
    }
    
    public void setGenericEffect(final FX genericEffect) {
        this.m_genericEffect = genericEffect;
    }
    
    public void initialiseParent() {
        this.m_staticResult = new EffectExecutionResult();
        this.m_isStatic = true;
    }
    
    public EffectUser getCaster() {
        return this.m_caster;
    }
    
    public void setCaster(final EffectUser caster) {
        this.m_caster = caster;
    }
    
    public EffectUser getTarget() {
        return this.m_target;
    }
    
    public Point3 getTargetCell() {
        return this.m_targetCell;
    }
    
    public EC getEffectContainer() {
        return this.m_effectContainer;
    }
    
    public void setTarget(final EffectUser target) {
        this.m_target = target;
    }
    
    public void setTargetCell(final int targetCellX, final int targetCellY, final short targetCellZ) {
        this.m_targetCell.set(targetCellX, targetCellY, targetCellZ);
    }
    
    public EffectExecutionParameters getParams() {
        return this.m_params;
    }
    
    public void setEffectContainer(final EC effectContainer) {
        this.m_effectContainer = effectContainer;
    }
    
    public void setContext(final EffectContext context) {
        this.m_context = (EffectContext<FX>)context;
    }
    
    public EffectContext getContext() {
        return this.m_context;
    }
    
    public int getValue() {
        return this.m_value;
    }
    
    public void forceValue(final int forcedValue) {
        this.m_value = forcedValue;
    }
    
    public int getEffectId() {
        if (this.m_genericEffect == null) {
            return -1;
        }
        return this.m_genericEffect.getEffectId();
    }
    
    public boolean mustBeExecutedNow() {
        return false;
    }
    
    @Override
    public void release() {
        if (this.m_referenceCount > 0) {
            RunningEffect.m_logger.error((Object)("On essaye de release un RunningEffect encore r\u00e9f\u00e9renc\u00e9 refCount=" + this.m_referenceCount + " id=" + this.m_id));
            return;
        }
        if (this.m_isPooled && this.isReleased()) {
            RunningEffect.m_logger.error((Object)("Double release sur un " + this.getClass().getSimpleName() + " hashCode : " + this.hashCode() + " : " + ExceptionFormatter.currentStackTrace()));
            this.onCheckIn();
            return;
        }
        if (!this.m_isPooled) {
            this.onCheckIn();
            return;
        }
        this.returnObjectInPool();
    }
    
    public boolean isReleased() {
        return this.m_pool == null;
    }
    
    private void returnObjectInPool() {
        try {
            this.m_pool.returnObject(this);
        }
        catch (Exception e) {
            RunningEffect.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + " normalement impossible"));
        }
        this.m_pool = null;
    }
    
    public List<List<EffectUser>> determineTargets() {
        return this.determineTargets(this.m_genericEffect, this.m_caster, this.m_context, this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ());
    }
    
    public void clearStaticResult() {
        if (this.m_staticResult != null) {
            this.m_staticResult.clear();
        }
    }
    
    @Override
    public EffectExecutionResult run(final FX genericEffect, final EC container, final EffectContext context, final EffectUser launcher, final int targetCellX, final int targetCellY, final short targetCellZ, final EffectUser target, final EffectExecutionParameters params) {
        if (this.m_staticResult != null) {
            this.m_staticResult.clear();
        }
        else {
            this.m_staticResult = EffectExecutionResult.checkOut();
            this.m_tempStaticResultCreation = true;
        }
        if (params == null || params.resetLimitedApplyCount()) {
            resetLimitedApplyCount();
        }
        this.setParameters(genericEffect, container, context, launcher, null, targetCellX, targetCellY, targetCellZ, params);
        if (this.useTargetCell() && !this.useTarget()) {
            final RunningEffect reOnCellOnly = this.newParameterizedInstance();
            if (reOnCellOnly != null) {
                if (reOnCellOnly.hasDuration() && !reOnCellOnly.isInfinite()) {
                    reOnCellOnly.pushRunningEffectDurationTimeEventInTimeline();
                }
                if (reOnCellOnly.isValueComputationEnabled()) {
                    if (reOnCellOnly.useTarget() && this.m_target == null) {
                        RunningEffect.m_logger.warn((Object)(" on veut calculer un effet qui a besoin d'une cible, sans cible : " + this.actionAndGenericEffectIdString()));
                    }
                    if (reOnCellOnly.useCaster() && this.m_caster == null) {
                        RunningEffect.m_logger.warn((Object)("on veut calculer un effet qui a besoin d'un caster, sans caster : " + this.actionAndGenericEffectIdString()));
                    }
                    if (reOnCellOnly.useTargetCell() && this.m_targetCell == null) {
                        RunningEffect.m_logger.warn((Object)("on veut calculer un effet qui a besoin d'une cellule cible, sans cellule cible : " + this.actionAndGenericEffectIdString()));
                    }
                    reOnCellOnly.computeValue(null);
                }
                if (this.m_caster != null && this.m_caster.getRunningEffectManager() != null) {
                    if (reOnCellOnly.isInfinite() || reOnCellOnly.hasDuration()) {
                        this.m_caster.getRunningEffectManager().storeEffect(reOnCellOnly);
                    }
                }
                else {
                    reOnCellOnly.forceInstant();
                }
                if (!reOnCellOnly.mustBeTriggered()) {
                    reOnCellOnly.askForExecution();
                }
            }
        }
        if (this.useTarget()) {
            if (target == null || (genericEffect != null && genericEffect.isShouldRecomputeTarget())) {
                final List<List<EffectUser>> targets = this.determineTargets(genericEffect, launcher, context, targetCellX, targetCellY, targetCellZ);
                for (final List<EffectUser> stepTargets : targets) {
                    this.applyOnTargets(stepTargets);
                    if (this.m_staticResult != null) {
                        this.m_staticResult.addTargettedEffectUsers(stepTargets);
                    }
                }
            }
            else {
                this.applyOnTargets(target);
                if (this.m_staticResult != null) {
                    this.m_staticResult.addTargettedEffectUser(target);
                }
            }
        }
        if (this.m_isStatic) {
            this.clearParameters();
        }
        return this.m_staticResult;
    }
    
    public void setParameters(final FX genericEffect, final EC container, final EffectContext context, final EffectUser caster, final EffectUser target, final int targetCellX, final int targetCellY, final short targetCellZ, final EffectExecutionParameters params) {
        this.m_genericEffect = genericEffect;
        this.m_effectContainer = container;
        this.m_caster = caster;
        this.m_context = (EffectContext<FX>)context;
        this.m_target = target;
        this.setTargetCell(targetCellX, targetCellY, targetCellZ);
        if (this.m_params != null) {
            this.m_params.release();
        }
        if (params != null) {
            this.m_params = params.newInstance();
        }
        else {
            this.m_params = null;
        }
        this.initialiseExecutionCount();
    }
    
    public void setExecutionParameters(final EffectExecutionParameters parameters) {
        if (this.m_params != null) {
            this.m_params.release();
        }
        this.m_params = parameters;
    }
    
    protected void initialiseExecutionCount() {
        if (this.m_genericEffect != null) {
            this.m_maxExecutionCount = this.m_genericEffect.getMaximumExecutions();
        }
    }
    
    protected void clearParameters() {
        this.m_genericEffect = null;
        this.m_effectContainer = null;
        this.m_caster = null;
        this.m_context = null;
        this.m_target = null;
        if (this.m_params != null) {
            this.m_params.release();
        }
        this.m_params = null;
    }
    
    public RunningEffect getParent() {
        return this.m_parent;
    }
    
    public void setParent(final RunningEffect<FX, EC> parent) {
        this.m_parent = parent;
    }
    
    @Override
    public RunningEffect newInstance(final EffectContext context, final AbstractEffectManager<FX> manager) {
        final RunningEffect<FX, EC> re = this.newParameterizedInstance();
        re.setId(this.getId());
        re.setContext(context);
        re.setRunningEffectStatus(this.getRunningEffectStatus());
        return re;
    }
    
    public RunningEffect<FX, EC> newParameterizedInstance() {
        final RunningEffect<FX, EC> re = this.newInstance();
        if (RunningEffect.m_UIDGenerator != null) {
            re.m_uid = RunningEffect.m_UIDGenerator.getNextUID(re);
        }
        else {
            re.m_uid = getNextUID();
        }
        re.cloneParameters(this);
        re.m_baseUid = re.m_uid;
        return re;
    }
    
    public abstract RunningEffect<FX, EC> newInstance();
    
    @Override
    public void onCheckOut() {
        this.unparametrize();
        this.m_unapplied = false;
        this.m_isPooled = true;
    }
    
    protected void unparametrize() {
        this.m_managerWhereIamStored = null;
        this.m_referenceCount = 0;
        this.m_maxExecutionCount = -1;
        this.m_caster = null;
        this.m_target = null;
        this.m_context = null;
        this.m_genericEffect = null;
        this.m_effectContainer = null;
        this.m_valueComputationEnabled = true;
        this.m_triggers.clear();
        this.m_isStatic = false;
        this.m_staticResult = null;
        this.m_uid = -1L;
        this.m_baseUid = -1L;
        this.m_tempStaticResultCreation = false;
        this.m_startTime = null;
        this.m_cancelled = false;
    }
    
    @Override
    public void onCheckIn() {
        this.m_dontTriggerAnythingForced = false;
        this.unparametrize();
        this.m_targetCell.set(Integer.MIN_VALUE, Integer.MIN_VALUE, (short)(-32768));
        if (this.m_params != null) {
            this.m_params.release();
        }
        this.m_params = null;
        this.m_value = 0;
        this.m_parent = null;
        this.m_forceInstant = false;
        if (this.m_linkedEvent != null) {
            this.m_linkedEvent.setRunningEffectToNull();
        }
        this.m_linkedEvent = null;
        if (this.m_staticResult != null && this.m_tempStaticResultCreation) {
            this.m_staticResult.release();
        }
    }
    
    protected void cloneParameters(final RunningEffect<FX, EC> re) {
        this.m_dontTriggerAnythingForced = re.m_dontTriggerAnythingForced;
        this.m_genericEffect = re.m_genericEffect;
        this.m_effectContainer = re.m_effectContainer;
        this.m_caster = re.m_caster;
        this.m_target = re.m_target;
        this.m_targetCell.set(re.m_targetCell);
        if (this.m_params != null) {
            this.m_params.release();
        }
        if (re.m_params != null) {
            this.m_params = re.m_params.newInstance();
        }
        else {
            this.m_params = null;
        }
        this.m_context = re.m_context;
        this.m_id = re.m_id;
        this.m_status = re.m_status;
        this.m_value = re.m_value;
        this.setTriggersToExecute();
        this.m_triggers.or(re.m_triggers);
        this.addTriggersToExecute();
        this.m_maxExecutionCount = re.m_maxExecutionCount;
        this.m_parent = re.m_parent;
        this.m_forceInstant = false;
        this.m_staticResult = re.m_staticResult;
        this.m_valueComputationEnabled = re.m_valueComputationEnabled;
    }
    
    public RunningEffectManager getManagerWhereIamStored() {
        return this.m_managerWhereIamStored;
    }
    
    public void setManagerWhereIamStored(final RunningEffectManager managerWhereIamStored) {
        this.m_managerWhereIamStored = managerWhereIamStored;
    }
    
    public void disableValueComputation() {
        this.m_valueComputationEnabled = false;
    }
    
    public void enableValueComputation() {
        this.m_valueComputationEnabled = true;
    }
    
    public boolean isValueComputationEnabled() {
        return this.m_valueComputationEnabled;
    }
    
    public EffectExecutionResult getStaticResult() {
        return this.m_staticResult;
    }
    
    public BitSet getListeningTriggersBeforeComputation() {
        if (this.m_genericEffect == null) {
            return null;
        }
        return this.m_genericEffect.getListeningTriggersBeforeComputation();
    }
    
    public BitSet getListeningTriggersBeforeExecution() {
        if (this.m_genericEffect == null) {
            return null;
        }
        return this.m_genericEffect.getListeningTriggersBeforeExecution();
    }
    
    public BitSet getListeningTriggersAfterExecution() {
        if (this.m_genericEffect == null) {
            return null;
        }
        return this.m_genericEffect.getListeningTriggersAfterExecution();
    }
    
    public BitSet getListeningTriggesrAfterAllExecutions() {
        if (this.m_genericEffect == null) {
            return null;
        }
        return this.m_genericEffect.getListeningTriggerAfterAllExecutions();
    }
    
    public BitSet getListeningTriggesrNotRelatedToExecutions() {
        if (this.m_genericEffect == null) {
            return null;
        }
        return this.m_genericEffect.getListeningTriggerNotRelatedToExecutions();
    }
    
    public boolean canBeTriggerBy(final int trigger) {
        return (this.getListeningTriggersBeforeExecution() != null && this.getListeningTriggersBeforeExecution().get(trigger)) || (this.getListeningTriggersBeforeComputation() != null && this.getListeningTriggersBeforeComputation().get(trigger)) || (this.getListeningTriggersAfterExecution() != null && this.getListeningTriggersAfterExecution().get(trigger)) || (this.getListeningTriggesrAfterAllExecutions() != null && this.getListeningTriggesrAfterAllExecutions().get(trigger)) || (this.getListeningTriggesrNotRelatedToExecutions() != null && this.getListeningTriggesrNotRelatedToExecutions().get(trigger));
    }
    
    public BitSet getListeningTriggerForUnapplication() {
        if (this.m_genericEffect == null) {
            return null;
        }
        return this.m_genericEffect.getListeningTriggerForUnapplication();
    }
    
    @Override
    public BitSet getTriggersToExecute() {
        if (this.m_genericEffect != null && this.m_genericEffect.getExecutionTriggersAdditionnal() != null) {
            this.m_triggers.or(this.m_genericEffect.getExecutionTriggersAdditionnal());
        }
        return this.m_triggers;
    }
    
    public void setTriggersToExecute() {
        this.m_triggers.clear();
        this.m_triggers.set(0);
    }
    
    protected void addTriggersToExecute() {
    }
    
    public boolean mustBeTriggered() {
        return (this.getListeningTriggersBeforeExecution() != null && this.getListeningTriggersBeforeExecution().length() > 0) || (this.getListeningTriggersBeforeComputation() != null && this.getListeningTriggersBeforeComputation().length() > 0) || (this.getListeningTriggersAfterExecution() != null && this.getListeningTriggersAfterExecution().length() > 0) || (this.getListeningTriggesrAfterAllExecutions() != null && this.getListeningTriggesrAfterAllExecutions().length() > 0) || (this.getListeningTriggesrNotRelatedToExecutions() != null && this.getListeningTriggesrNotRelatedToExecutions().length() > 0);
    }
    
    public abstract boolean useCaster();
    
    public abstract boolean useTarget();
    
    public abstract boolean useTargetCell();
    
    public void update(final int whatToUpdate, final float howMuchToUpate, final boolean set) {
    }
    
    public boolean isSelfTrigger() {
        return this.m_genericEffect != null && this.m_genericEffect.isSelfTrigger();
    }
    
    public boolean preCalculateTriggerTarget() {
        return this.m_genericEffect != null && this.m_genericEffect.preCalculateTriggerTarget();
    }
    
    public void trigger(final byte... triggerTypes) {
        if (this.getTriggersToExecute() == null) {
            return;
        }
        for (final byte triggerType : triggerTypes) {
            this.trigger(triggerType);
        }
    }
    
    public void forceDontTriggerAnything() {
        this.m_dontTriggerAnythingForced = true;
    }
    
    public boolean dontTriggerAnything() {
        return this.m_dontTriggerAnythingForced || (this.m_genericEffect != null && this.m_genericEffect.dontTriggerAnything());
    }
    
    public boolean isDontTriggerAnythingForced() {
        return this.m_dontTriggerAnythingForced;
    }
    
    public boolean trigger(final byte triggerType) {
        if (this.getTriggersToExecute() == null) {
            return false;
        }
        boolean somethingWasTriggered = false;
        switch (triggerType) {
            case 1: {
                final List<EffectUser> previousEffectUsers = new ArrayList<EffectUser>();
                while (this.getCaster() != null && !previousEffectUsers.contains(this.getCaster())) {
                    previousEffectUsers.add(this.getCaster());
                    somethingWasTriggered |= this.getCaster().trigger(this.getTriggersToExecute(), this, (byte)10);
                }
                previousEffectUsers.clear();
                while (this.getTarget() != null && !previousEffectUsers.contains(this.getTarget())) {
                    previousEffectUsers.add(this.getTarget());
                    somethingWasTriggered |= this.getTarget().trigger(this.getTriggersToExecute(), this, (byte)1);
                }
                break;
            }
            case 2: {
                if (this.getCaster() != null) {
                    somethingWasTriggered |= this.getCaster().trigger(this.getTriggersToExecute(), this, (byte)20);
                }
                if (this.getTarget() != null) {
                    somethingWasTriggered |= this.getTarget().trigger(this.getTriggersToExecute(), this, (byte)2);
                    break;
                }
                break;
            }
            case 3: {
                if (this.getCaster() != null) {
                    somethingWasTriggered |= this.getCaster().trigger(this.getTriggersToExecute(), this, (byte)30);
                }
                if (this.getTarget() != null) {
                    somethingWasTriggered |= this.getTarget().trigger(this.getTriggersToExecute(), this, (byte)3);
                    break;
                }
                break;
            }
            case 4: {
                if (this.getCaster() != null) {
                    somethingWasTriggered |= this.getCaster().trigger(this.getTriggersToExecute(), this, (byte)40);
                }
                if (this.getTarget() != null) {
                    somethingWasTriggered |= this.getTarget().trigger(this.getTriggersToExecute(), this, (byte)4);
                    break;
                }
                break;
            }
            case 6: {
                if (this.getCaster() != null) {
                    somethingWasTriggered |= this.getCaster().trigger(this.getTriggersToExecute(), this, (byte)60);
                }
                if (this.getTarget() != null) {
                    somethingWasTriggered |= this.getTarget().trigger(this.getTriggersToExecute(), this, (byte)6);
                    break;
                }
                break;
            }
        }
        return somethingWasTriggered;
    }
    
    public void onApplication() {
        if (this.m_context != null && this.m_context.getEffectExecutionListener() != null) {
            this.m_context.getEffectExecutionListener().onEffectApplication(this);
        }
    }
    
    public void onUnApplication() {
        if (this.m_context != null && this.m_context.getEffectExecutionListener() != null) {
            this.m_context.getEffectExecutionListener().onEffectUnApplication(this);
        }
    }
    
    public void applyOnTargets(final EffectUser target) {
        if (target == null) {
            return;
        }
        this.applyOnTargets(Collections.singletonList(target));
    }
    
    public boolean applyOnTargets(final List<EffectUser> targets) {
        if (targets == null || targets.size() == 0) {
            return false;
        }
        boolean bAtLeastOneApplication = false;
        ++RunningEffect.m_limitedApplyCount;
        if (RunningEffect.m_limitedApplyCount >= 200) {
            if (RunningEffect.m_limitedApplyCount == 200) {
                final StringBuilder errorLog = new StringBuilder().append("boucle infinie pour un runningEffect ? action id=").append(this.getId()).append(", effect Id ").append(this.getEffectId());
                if (this.getContext() != null && this.getContext().getSpellCaster() != null) {
                    errorLog.append(", Sort a l'origine ").append(this.getContext().getSpellCaster().getSpellId());
                }
                errorLog.append(", historique de trigger ").append(TriggerLoopWatcher.INSTANCE);
                RunningEffect.m_logger.error((Object)errorLog.toString(), (Throwable)new NullPointerException("erreur generee pour etude de stack"));
            }
            return false;
        }
        final ArrayList<RunningEffect> runningEffectsToApply = new ArrayList<RunningEffect>();
        final ArrayList<RunningEffect> runningEffectsToReleaseAfterApply = new ArrayList<RunningEffect>();
        for (final EffectUser target : targets) {
            final RunningEffect re = this.newParameterizedInstance();
            re.setTarget(target);
            if (this.m_context != null && this.m_context.getTimeline() != null) {
                re.m_startTime = this.m_context.getTimeline().now();
            }
            if (!re.mustBeTriggered()) {
                if (target != null) {
                    re.trigger((byte)1);
                }
                if (re.m_cancelled) {
                    re.release();
                    continue;
                }
                if (this.isValueComputationEnabled() && re.isValueComputationEnabled()) {
                    if (this.useCaster() && re.getCaster() == null) {
                        RunningEffect.m_logger.warn((Object)("on veut calculer un effet qui a besoin d'un caster, sans caster " + this.getId() + ((this.m_genericEffect != null) ? (" generic effect " + this.m_genericEffect.getEffectId() + "action " + this.m_genericEffect.getActionId()) : "")));
                    }
                    if (this.useTargetCell() && re.getTargetCell() == null) {
                        RunningEffect.m_logger.warn((Object)("on veut calculer un effet qui a besoin d'une cellule cible, sans cellule cible" + this.getId() + ((this.m_genericEffect != null) ? (" generic effect" + this.m_genericEffect.getEffectId() + " action " + this.m_genericEffect.getActionId()) : "")));
                    }
                    re.computeValue(null);
                }
                bAtLeastOneApplication = true;
                runningEffectsToApply.add(re);
            }
            else if (re.hasDuration() && !re.isInfinite()) {
                bAtLeastOneApplication = true;
                re.pushRunningEffectDurationTimeEventInTimeline();
            }
            if (re.hasDuration() || re.hasExecutionDelay()) {
                if (this.mustStoreOnCaster()) {
                    if (re.getCaster() != null && re.getCaster().getRunningEffectManager() != null) {
                        re.getCaster().getRunningEffectManager().storeEffect(re);
                        re.onApplication();
                    }
                    else {
                        runningEffectsToReleaseAfterApply.add(re);
                    }
                }
                else if (re.getTarget() != null && re.getTarget().getRunningEffectManager() != null) {
                    re.getTarget().getRunningEffectManager().storeEffect(re);
                    re.onApplication();
                }
                else {
                    runningEffectsToReleaseAfterApply.add(re);
                }
            }
        }
        for (final RunningEffect re2 : runningEffectsToApply) {
            if (re2.hasDuration() && !re2.isInfinite()) {
                re2.pushRunningEffectDurationTimeEventInTimeline();
            }
            if (!re2.hasExecutionDelay()) {
                re2.askForExecution();
            }
            else {
                re2.pushRunningEffectDelayedTimeEventInTimeline();
            }
        }
        for (final RunningEffect re2 : runningEffectsToReleaseAfterApply) {
            re2.release();
        }
        return bAtLeastOneApplication;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.m_cancelled = cancelled;
    }
    
    public abstract boolean canBeExecuted();
    
    public boolean canBeTriggeredExecuted(final RunningEffect linkedRE) {
        return true;
    }
    
    public void askForExecution() {
        if (!this.canBeExecuted()) {
            this.checkEffectValidity(false);
            return;
        }
        if (this.getTarget() != null && this.getTarget().isOutOfPlay() && !this.canBeExecutedOnKO()) {
            this.checkEffectValidity(false);
            return;
        }
        this.trigger((byte)2);
        if (this.getTarget() != null && this.getTarget().isOutOfPlay() && !this.canBeExecutedOnKO()) {
            this.checkEffectValidity(false);
            return;
        }
        this.execute(this.getParent(), false);
    }
    
    protected void afterExecutionTriggerHook() {
    }
    
    public void forceInstant() {
        this.m_forceInstant = true;
    }
    
    protected void initTriggeredEffect(final RunningEffect re) {
        re.forceInstant();
    }
    
    public void askForTriggeredExecution(final RunningEffect linkedRE) {
        final EffectUser triggeringCaster = this.getTriggeringCaster(linkedRE);
        final long casterId = (triggeringCaster != null) ? triggeringCaster.getId() : 0L;
        final EffectUser triggeringTarget = this.getTriggeringTarget(linkedRE);
        final long targetId = (triggeringTarget != null) ? triggeringTarget.getId() : 0L;
        if (TriggerLoopWatcher.INSTANCE.hasBeenTriggered(this.getEffectId(), casterId, targetId)) {
            return;
        }
        if (!this.canBeTriggeredExecuted(linkedRE)) {
            return;
        }
        final RunningEffect re = this.newParameterizedInstance();
        re.setParent(this);
        re.m_baseUid = this.getUniqueId();
        this.initTriggeredEffect(re);
        if (linkedRE != null) {
            re.setCaster(this.getTriggeringCaster(linkedRE));
            re.setTarget(this.getTriggeringTarget(linkedRE));
            if (re.getTarget() != null) {
                final EffectUser target = re.getTarget();
                re.setTargetCell(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude());
            }
            else {
                final Point3 cell = linkedRE.getTargetCell();
                re.setTargetCell(cell.getX(), cell.getY(), cell.getZ());
            }
        }
        if ((re.useTarget() && re.getTarget() == null) || (re.useCaster() && re.getCaster() == null) || (re.getTarget() != null && re.getTarget().isOutOfPlay() && !this.canBeExecutedOnKO()) || (re.useTargetCell() && re.getTargetCell() == null)) {
            if (re.useTarget() && re.getTarget() == null) {
                RunningEffect.m_logger.error((Object)("on veut executer un effet qui a besoin d'une cible, sans cible (action=" + re.actionAndGenericEffectIdString() + ")"));
            }
            if (re.useCaster() && re.getCaster() == null) {
                RunningEffect.m_logger.error((Object)("on veut executer un effet qui a besoin d'un caster, sans caster\t(action = " + re.actionAndGenericEffectIdString() + ")"));
            }
            if (re.useTargetCell() && re.getTargetCell() == null) {
                RunningEffect.m_logger.error((Object)("on veut executer un effet qui a besoin d'une cellule cible, sans cellule cible (action=" + re.actionAndGenericEffectIdString() + ")"));
            }
            re.release();
            return;
        }
        final TriggerLoopWatcherNode node = TriggerLoopWatcher.INSTANCE.newTrigger(this.getEffectId(), casterId, targetId);
        final boolean isRoot = node.getParent() == null;
        try {
            if (re.isValueComputationEnabled()) {
                re.computeValue(linkedRE);
            }
            re.trigger(1, 2);
            final long id = this.getUniqueId();
            re.execute(linkedRE, true);
            if (this.getUniqueId() == id) {
                this.checkEffectValidity(true);
            }
        }
        catch (Exception e) {
            RunningEffect.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        TriggerLoopWatcher.INSTANCE.endCurrentTrigger();
        if (isRoot) {
            TriggerLoopWatcher.INSTANCE.clear();
        }
    }
    
    public String actionAndGenericEffectIdString() {
        return (this.m_genericEffect == null) ? this.getClass().getSimpleName() : ("Act#" + this.m_genericEffect.getActionId() + "@Eff#" + this.m_genericEffect.getEffectId());
    }
    
    protected EffectUser getTriggeringTarget(final RunningEffect linkedRE) {
        if (linkedRE == null || this.m_genericEffect == null || this.preCalculateTriggerTarget()) {
            return this.m_target;
        }
        if (this.isSelfTrigger()) {
            if (this.m_genericEffect.triggerTargetIsSelf()) {
                return linkedRE.getCaster();
            }
            return linkedRE.getTarget();
        }
        else {
            if (this.m_genericEffect.triggerTargetIsSelf()) {
                return linkedRE.getTarget();
            }
            return linkedRE.getCaster();
        }
    }
    
    protected EffectUser getTriggeringCaster(final RunningEffect linkedRE) {
        return this.m_caster;
    }
    
    public void askForUnapplication() {
        if (this.m_managerWhereIamStored != null) {
            this.m_managerWhereIamStored.removeEffect(this);
        }
        else {
            this.unapply();
        }
    }
    
    public void askForTriggeredUnapplication() {
        this.askForUnapplication();
    }
    
    public boolean hasBeenUnnaplied() {
        return this.m_unapplied;
    }
    
    public void unapply() {
        this.unapply(true);
    }
    
    public void unapplyOverride() {
    }
    
    public void unapply(final boolean checkChanges) {
        this.unapplyOverride();
        if (checkChanges) {
            this.checkEffectUserPlayChanges();
        }
        this.onUnApplication();
        this.m_unapplied = true;
        if (!this.m_isStatic) {
            this.release();
        }
    }
    
    public abstract void pushRunningEffectDelayedTimeEventInTimeline();
    
    public abstract void pushRunningEffectDurationTimeEventInTimeline();
    
    public AbsoluteFightTime getStartTime() {
        return this.m_startTime;
    }
    
    public abstract RelativeFightTime getEndTime();
    
    public abstract boolean hasExecutionDelay();
    
    public boolean mustStoreOnCaster() {
        return this.m_genericEffect != null && this.m_genericEffect.mustStoreOnCaster();
    }
    
    public boolean hasDuration() {
        return !this.m_forceInstant && this.mustBeTriggered();
    }
    
    public abstract boolean isInfinite();
    
    public boolean isPooled() {
        return this.m_isPooled;
    }
    
    public void execute(final RunningEffect triggerRE, final boolean isTriggered) {
        this.checkEffectUserPlayChanges();
        if (this.m_staticResult != null) {
            this.m_staticResult.addExecutedTriggers(this.getTriggersToExecute());
            this.m_staticResult.addOneExecution();
        }
        this.trigger(3, 4);
        this.afterExecutionTriggerHook();
        this.checkEffectValidity(true);
    }
    
    public void checkEffectValidity(final boolean executionSuccessfull) {
        boolean needToBeUnapply = false;
        if (executionSuccessfull && this.m_maxExecutionCount >= 0) {
            if (this.m_maxExecutionCount > 0) {
                --this.m_maxExecutionCount;
            }
            if (this.m_maxExecutionCount == 0) {
                needToBeUnapply = true;
            }
        }
        if (!this.hasBeenUnnaplied() && (!this.hasDuration() || needToBeUnapply)) {
            this.askForUnapplication();
        }
    }
    
    protected void checkEffectUserPlayChanges() {
        byte count = 0;
        try {
            while (this.checkEffectUserPlayChange() && count < 3) {
                ++count;
            }
        }
        catch (Exception e) {
            RunningEffect.m_logger.error((Object)"Exception catch\u00e9e : ", (Throwable)e);
        }
    }
    
    protected boolean checkEffectUserPlayChange() {
        boolean changed = false;
        if (this.m_target != null && !this.m_target.isOffPlay() && this.m_target.mustGoOffPlay() && this.m_target.canChangePlayStatus()) {
            this.m_target.setUnderChange(true);
            changed = true;
            this.m_target.goOffPlay(this.m_caster);
            if (this.m_target != null) {
                this.m_target.setUnderChange(false);
            }
        }
        if (this.m_caster != null && !this.m_caster.isOffPlay() && this.m_caster.mustGoOffPlay() && this.m_caster.canChangePlayStatus()) {
            this.m_caster.setUnderChange(true);
            changed = true;
            this.m_caster.goOffPlay(this.m_caster);
            if (this.m_caster != null) {
                this.m_caster.setUnderChange(false);
            }
        }
        return changed;
    }
    
    public abstract void computeValue(final RunningEffect p0);
    
    public boolean equalsRunningEffectDespiteTarget(final RunningEffect re) {
        return false;
    }
    
    public byte[] serializeWithoutTarget() {
        return this.build(this.CORE, this.getCasterBinarSerialPart(), this.getAdditionalDatasBinarSerialPart(), this.getEffectContainerBinarSerialPart(), this.getGameSpecificDatasBinarSerialPart());
    }
    
    public byte[] serialize() {
        return this.build(this.CORE, this.getCasterBinarSerialPart(), this.getTargetBinarSerialPart(), this.getAdditionalDatasBinarSerialPart(), this.getEffectContainerBinarSerialPart(), this.getGameSpecificDatasBinarSerialPart());
    }
    
    public byte[] serializeForSave() {
        return this.build(this.CORE, this.getAdditionalDatasBinarSerialPart(), this.getEffectContainerBinarSerialPart(), this.getGameSpecificDatasBinarSerialPart());
    }
    
    @Override
    public BinarSerialPart[] partsEnumeration() {
        return new BinarSerialPart[] { this.CORE, this.getCasterBinarSerialPart(), this.getTargetBinarSerialPart(), this.getAdditionalDatasBinarSerialPart(), this.getEffectContainerBinarSerialPart(), this.getGameSpecificDatasBinarSerialPart() };
    }
    
    public BinarSerialPart getCoreBinarSerialPart() {
        return this.CORE;
    }
    
    public BinarSerialPart getCasterBinarSerialPart() {
        return this.CASTER;
    }
    
    public BinarSerialPart getTargetBinarSerialPart() {
        return this.TARGET;
    }
    
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return BinarSerialPart.EMPTY;
    }
    
    public BinarSerialPart getEffectContainerBinarSerialPart() {
        return BinarSerialPart.EMPTY;
    }
    
    public BinarSerialPart getGameSpecificDatasBinarSerialPart() {
        return BinarSerialPart.EMPTY;
    }
    
    public abstract Effect getDefaultEffect();
    
    public void setBaseUid(final long baseUid) {
        this.m_baseUid = baseUid;
    }
    
    public void setLinkedEvent(final RunningEffectEvent linkedEvent) {
        this.m_linkedEvent = linkedEvent;
    }
    
    protected abstract boolean canBeExecutedOnKO();
    
    static {
        m_logger = Logger.getLogger((Class)RunningEffect.class);
        RunningEffect.uid = 0L;
        RunningEffect.m_UIDGenerator = null;
        RunningEffect.m_useResult = false;
    }
}
