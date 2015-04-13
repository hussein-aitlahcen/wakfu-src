package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;

public class WakfuFightEffectImpl extends WakfuStandardEffect implements WakfuFightEffect
{
    private int m_durationTableTurnBase;
    private float m_durationTableTurnIncrement;
    private boolean m_durationIsInFullTurns;
    private boolean m_endsAtEndOfTurn;
    private int m_executionDelayTableTurnBase;
    private float m_executionDelayTableTurnIncrement;
    private boolean m_isDecursable;
    
    public WakfuFightEffectImpl(final int effectId, final int actionId, final AreaOfEffect area, final int[] listeningTriggerBeforeComputation, final int[] listeningTriggersBeforeExecution, final int[] listeningTriggerForUnapplication, final int[] listeningTriggersAfterExecution, final int[] listeningTriggersAfterAllExecution, final int[] listeningTriggersNotRelatedToExecution, final int[] executionTriggersAdditionnal, final long flags, final TargetValidator targetValidator, final boolean affectedByLocalisation, final int durations, final float durationIncrement, final boolean endsAtEndOfTurn, final boolean isDurationInFullTurns, final int executionDelayDuration, final float executionDelayIncrementDuration, final float[] paramValues, final float proba, final float probainc, final boolean selfTrigger, final boolean triggerSelfTarget, final boolean precalculateTriggerTarget, final boolean storeOnCaster, final int containerMinLevel, final int containerMaxLevel, final SimpleCriterion crit, final short maxExecution, final float maxExecutionIncr, final byte maxTargetsCount, final boolean notifyInChat, final boolean dontTriggerAnything, final boolean isUsableInFight, final boolean isPersonal, final AreaOfEffect emptyCells, final boolean decursable, final boolean notifyInChatForCaster, final boolean notifyInChatForTarget, final boolean notifyInChatWithCasterName) {
        super(effectId, actionId, area, listeningTriggerBeforeComputation, listeningTriggersBeforeExecution, listeningTriggerForUnapplication, listeningTriggersAfterExecution, listeningTriggersAfterAllExecution, listeningTriggersNotRelatedToExecution, executionTriggersAdditionnal, flags, targetValidator, affectedByLocalisation, paramValues, proba, probainc, selfTrigger, triggerSelfTarget, precalculateTriggerTarget, storeOnCaster, containerMinLevel, containerMaxLevel, crit, maxExecution, maxExecutionIncr, maxTargetsCount, notifyInChat, dontTriggerAnything, isUsableInFight, false, isPersonal, emptyCells, notifyInChatForCaster, notifyInChatForTarget, notifyInChatWithCasterName);
        this.m_durationTableTurnBase = durations;
        this.m_durationTableTurnIncrement = durationIncrement;
        this.m_executionDelayTableTurnBase = executionDelayDuration;
        this.m_executionDelayTableTurnIncrement = executionDelayIncrementDuration;
        this.m_durationIsInFullTurns = isDurationInFullTurns;
        this.m_endsAtEndOfTurn = endsAtEndOfTurn;
        this.m_isDecursable = decursable;
    }
    
