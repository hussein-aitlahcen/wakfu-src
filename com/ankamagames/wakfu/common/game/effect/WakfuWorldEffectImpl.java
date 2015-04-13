package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

public class WakfuWorldEffectImpl extends WakfuStandardEffect implements WakfuWorldEffect
{
    private final int m_durationInMs;
    private final float m_durationInMsIncrement;
    
    public WakfuWorldEffectImpl(final int effectId, final int actionId, final AreaOfEffect area, final int[] listeningTriggerBeforeComputation, final int[] listeningTriggersBeforeExecution, final int[] listeningTriggerForUnapplication, final int[] listeningTriggersAfterExecution, final int[] listeningTriggersAfterAllExecution, final int[] listeningTriggersNotRelatedToExecution, final int[] executionTriggersAdditionnal, final long flags, final TargetValidator targetValidator, final boolean affectedByLocalisation, final int durations, final float durationIncrement, final float[] paramValues, final float proba, final float probainc, final boolean selfTrigger, final boolean triggerSelfTarget, final boolean precalculateTriggerTarget, final boolean storeOnSelf, final int containerMinLevel, final int containerMaxLevel, final SimpleCriterion crit, final short max_execution, final float maxExecutionIncr, final byte maxTargetsCount, final boolean notifyInChat, final boolean dontTriggerAnything, final boolean isUsableInWorld, final boolean isPersonal, final AreaOfEffect emptyCells, final boolean notifyInChatForCaster, final boolean notifyInChatForTarget, final boolean notifyInChatWithCasterName) {
        super(effectId, actionId, area, listeningTriggerBeforeComputation, listeningTriggersBeforeExecution, listeningTriggerForUnapplication, listeningTriggersAfterExecution, listeningTriggersAfterAllExecution, listeningTriggersNotRelatedToExecution, executionTriggersAdditionnal, flags, targetValidator, affectedByLocalisation, paramValues, proba, probainc, selfTrigger, triggerSelfTarget, precalculateTriggerTarget, storeOnSelf, containerMinLevel, containerMaxLevel, crit, max_execution, maxExecutionIncr, maxTargetsCount, notifyInChat, dontTriggerAnything, false, isUsableInWorld, isPersonal, emptyCells, notifyInChatForCaster, notifyInChatForTarget, notifyInChatWithCasterName);
        this.m_durationInMs = durations;
        this.m_durationInMsIncrement = durationIncrement;
    }
    
    private WakfuWorldEffectImpl(final int effectId, final int actionId, final AreaOfEffect area, final BitSet listeningTriggerBeforeComputation, final BitSet listeningTriggersBeforeExecution, final BitSet listeningTriggersForUnapplication, final BitSet listeningTriggerAfterExecution, final BitSet listeningTriggersAfterAllExecution, final BitSet listeningTriggersNotRelatedToExecution, final BitSet executionTriggersAdditionnal, final long flags, final TargetValidator targetValidator, final boolean affectedByLocalisation, final int duration, final float durationIncr, final float[] paramValues, final float proba, final float probainc, final boolean selfTrigger, final boolean triggerTargetIsSelf, final boolean precalculateTriggerTarget, final boolean storeOnSelf, final int containerMinLevel, final int containerMaxLevel, final SimpleCriterion crit, final short max_execution, final float maxExecutionIncr, final boolean dontTriggerAnything, final boolean isUsableInWorld, final boolean notifyInChat, final boolean isPersonal, final AreaOfEffect emptyCells, final boolean notifyInChatForCaster, final boolean notifyInChatForTarget, final boolean notifyInChatWithCasterName) {
        super(effectId, actionId, area, listeningTriggerBeforeComputation, listeningTriggersBeforeExecution, listeningTriggersForUnapplication, listeningTriggerAfterExecution, listeningTriggersAfterAllExecution, listeningTriggersNotRelatedToExecution, executionTriggersAdditionnal, flags, targetValidator, affectedByLocalisation, paramValues, proba, probainc, selfTrigger, triggerTargetIsSelf, precalculateTriggerTarget, storeOnSelf, containerMinLevel, containerMaxLevel, crit, max_execution, max_execution, dontTriggerAnything, false, isUsableInWorld, notifyInChat, isPersonal, emptyCells, notifyInChatForCaster, notifyInChatForTarget, notifyInChatWithCasterName);
        this.m_durationInMs = duration;
        this.m_durationInMsIncrement = durationIncr;
    }
    
    @Override
    public byte getEffectType() {
        return 1;
    }
    
    @Override
    public int getDurationInMs(final short spellLevel) {
        return this.m_durationInMs + (int)Math.floor(this.m_durationInMsIncrement * spellLevel);
    }
    
    @Override
    public WakfuWorldEffectImpl clone() {
        final float[] rawParams = this.getRawParams();
        final float[] params = new float[rawParams.length];
        System.arraycopy(rawParams, 0, params, 0, rawParams.length);
        final WakfuWorldEffectImpl clone = new WakfuWorldEffectImpl(this.getEffectId(), this.getActionId(), this.getAreaOfEffect(), this.getListeningTriggersBeforeComputation(), this.getListeningTriggersBeforeExecution(), this.getListeningTriggerForUnapplication(), this.getListeningTriggersAfterExecution(), this.getListeningTriggerAfterAllExecutions(), this.getListeningTriggerNotRelatedToExecutions(), this.getExecutionTriggersAdditionnal(), this.getFlags(), this.getTargetValidator(), this.isAffectedByLocalisation(), this.m_durationInMs, this.m_durationInMsIncrement, params, this.getExecutionProbability(), this.getExecutionProbabilityIncrement(), this.isSelfTrigger(), this.triggerTargetIsSelf(), this.preCalculateTriggerTarget(), this.mustStoreOnCaster(), this.getContainerMinLevel(), this.getContainerMaxLevel(), this.getConditions(), this.getMaximumExecutions(), this.getMaxExecutionIncr(), this.dontTriggerAnything(), this.isAnUsableEffect(), this.notifyInChat(), this.isPersonal(), this.getEmptyCellNeededAreaOfEffect(), this.notifyInChatForCaster(), this.notifyInChatForTarget(), this.notifyInChatWithCasterName());
        clone.m_triggerListenerType = this.m_triggerListenerType;
        return clone;
    }
}
