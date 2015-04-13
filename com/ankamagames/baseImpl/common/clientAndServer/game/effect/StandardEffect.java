package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.targetfinder.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public abstract class StandardEffect implements Effect
{
    protected static final Logger m_logger;
    private final int m_effectId;
    private final int m_actionId;
    private final AreaOfEffect m_area;
    private final TargetValidator<EffectUser> m_targetValidator;
    private final long m_flags;
    private short m_maximum_executions;
    private boolean m_dontTriggerAnything;
    protected boolean m_display;
    private BitSet m_listeningTriggersBeforeComputation;
    private BitSet m_listeningTriggersBeforeExecution;
    private BitSet m_listeningTriggersAfterExecution;
    private BitSet m_listeningTriggersAfterAllExecution;
    private BitSet m_listeningTriggersNotRelatedToExecution;
    private BitSet m_listeningTriggerForUnapplication;
    private BitSet m_executionTriggersAdditionnal;
    protected TriggerListenerType m_triggerListenerType;
    private boolean m_precalculateTriggerTarget;
    private boolean m_targetSelfTrigger;
    private boolean m_storeOnCaster;
    private final boolean m_affectedByLocalisation;
    private boolean m_durationInCasterTurn;
    
    public StandardEffect(final int effectId, final int actionId, final AreaOfEffect area, final int[] listeningTriggersBeforeComputation, final int[] listeningTriggersBeforeExecution, final int[] listeningTriggerForUnapplication, final int[] listeningTriggersAfterExecution, final int[] listeningTriggersAfterAllExecution, final int[] listeningTriggersNotRelatedToExecution, final int[] executionTriggersAdditionnal, final long flags, final TargetValidator<EffectUser> targetValidator, final boolean affectedByLocalisation, final boolean selfTrigger, final short maxExecution, final boolean triggerTargetIsSelf, final boolean precalculateTriggerTarget, final boolean storeOnCaster, final boolean dontTriggerAnything) {
        super();
        this.m_display = true;
        this.m_triggerListenerType = TriggerListenerType.TARGET_TRIGGER_LISTENER;
        this.m_precalculateTriggerTarget = false;
        this.m_targetSelfTrigger = false;
        this.m_storeOnCaster = false;
        this.m_durationInCasterTurn = false;
        this.m_effectId = effectId;
        this.m_actionId = actionId;
        this.m_area = area;
        this.m_flags = flags;
        this.m_targetValidator = targetValidator;
        if (listeningTriggersBeforeComputation.length > 0) {
            this.setBits(listeningTriggersBeforeComputation, this.m_listeningTriggersBeforeComputation = new BitSet());
        }
        if (listeningTriggersBeforeExecution.length > 0) {
            this.setBits(listeningTriggersBeforeExecution, this.m_listeningTriggersBeforeExecution = new BitSet());
        }
        if (listeningTriggerForUnapplication.length > 0) {
            this.setBits(listeningTriggerForUnapplication, this.m_listeningTriggerForUnapplication = new BitSet());
        }
        if (listeningTriggersAfterExecution.length > 0) {
            this.setBits(listeningTriggersAfterExecution, this.m_listeningTriggersAfterExecution = new BitSet());
        }
        if (listeningTriggersAfterAllExecution.length > 0) {
            this.setBits(listeningTriggersAfterAllExecution, this.m_listeningTriggersAfterAllExecution = new BitSet());
        }
        if (listeningTriggersNotRelatedToExecution.length > 0) {
            this.setBits(listeningTriggersNotRelatedToExecution, this.m_listeningTriggersNotRelatedToExecution = new BitSet());
        }
        if (executionTriggersAdditionnal.length > 0) {
            this.setBits(executionTriggersAdditionnal, this.m_executionTriggersAdditionnal = new BitSet());
        }
        this.m_affectedByLocalisation = affectedByLocalisation;
        if (selfTrigger) {
            this.m_triggerListenerType = TriggerListenerType.CASTER_TRIGGER_LISTENER;
        }
        this.m_targetSelfTrigger = triggerTargetIsSelf;
        this.m_storeOnCaster = storeOnCaster;
        this.m_precalculateTriggerTarget = precalculateTriggerTarget;
        this.m_maximum_executions = maxExecution;
        this.m_dontTriggerAnything = dontTriggerAnything;
    }
    
    private void setBits(final int[] src, final BitSet dest) {
        if (src == null || dest == null) {
            return;
        }
        for (int index = 0; index < src.length; ++index) {
            final int bitIndex;
            if ((bitIndex = src[index]) >= 0) {
                dest.set(bitIndex);
            }
        }
    }
    
    @Override
    public boolean isSelfTrigger() {
        return this.m_triggerListenerType == TriggerListenerType.CASTER_TRIGGER_LISTENER;
    }
    
    @Override
    public boolean isGlobalTriggerListener() {
        return this.m_triggerListenerType == TriggerListenerType.GLOBAL_TRIGGER_LISTENER;
    }
    
    @Override
    public boolean preCalculateTriggerTarget() {
        return this.m_precalculateTriggerTarget;
    }
    
    @Override
    public boolean triggerTargetIsSelf() {
        return this.m_targetSelfTrigger;
    }
    
    @Override
    public TriggerTargetType getTriggerTargetType() {
        return null;
    }
    
    @Override
    public TriggerCasterType getTriggerCasterType() {
        return null;
    }
    
    @Override
    public boolean mustStoreOnCaster() {
        return this.m_storeOnCaster;
    }
    
    @Override
    public boolean dontTriggerAnything() {
        return this.m_dontTriggerAnything;
    }
    
    @Override
    public boolean alwaysTrigger() {
        return false;
    }
    
    protected StandardEffect(final int effectId, final int actionId, final AreaOfEffect area, final BitSet listeningTriggersBeforeComputation, final BitSet listeningTriggersBeforeExecution, final BitSet listeningTriggerForUnapplication, final BitSet listeningTriggersAfterExecution, final BitSet listeningTriggersAfterAllExecution, final BitSet listeningTriggersNotRelatedToExecution, final BitSet executionTriggersAdditionnal, final long flags, final TargetValidator<EffectUser> targetValidator, final boolean affectedByLocalisation, final boolean selfTrigger, final short maxExecutions, final boolean triggerTargetIsSelf, final boolean precalculateTriggerTarget, final boolean storeOnCaster, final boolean dontTriggerAnything) {
        super();
        this.m_display = true;
        this.m_triggerListenerType = TriggerListenerType.TARGET_TRIGGER_LISTENER;
        this.m_precalculateTriggerTarget = false;
        this.m_targetSelfTrigger = false;
        this.m_storeOnCaster = false;
        this.m_durationInCasterTurn = false;
        this.m_effectId = effectId;
        this.m_actionId = actionId;
        this.m_area = area;
        this.m_flags = flags;
        this.m_targetValidator = targetValidator;
        if (listeningTriggersBeforeComputation != null) {
            for (int j = 0; j < listeningTriggersBeforeComputation.size(); ++j) {
                if (j > 0 && listeningTriggersBeforeComputation.get(j)) {
                    (this.m_listeningTriggersBeforeComputation = new BitSet()).set(j);
                }
            }
        }
        if (listeningTriggersBeforeExecution != null) {
            for (int j = 0; j < listeningTriggersBeforeExecution.size(); ++j) {
                if (j > 0 && listeningTriggersBeforeExecution.get(j)) {
                    (this.m_listeningTriggersBeforeExecution = new BitSet()).set(j);
                }
            }
        }
        if (listeningTriggerForUnapplication != null) {
            for (int j = 0; j < listeningTriggerForUnapplication.size(); ++j) {
                if (j > 0 && listeningTriggerForUnapplication.get(j)) {
                    (this.m_listeningTriggerForUnapplication = new BitSet()).set(j);
                }
            }
        }
        if (listeningTriggersAfterExecution != null) {
            for (int j = 0; j < listeningTriggersAfterExecution.size(); ++j) {
                if (j > 0 && listeningTriggersAfterExecution.get(j)) {
                    (this.m_listeningTriggersAfterExecution = new BitSet()).set(j);
                }
            }
        }
        if (listeningTriggersAfterAllExecution != null) {
            for (int j = 0; j < listeningTriggersAfterAllExecution.size(); ++j) {
                if (j > 0 && listeningTriggersAfterAllExecution.get(j)) {
                    (this.m_listeningTriggersAfterAllExecution = new BitSet()).set(j);
                }
            }
        }
        if (listeningTriggersNotRelatedToExecution != null) {
            for (int j = 0; j < listeningTriggersNotRelatedToExecution.size(); ++j) {
                if (j > 0 && listeningTriggersNotRelatedToExecution.get(j)) {
                    (this.m_listeningTriggersNotRelatedToExecution = new BitSet()).set(j);
                }
            }
        }
        if (executionTriggersAdditionnal != null) {
            for (int j = 0; j < executionTriggersAdditionnal.size(); ++j) {
                if (j > 0 && executionTriggersAdditionnal.get(j)) {
                    (this.m_executionTriggersAdditionnal = new BitSet()).set(j);
                }
            }
        }
        this.m_affectedByLocalisation = affectedByLocalisation;
        if (selfTrigger) {
            this.m_triggerListenerType = TriggerListenerType.CASTER_TRIGGER_LISTENER;
        }
        this.m_targetSelfTrigger = triggerTargetIsSelf;
        this.m_precalculateTriggerTarget = precalculateTriggerTarget;
        this.m_storeOnCaster = storeOnCaster;
        this.m_maximum_executions = maxExecutions;
        this.m_dontTriggerAnything = dontTriggerAnything;
    }
    
    @Override
    public int getEffectId() {
        return this.m_effectId;
    }
    
    @Override
    public int getActionId() {
        return this.m_actionId;
    }
    
    @Override
    public boolean checkFlags(final long flagsToCheck) {
        return (this.m_flags & flagsToCheck) == flagsToCheck;
    }
    
    @Override
    public TargetValidator<EffectUser> getTargetValidator() {
        return this.m_targetValidator;
    }
    
    @Override
    public BitSet getListeningTriggersBeforeComputation() {
        return this.m_listeningTriggersBeforeComputation;
    }
    
    @Override
    public BitSet getListeningTriggersBeforeExecution() {
        return this.m_listeningTriggersBeforeExecution;
    }
    
    @Override
    public BitSet getListeningTriggerForUnapplication() {
        return this.m_listeningTriggerForUnapplication;
    }
    
    @Override
    public BitSet getListeningTriggersAfterExecution() {
        return this.m_listeningTriggersAfterExecution;
    }
    
    @Override
    public BitSet getListeningTriggerAfterAllExecutions() {
        return this.m_listeningTriggersAfterAllExecution;
    }
    
    @Override
    public BitSet getListeningTriggerNotRelatedToExecutions() {
        return this.m_listeningTriggersNotRelatedToExecution;
    }
    
    @Override
    public boolean mustBeTriggered() {
        return (this.getListeningTriggersBeforeExecution() != null && this.getListeningTriggersBeforeExecution().length() > 0) || (this.getListeningTriggersBeforeComputation() != null && this.getListeningTriggersBeforeComputation().length() > 0) || (this.getListeningTriggersAfterExecution() != null && this.getListeningTriggersAfterExecution().length() > 0) || (this.getListeningTriggerAfterAllExecutions() != null && this.getListeningTriggerAfterAllExecutions().length() > 0) || (this.getListeningTriggerNotRelatedToExecutions() != null && this.getListeningTriggerNotRelatedToExecutions().length() > 0);
    }
    
    @Override
    public BitSet getExecutionTriggersAdditionnal() {
        return this.m_executionTriggersAdditionnal;
    }
    
    @Override
    public EffectExecutionResult execute(final EffectContainer cont, final EffectUser launcher, final EffectContext context, final Constants<? extends StaticRunningEffect> constants, final int targetCellx, final int targetCelly, final short targetCellz, final EffectUser target, final EffectExecutionParameters params, final boolean withResult) {
        final StaticRunningEffect<Effect, EffectContainer> sre = (StaticRunningEffect<Effect, EffectContainer>)constants.getObjectFromId(this.getActionId());
        final EffectExecutionResult result = sre.run(this, cont, context, launcher, targetCellx, targetCelly, targetCellz, target, params);
        if (!withResult) {
            result.clear();
            return null;
        }
        return result;
    }
    
    @Override
    public AreaOfEffect getAreaOfEffect() {
        return this.m_area;
    }
    
    @Override
    public boolean isAffectedByLocalisation() {
        return this.m_affectedByLocalisation;
    }
    
    @Override
    public long getFlags() {
        return this.m_flags;
    }
    
    @Override
    public short getMaximumExecutions() {
        return this.m_maximum_executions;
    }
    
    public boolean getDisplay() {
        return this.m_display;
    }
    
    @Override
    public void setMaximumExecutions(final short maximum_executions) {
        this.m_maximum_executions = maximum_executions;
    }
    
    protected void setDurationInCasterTurn(final boolean durationInCasterTurn) {
        this.m_durationInCasterTurn = durationInCasterTurn;
    }
    
    @Override
    public boolean isDurationInCasterTurn() {
        return this.m_durationInCasterTurn;
    }
    
    @Override
    public boolean isDurationInTargetTurn() {
        return !this.m_durationInCasterTurn;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Effect.class);
    }
}