    private WakfuFightEffectImpl(final int effectId, final int actionId, final AreaOfEffect area, final BitSet listeningTriggerBeforeComputation, final BitSet listeningTriggersBeforeExecution, final BitSet listeningTriggersForUnapplication, final BitSet listeningTriggerAfterExecution, final BitSet listeningTriggersAfterAllExecution, final BitSet listeningTriggersNotRelatedToExecution, final BitSet executionTriggersAdditionnal, final long flags, final TargetValidator targetValidator, final boolean affectedByLocalisation, final int durationTableTurnBase, final float durationTableTurnIncrement, final boolean endsAtEndOfTurn, final boolean isDurationInFullTurns, final int executionDelayTableTurnBase, final float executionDelayTableTurnIncrement, final float[] paramValues, final float proba, final float probainc, final boolean selfTrigger, final boolean triggerTargetIsSelf, final boolean precalculateTriggerTarget, final boolean storeOnSelf, final int containerMinLevel, final int containerMaxLevel, final SimpleCriterion crit, final short max_execution, final float maxExecutionIncr, final boolean dontTriggerAnything, final boolean isUsableInFight, final boolean notifyInChat, final boolean isPersonal, final AreaOfEffect emptyCells, final boolean notifyInChatForCaster, final boolean notifyInChatForTarget, final boolean notifyInChatWithCasterName) {
        super(effectId, actionId, area, listeningTriggerBeforeComputation, listeningTriggersBeforeExecution, listeningTriggersForUnapplication, listeningTriggerAfterExecution, listeningTriggersAfterAllExecution, listeningTriggersNotRelatedToExecution, executionTriggersAdditionnal, flags, targetValidator, affectedByLocalisation, paramValues, proba, probainc, selfTrigger, triggerTargetIsSelf, precalculateTriggerTarget, storeOnSelf, containerMinLevel, containerMaxLevel, crit, max_execution, maxExecutionIncr, dontTriggerAnything, isUsableInFight, false, notifyInChat, isPersonal, emptyCells, notifyInChatForCaster, notifyInChatForTarget, notifyInChatWithCasterName);
        this.m_durationTableTurnBase = durationTableTurnBase;
        this.m_durationTableTurnIncrement = durationTableTurnIncrement;
        this.m_executionDelayTableTurnBase = executionDelayTableTurnBase;
        this.m_executionDelayTableTurnIncrement = executionDelayTableTurnIncrement;
    }
    
    @Override
    public byte getEffectType() {
        return 2;
    }
    
    @Override
    public RelativeFightTimeInterval getDuration(final short spellLevel) {
        final short nTurns = (short)(this.m_durationTableTurnBase + (int)Math.floor(this.m_durationTableTurnIncrement * spellLevel));
        return RelativeFightTimeInterval.turnsFromNow(nTurns).atEndOfTurn(this.m_endsAtEndOfTurn).inFullTurns(this.m_durationIsInFullTurns).withPriority((short)(nTurns + 1));
    }
    
    @Override
    public RelativeFightTimeInterval getDelay(final short spellLevel) {
        final short nTurns = (short)(this.m_executionDelayTableTurnBase + (int)Math.floor(this.m_executionDelayTableTurnIncrement * spellLevel));
        return RelativeFightTimeInterval.turnsFromNow(nTurns).atEndOfTurn(this.m_endsAtEndOfTurn);
    }
    
    @Override
    public WakfuStandardEffect clone() {
        final float[] rawParams = this.getRawParams();
        final float[] params = new float[rawParams.length];
        System.arraycopy(rawParams, 0, params, 0, rawParams.length);
        final WakfuFightEffectImpl clone = new WakfuFightEffectImpl(this.getEffectId(), this.getActionId(), this.getAreaOfEffect(), this.getListeningTriggersBeforeComputation(), this.getListeningTriggersBeforeExecution(), this.getListeningTriggerForUnapplication(), this.getListeningTriggersAfterExecution(), this.getListeningTriggerAfterAllExecutions(), this.getListeningTriggerNotRelatedToExecutions(), this.getExecutionTriggersAdditionnal(), this.getFlags(), this.getTargetValidator(), this.isAffectedByLocalisation(), this.m_durationTableTurnBase, this.m_durationTableTurnIncrement, this.m_endsAtEndOfTurn, this.m_durationIsInFullTurns, this.m_executionDelayTableTurnBase, this.m_executionDelayTableTurnIncrement, params, this.getExecutionProbability(), this.getExecutionProbabilityIncrement(), this.isSelfTrigger(), this.triggerTargetIsSelf(), this.preCalculateTriggerTarget(), this.mustStoreOnCaster(), this.getContainerMinLevel(), this.getContainerMaxLevel(), this.getConditions(), this.getMaximumExecutions(), this.getMaxExecutionIncr(), this.dontTriggerAnything(), this.isAnUsableEffect(), this.notifyInChat(), this.isPersonal(), this.getEmptyCellNeededAreaOfEffect(), this.notifyInChatForCaster(), this.notifyInChatForTarget(), this.notifyInChatWithCasterName());
        clone.m_triggerListenerType = this.m_triggerListenerType;
        return clone;
    }
    
    @Override
    public boolean isDecursable() {
        return this.m_isDecursable;
    }
}
