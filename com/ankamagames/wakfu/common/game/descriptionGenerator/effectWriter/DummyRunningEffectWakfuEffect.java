package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.targetfinder.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class DummyRunningEffectWakfuEffect implements WakfuEffect
{
    private RunningEffect<WakfuEffect, WakfuEffectContainer> m_runningEffect;
    
    public DummyRunningEffectWakfuEffect(final RunningEffect<WakfuEffect, WakfuEffectContainer> runningEffect) {
        super();
        this.m_runningEffect = runningEffect;
    }
    
    @Override
    public byte getMaxTargetsCount() {
        return this.m_runningEffect.getGenericEffect().getMaxTargetsCount();
    }
    
    @Override
    public float[] getRawParams() {
        return this.m_runningEffect.getGenericEffect().getRawParams();
    }
    
    @Override
    public int getParamsCount() {
        return this.m_runningEffect.getGenericEffect().getParamsCount();
    }
    
    @Override
    public int getParam(final int paramNum, final short containerLevel, final RoundingMethod roundingMethod) {
        if (this.m_runningEffect instanceof CharacBuff && paramNum == 0) {
            final int firstParam = this.m_runningEffect.getGenericEffect().getParam(0, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            if (firstParam == -1 || firstParam == -2) {
                return this.m_runningEffect.getValue();
            }
        }
        else if (this.m_runningEffect instanceof CharacGain && this.getParamsCount() == 4 && paramNum == 0) {
            return this.m_runningEffect.getValue();
        }
        return this.m_runningEffect.getGenericEffect().getParam(paramNum, containerLevel, roundingMethod);
    }
    
    @Override
    public float getParam(final int paramNum, final short containerLevel) {
        if (this.m_runningEffect instanceof CharacBuff && paramNum == 0) {
            final float firstParam = this.m_runningEffect.getGenericEffect().getParam(0, containerLevel);
            if (firstParam == -1.0f || firstParam == -2.0f) {
                return this.m_runningEffect.getValue();
            }
        }
        else if (this.m_runningEffect instanceof CharacGain && this.getParamsCount() == 4 && paramNum == 0) {
            return this.m_runningEffect.getValue();
        }
        return this.m_runningEffect.getGenericEffect().getParam(paramNum, containerLevel);
    }
    
    @Override
    public float getParam(final int paramNum) {
        if (this.m_runningEffect instanceof CharacBuff && paramNum == 0) {
            final float firstParam = this.m_runningEffect.getGenericEffect().getParam(0);
            if (firstParam == -1.0f || firstParam == -2.0f) {
                return this.m_runningEffect.getValue();
            }
        }
        else if (this.m_runningEffect instanceof CharacGain && this.getParamsCount() == 4 && paramNum == 0) {
            return this.m_runningEffect.getValue();
        }
        return this.m_runningEffect.getGenericEffect().getParam(paramNum);
    }
    
    @Override
    public boolean notifyInChat() {
        return this.m_runningEffect.getGenericEffect().notifyInChat();
    }
    
    @Override
    public boolean notifyInChatForCaster() {
        return this.m_runningEffect.getGenericEffect().notifyInChatForCaster();
    }
    
    @Override
    public boolean notifyInChatForTarget() {
        return this.m_runningEffect.getGenericEffect().notifyInChatForTarget();
    }
    
    @Override
    public boolean notifyInChatWithCasterName() {
        return this.m_runningEffect.getGenericEffect().notifyInChatWithCasterName();
    }
    
    @Override
    public WakfuEffect clone() {
        return this.m_runningEffect.getGenericEffect().clone();
    }
    
    @Override
    public boolean isAnUsableEffect() {
        return this.m_runningEffect.getGenericEffect().isAnUsableEffect();
    }
    
    @Override
    public boolean isUsableInFight() {
        return this.m_runningEffect.getGenericEffect().isUsableInFight();
    }
    
    @Override
    public boolean isUsableInWorld() {
        return this.m_runningEffect.getGenericEffect().isUsableInWorld();
    }
    
    @Override
    public int getContainerMinLevel() {
        return this.m_runningEffect.getGenericEffect().getContainerMinLevel();
    }
    
    @Override
    public int getContainerMaxLevel() {
        return this.m_runningEffect.getGenericEffect().getContainerMaxLevel();
    }
    
    @Override
    public SimpleCriterion getConditions() {
        return this.m_runningEffect.getGenericEffect().getConditions();
    }
    
    @Override
    public float getExecutionProbability(final short containerLevel) {
        return this.m_runningEffect.getGenericEffect().getExecutionProbability(containerLevel);
    }
    
    @Override
    public float getExecutionProbability() {
        return this.m_runningEffect.getGenericEffect().getExecutionProbability();
    }
    
    @Override
    public void addActionToExecuteOnApplication(final HMIAction action) {
    }
    
    @Override
    public void addActionToExecuteOnUnApplication(final HMIAction action) {
    }
    
    @Override
    public void addActionToExecuteOnExecution(final HMIAction action) {
    }
    
    @Override
    public void addActionToStopOnUnApplication(final HMIAction action) {
    }
    
    @Override
    public void addActionToStopOnExecution(final HMIAction action) {
    }
    
    @Override
    public Iterator<HMIAction> getActionsToExecuteOnApplication() {
        return null;
    }
    
    @Override
    public Iterator<HMIAction> getActionsToExecuteOnExecution() {
        return null;
    }
    
    @Override
    public AreaOfEffect getEmptyCellNeededAreaOfEffect() {
        return null;
    }
    
    @Override
    public float getMaxExecutionIncr() {
        return 0.0f;
    }
    
    @Override
    public int getScriptFileId() {
        return 0;
    }
    
    @Override
    public void setScriptFileId(final int scriptFileId) {
    }
    
    @Override
    public void setIsDurationInCasterTurn(final boolean isDurationInCasterTurn) {
    }
    
    @Override
    public List<HMIAction> getActionsOrder() {
        return null;
    }
    
    @Override
    public boolean isActionToExecuteOnUnapplication(final HMIAction action) {
        return false;
    }
    
    @Override
    public boolean isActionToStopOnUnapplication(final HMIAction action) {
        return false;
    }
    
    @Override
    public boolean isActionToExecuteOnApplication(final HMIAction action) {
        return false;
    }
    
    @Override
    public boolean isActionToExecuteOnExecution(final HMIAction action) {
        return false;
    }
    
    @Override
    public boolean isActionToStopOnExecution(final HMIAction action) {
        return false;
    }
    
    @Override
    public void setTriggerListenerType(final TriggerListenerType triggerListenerType) {
    }
    
    @Override
    public void setTriggerTargetType(final TriggerTargetType triggerTargetType) {
    }
    
    @Override
    public void setTriggerCasterType(final TriggerCasterType triggerCasterType) {
    }
    
    @Override
    public void setDisplayInSpellDescription(final boolean displayInSpellDescription) {
    }
    
    @Override
    public boolean isDisplayInSpellDescription() {
        return this.m_runningEffect.getGenericEffect().isDisplayInSpellDescription();
    }
    
    @Override
    public boolean isDisplayInStateBar() {
        return this.m_runningEffect.getGenericEffect().isDisplayInStateBar();
    }
    
    @Override
    public void setDisplayInStateBar(final boolean displayInStateOverview) {
    }
    
    @Override
    public boolean isSelfTrigger() {
        return this.m_runningEffect.getGenericEffect().isSelfTrigger();
    }
    
    @Override
    public boolean isGlobalTriggerListener() {
        return this.m_runningEffect.getGenericEffect().isGlobalTriggerListener();
    }
    
    @Override
    public boolean preCalculateTriggerTarget() {
        return false;
    }
    
    @Override
    public boolean triggerTargetIsSelf() {
        return false;
    }
    
    @Override
    public boolean mustStoreOnCaster() {
        return false;
    }
    
    @Override
    public boolean dontTriggerAnything() {
        return false;
    }
    
    @Override
    public boolean alwaysTrigger() {
        return false;
    }
    
    @Override
    public byte getEffectType() {
        return this.m_runningEffect.getGenericEffect().getEffectType();
    }
    
    @Override
    public int getEffectId() {
        return this.m_runningEffect.getGenericEffect().getEffectId();
    }
    
    @Override
    public int getActionId() {
        return this.m_runningEffect.getGenericEffect().getActionId();
    }
    
    @Override
    public boolean checkFlags(final long flagsToCheck) {
        return this.m_runningEffect.getGenericEffect().checkFlags(flagsToCheck);
    }
    
    @Override
    public TargetValidator<EffectUser> getTargetValidator() {
        return this.m_runningEffect.getGenericEffect().getTargetValidator();
    }
    
    @Override
    public BitSet getListeningTriggersBeforeComputation() {
        return null;
    }
    
    @Override
    public BitSet getListeningTriggersBeforeExecution() {
        return null;
    }
    
    @Override
    public BitSet getListeningTriggerForUnapplication() {
        return null;
    }
    
    @Override
    public BitSet getListeningTriggersAfterExecution() {
        return null;
    }
    
    @Override
    public BitSet getListeningTriggerAfterAllExecutions() {
        return null;
    }
    
    @Override
    public BitSet getListeningTriggerNotRelatedToExecutions() {
        return null;
    }
    
    @Override
    public boolean mustBeTriggered() {
        return false;
    }
    
    @Override
    public BitSet getExecutionTriggersAdditionnal() {
        return null;
    }
    
    @Override
    public EffectExecutionResult execute(final EffectContainer cont, final EffectUser launcher, final EffectContext context, final Constants<? extends StaticRunningEffect> constants, final int targetCellx, final int targetCelly, final short targetCellz, final EffectUser target, final EffectExecutionParameters params, final boolean withResult) {
        return null;
    }
    
    @Override
    public AreaOfEffect getAreaOfEffect() {
        return this.m_runningEffect.getGenericEffect().getAreaOfEffect();
    }
    
    @Override
    public boolean isAffectedByLocalisation() {
        return false;
    }
    
    @Override
    public long getFlags() {
        return this.m_runningEffect.getGenericEffect().getFlags();
    }
    
    @Override
    public short getMaximumExecutions() {
        return this.m_runningEffect.getGenericEffect().getMaximumExecutions();
    }
    
    @Override
    public void setMaximumExecutions(final short maximum_executions) {
    }
    
    @Override
    public boolean isDurationInCasterTurn() {
        return this.m_runningEffect.getGenericEffect().isDurationInCasterTurn();
    }
    
    @Override
    public boolean isDurationInTargetTurn() {
        return this.m_runningEffect.getGenericEffect().isDurationInTargetTurn();
    }
    
    @Override
    public TriggerTargetType getTriggerTargetType() {
        return this.m_runningEffect.getGenericEffect().getTriggerTargetType();
    }
    
    @Override
    public TriggerCasterType getTriggerCasterType() {
        return this.m_runningEffect.getGenericEffect().getTriggerCasterType();
    }
    
    @Override
    public boolean isShouldRecomputeTarget() {
        return this.m_runningEffect.getGenericEffect().isShouldRecomputeTarget();
    }
    
    @Override
    public boolean recomputeAreaOfEffectDisplay() {
        return this.m_runningEffect.getGenericEffect().recomputeAreaOfEffectDisplay();
    }
    
    @Override
    public boolean isCriterionGrayable() {
        return this.m_runningEffect.getGenericEffect().isCriterionGrayable();
    }
    
    @Override
    public boolean doNotNotify() {
        return this.m_runningEffect.getGenericEffect().doNotNotify();
    }
    
    @Override
    public boolean hasProperty(final RunningEffectPropertyType property) {
        return false;
    }
}
