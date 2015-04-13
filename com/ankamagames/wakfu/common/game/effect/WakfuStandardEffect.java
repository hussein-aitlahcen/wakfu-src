package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.dataProvider.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public class WakfuStandardEffect extends StandardEffect implements WakfuEffect
{
    private static final double INC_PRECISION_TO_ROUND = 1.0E-5;
    private final float m_executionProbability;
    private final float m_executionProbabilityIncrement;
    private final float[] m_paramValues;
    private final int m_paramsCount;
    private final boolean m_notifyInChat;
    private final boolean m_notifyInChatForCaster;
    private final boolean m_notifyInChatForTarget;
    private final boolean m_notifyInChatWithCasterName;
    private boolean m_recomputeAreaOfEffectDisplay;
    private final int m_containerMinLevel;
    private final int m_containerMaxLevel;
    private final SimpleCriterion m_conditions;
    private boolean m_criterionGrayable;
    private final byte m_maxTargetsCount;
    private List<HMIAction> m_actionsToExecuteOnApplication;
    private List<HMIAction> m_actionsToExecuteOnUnApplication;
    private List<HMIAction> m_actionsToExecuteOnExecution;
    private List<HMIAction> m_actionsToStopOnUnApplication;
    private List<HMIAction> m_actionsToStopOnExecution;
    private List<HMIAction> m_actionsOrder;
    private final boolean m_isUsableInFight;
    private final boolean m_isUsableInWorld;
    private final boolean m_isPersonal;
    private int m_scriptFileId;
    private boolean m_displayInSpellDescription;
    private boolean m_displayInStateBar;
    private TriggerTargetType m_triggerTargetType;
    private TriggerCasterType m_triggerCasterType;
    private final AreaOfEffect m_emptyCellNeeded;
    protected static final int[] EMPTY_INT_ARRAY;
    protected static final float[] EMPTY_FLOAT_ARRAY;
    protected static final int[] INITIALIZED_TO_0_INT_ARRAY;
    private final float m_maxExecutionIncr;
    private boolean m_shouldRecomputeTarget;
    private boolean m_doNotNotify;
    private Set<RunningEffectPropertyType> m_properties;
    
    public WakfuStandardEffect(final int effectId, final int actionId, final AreaOfEffect area, final int[] listeningTriggerBeforeComputation, final int[] listeningTriggersBeforeExecution, final int[] listeningTriggerForUnapplication, final int[] listeningTriggersAfterExecution, final int[] listeningTriggersAfterAllExecution, final int[] listeningTriggersNotRelatedToExecution, final int[] executionTriggersAdditionnal, final long flags, final TargetValidator<EffectUser> targetValidator, final boolean affectedByLocalisation, final float[] paramValues, final float proba, final float probainc, final boolean selfTrigger, final boolean triggerSelfTarget, final boolean precalculateTriggerTarget, final boolean storeOnCaster, final int containerMinLevel, final int containerMaxLevel, final SimpleCriterion crit, final short maxExecution, final float maxExecutionIncr, final byte maxTargetsCount, final boolean notifyInChat, final boolean dontTriggerAnything, final boolean isUsableInFight, final boolean isUsableInWorld, final boolean isPersonal, final AreaOfEffect emptyCell, final boolean notifyInChatForCaster, final boolean notifyInChatForTarget, final boolean notifyInChatWithCasterName) {
        super(effectId, actionId, area, listeningTriggerBeforeComputation, listeningTriggersBeforeExecution, listeningTriggerForUnapplication, listeningTriggersAfterExecution, listeningTriggersAfterAllExecution, listeningTriggersNotRelatedToExecution, executionTriggersAdditionnal, flags, targetValidator, affectedByLocalisation, selfTrigger, maxExecution, triggerSelfTarget, precalculateTriggerTarget, storeOnCaster, dontTriggerAnything);
        this.m_displayInSpellDescription = true;
        this.m_displayInStateBar = true;
        this.m_shouldRecomputeTarget = false;
        this.m_emptyCellNeeded = emptyCell;
        this.m_executionProbability = proba;
        this.m_executionProbabilityIncrement = probainc;
        this.m_paramValues = paramValues;
        this.m_paramsCount = paramValues.length / 2;
        this.m_maxTargetsCount = maxTargetsCount;
        this.m_containerMinLevel = containerMinLevel;
        this.m_containerMaxLevel = containerMaxLevel;
        this.m_conditions = crit;
        this.m_notifyInChat = notifyInChat;
        this.m_notifyInChatForCaster = notifyInChatForCaster;
        this.m_notifyInChatForTarget = notifyInChatForTarget;
        this.m_notifyInChatWithCasterName = notifyInChatWithCasterName;
        this.m_isUsableInFight = isUsableInFight;
        this.m_isUsableInWorld = isUsableInWorld;
        this.m_maxExecutionIncr = maxExecutionIncr;
        this.m_isPersonal = isPersonal;
        if (actionId != -1 && actionId != -2) {
            final WakfuRunningEffect runningEffect = RunningEffectConstants.getInstance().getObjectFromId(actionId);
            if (runningEffect == null) {
                WakfuStandardEffect.m_logger.error((Object)("Impossible de cr\u00e9er l'effet " + effectId + " : action " + actionId + " inconnue"));
            }
            else if (!runningEffect.getParametersListSet().mapValueCount(paramValues.length)) {
                WakfuStandardEffect.m_logger.error((Object)("Impossible de cr\u00e9er l'effet " + effectId + " : nombre de param\u00e8tres incorrect : " + paramValues.length));
            }
        }
    }
    
    protected WakfuStandardEffect(final int effectId, final int actionId, final AreaOfEffect area, final BitSet listeningTriggerBeforeComputation, final BitSet listeningTriggersBeforeExecution, final BitSet listeningTriggersForUnapplication, final BitSet listeningTriggerAfterExecution, final BitSet listeningTriggersAfterAllExecution, final BitSet listeningTriggersNotRelatedToExecution, final BitSet executionTriggersAdditionnal, final long flags, final TargetValidator<EffectUser> targetValidator, final boolean affectedByLocalisation, final float[] paramValues, final float proba, final float probainc, final boolean selfTrigger, final boolean triggerTargetIsSelf, final boolean precalculateTriggerTarget, final boolean storeOnSelf, final int containerMinLevel, final int containerMaxLevel, final SimpleCriterion crit, final short max_execution, final float maxExecutionIncr, final boolean dontTriggerAnything, final boolean isUsableInFight, final boolean isUsableInWorld, final boolean notifyInChat, final boolean isPersonal, final AreaOfEffect emptyCells, final boolean notifyInChatForCaster, final boolean notifyInChatForTarget, final boolean notifyInChatWithCasterName) {
        super(effectId, actionId, area, listeningTriggerBeforeComputation, listeningTriggersBeforeExecution, listeningTriggersForUnapplication, listeningTriggerAfterExecution, listeningTriggersAfterAllExecution, listeningTriggersNotRelatedToExecution, executionTriggersAdditionnal, flags, targetValidator, affectedByLocalisation, selfTrigger, max_execution, triggerTargetIsSelf, precalculateTriggerTarget, storeOnSelf, dontTriggerAnything);
        this.m_displayInSpellDescription = true;
        this.m_displayInStateBar = true;
        this.m_shouldRecomputeTarget = false;
        this.m_emptyCellNeeded = emptyCells;
        this.m_executionProbability = proba;
        this.m_executionProbabilityIncrement = probainc;
        this.m_paramValues = paramValues;
        this.m_paramsCount = paramValues.length / 2;
        this.m_maxTargetsCount = -1;
        this.m_notifyInChat = notifyInChat;
        this.m_notifyInChatForCaster = notifyInChatForCaster;
        this.m_notifyInChatForTarget = notifyInChatForTarget;
        this.m_notifyInChatWithCasterName = notifyInChatWithCasterName;
        this.m_maxExecutionIncr = maxExecutionIncr;
        this.m_containerMinLevel = containerMinLevel;
        this.m_containerMaxLevel = containerMaxLevel;
        this.m_conditions = crit;
        this.m_isUsableInFight = isUsableInFight;
        this.m_isUsableInWorld = isUsableInWorld;
        this.m_isPersonal = isPersonal;
    }
    
    @Override
    public byte getEffectType() {
        return 0;
    }
    
    @Override
    public float getExecutionProbability(final short containerLevel) {
        return this.m_executionProbability + containerLevel * this.m_executionProbabilityIncrement;
    }
    
    @Override
    public boolean isAnUsableEffect() {
        return this.m_isUsableInFight || this.m_isUsableInWorld;
    }
    
    @Override
    public boolean isUsableInFight() {
        return this.m_isUsableInFight;
    }
    
    @Override
    public boolean isUsableInWorld() {
        return this.m_isUsableInWorld;
    }
    
    public boolean isPersonal() {
        return this.m_isPersonal;
    }
    
    @Override
    public byte getMaxTargetsCount() {
        return this.m_maxTargetsCount;
    }
    
    @Override
    public float[] getRawParams() {
        return this.m_paramValues;
    }
    
    @Override
    public int getParamsCount() {
        return this.m_paramsCount;
    }
    
    @Override
    public int getParam(final int paramNum, final short containerLevel, final RoundingMethod roundingMethod) {
        final float value = this.getParam(paramNum, containerLevel);
        switch (roundingMethod) {
            case RANDOM: {
                return ValueRounder.randomRound(value);
            }
            case LIKE_PREVIOUS_LEVEL: {
                if (this.m_paramValues[paramNum * 2 + 1] >= 0.0f) {
                    return (int)Math.floor(value);
                }
                return (int)Math.ceil(value);
            }
            default: {
                return (int)value;
            }
        }
    }
    
    @Override
    public float getParam(final int paramNum, final short containerLevel) {
        if (paramNum < 0 || paramNum >= this.m_paramValues.length / 2) {
            throw new IllegalArgumentException("Tentative de r\u00e9cup\u00e9ration d'un param\u00e8tre d'ID " + paramNum + " alors que le nombre de valeurs est de " + this.m_paramValues.length + " effet : " + this.getEffectId());
        }
        float inc = this.m_paramValues[paramNum * 2 + 1] * containerLevel;
        if (Math.abs(inc - Math.round(inc)) < 1.0E-5) {
            inc = Math.round(inc);
        }
        return this.m_paramValues[paramNum * 2] + inc;
    }
    
    @Override
    public float getParam(final int paramNum) {
        if (paramNum < 0 || paramNum >= this.m_paramValues.length / 2) {
            throw new IllegalArgumentException("Tentative de r\u00e9cup\u00e9ration d'un param\u00e8tre d'ID " + paramNum + " alors que le nombre de valeurs est de " + this.m_paramValues.length);
        }
        return this.m_paramValues[paramNum * 2];
    }
    
    public float getParamInc(final int paramNum) {
        if (paramNum < 0 || paramNum >= this.m_paramValues.length / 2) {
            throw new IllegalArgumentException("Tentative de r\u00e9cup\u00e9ration d'un param\u00e8tre d'ID " + paramNum + " alors que le nombre de valeurs est de " + this.m_paramValues.length);
        }
        return this.m_paramValues[paramNum * 2 + 1];
    }
    
    @Override
    public boolean notifyInChat() {
        return this.m_notifyInChat;
    }
    
    @Override
    public boolean notifyInChatForCaster() {
        return this.m_notifyInChatForCaster;
    }
    
    @Override
    public boolean notifyInChatForTarget() {
        return this.m_notifyInChatForTarget;
    }
    
    @Override
    public boolean notifyInChatWithCasterName() {
        return this.m_notifyInChatWithCasterName;
    }
    
    @Override
    public WakfuStandardEffect clone() {
        final float[] params = new float[this.m_paramValues.length];
        System.arraycopy(this.m_paramValues, 0, params, 0, this.m_paramValues.length);
        final WakfuStandardEffect clone = new WakfuStandardEffect(this.getEffectId(), this.getActionId(), this.getAreaOfEffect(), this.getListeningTriggersBeforeComputation(), this.getListeningTriggersBeforeExecution(), this.getListeningTriggerForUnapplication(), this.getListeningTriggersAfterExecution(), this.getListeningTriggerAfterAllExecutions(), this.getListeningTriggerNotRelatedToExecutions(), this.getExecutionTriggersAdditionnal(), this.getFlags(), this.getTargetValidator(), this.isAffectedByLocalisation(), params, this.m_executionProbability, this.m_executionProbabilityIncrement, this.isSelfTrigger(), this.triggerTargetIsSelf(), this.preCalculateTriggerTarget(), this.mustStoreOnCaster(), this.m_containerMinLevel, this.m_containerMaxLevel, this.m_conditions, this.getMaximumExecutions(), this.getMaxExecutionIncr(), this.dontTriggerAnything(), this.isUsableInFight(), this.isUsableInWorld(), this.notifyInChat(), this.isPersonal(), this.getEmptyCellNeededAreaOfEffect(), this.notifyInChatForCaster(), this.notifyInChatForTarget(), this.notifyInChatWithCasterName());
        clone.m_triggerListenerType = this.m_triggerListenerType;
        return clone;
    }
    
    @Override
    public float getMaxExecutionIncr() {
        return this.m_maxExecutionIncr;
    }
    
    @Override
    public int getScriptFileId() {
        return this.m_scriptFileId;
    }
    
    @Override
    public void setScriptFileId(final int scriptFileId) {
        this.m_scriptFileId = scriptFileId;
    }
    
    @Override
    public void setTriggerListenerType(final TriggerListenerType triggerListenerType) {
        this.m_triggerListenerType = triggerListenerType;
    }
    
    @Override
    public void setTriggerTargetType(final TriggerTargetType triggerTargetType) {
        this.m_triggerTargetType = triggerTargetType;
    }
    
    @Override
    public TriggerTargetType getTriggerTargetType() {
        return this.m_triggerTargetType;
    }
    
    @Override
    public void setTriggerCasterType(final TriggerCasterType triggerCasterType) {
        this.m_triggerCasterType = triggerCasterType;
    }
    
    @Override
    public TriggerCasterType getTriggerCasterType() {
        return this.m_triggerCasterType;
    }
    
    @Override
    public boolean triggerTargetIsSelf() {
        return this.m_triggerTargetType == TriggerTargetType.EFFECT_CARRIER;
    }
    
    @Override
    public void setIsDurationInCasterTurn(final boolean isDurationInCasterTurn) {
        super.setDurationInCasterTurn(isDurationInCasterTurn);
    }
    
    @Override
    public SimpleCriterion getConditions() {
        return this.m_conditions;
    }
    
    @Override
    public int getContainerMinLevel() {
        return this.m_containerMinLevel;
    }
    
    @Override
    public int getContainerMaxLevel() {
        return this.m_containerMaxLevel;
    }
    
    public float getExecutionProbabilityIncrement() {
        return this.m_executionProbabilityIncrement;
    }
    
    @Override
    public float getExecutionProbability() {
        return this.m_executionProbability;
    }
    
    @Override
    public void setDisplayInSpellDescription(final boolean displayInSpellDescription) {
        this.m_displayInSpellDescription = displayInSpellDescription;
    }
    
    @Override
    public boolean isDisplayInSpellDescription() {
        return this.m_displayInSpellDescription;
    }
    
    @Override
    public boolean isDisplayInStateBar() {
        return this.m_displayInStateBar;
    }
    
    @Override
    public void setDisplayInStateBar(final boolean displayInStateBar) {
        this.m_displayInStateBar = displayInStateBar;
    }
    
    @Override
    public boolean isShouldRecomputeTarget() {
        return this.m_shouldRecomputeTarget;
    }
    
    public void setShouldRecomputeTarget(final boolean shouldRecomputeTarget) {
        this.m_shouldRecomputeTarget = shouldRecomputeTarget;
    }
    
    @Override
    public void addActionToExecuteOnApplication(final HMIAction action) {
        if (this.m_actionsToExecuteOnApplication == null) {
            this.m_actionsToExecuteOnApplication = new ArrayList<HMIAction>();
        }
        this.m_actionsToExecuteOnApplication.add(action);
        this.addActionToOrderdedList(action);
    }
    
    @Override
    public void addActionToExecuteOnUnApplication(final HMIAction action) {
        if (this.m_actionsToExecuteOnUnApplication == null) {
            this.m_actionsToExecuteOnUnApplication = new ArrayList<HMIAction>();
        }
        this.m_actionsToExecuteOnUnApplication.add(action);
        this.addActionToOrderdedList(action);
    }
    
    @Override
    public void addActionToExecuteOnExecution(final HMIAction action) {
        if (this.m_actionsToExecuteOnExecution == null) {
            this.m_actionsToExecuteOnExecution = new ArrayList<HMIAction>();
        }
        this.m_actionsToExecuteOnExecution.add(action);
        this.addActionToOrderdedList(action);
    }
    
    @Override
    public void addActionToStopOnUnApplication(final HMIAction action) {
        if (this.m_actionsToStopOnUnApplication == null) {
            this.m_actionsToStopOnUnApplication = new ArrayList<HMIAction>();
        }
        this.m_actionsToStopOnUnApplication.add(action);
        this.addActionToOrderdedList(action);
    }
    
    @Override
    public void addActionToStopOnExecution(final HMIAction action) {
        if (this.m_actionsToStopOnExecution == null) {
            this.m_actionsToStopOnExecution = new ArrayList<HMIAction>();
        }
        this.m_actionsToStopOnExecution.add(action);
        this.addActionToOrderdedList(action);
    }
    
    private void addActionToOrderdedList(final HMIAction action) {
        if (this.m_actionsOrder == null) {
            this.m_actionsOrder = new ArrayList<HMIAction>();
        }
        if (!this.m_actionsOrder.contains(action)) {
            this.m_actionsOrder.add(action);
        }
    }
    
    @Override
    public Iterator<HMIAction> getActionsToExecuteOnApplication() {
        return (this.m_actionsToExecuteOnApplication != null) ? this.m_actionsToExecuteOnApplication.iterator() : new EmptyIterator<HMIAction>();
    }
    
    @Override
    public Iterator<HMIAction> getActionsToExecuteOnExecution() {
        return (this.m_actionsToExecuteOnExecution != null) ? this.m_actionsToExecuteOnExecution.iterator() : new EmptyIterator<HMIAction>();
    }
    
    @Override
    public boolean isActionToExecuteOnUnapplication(final HMIAction action) {
        return this.m_actionsToExecuteOnUnApplication != null && this.m_actionsToExecuteOnUnApplication.contains(action);
    }
    
    @Override
    public boolean isActionToStopOnUnapplication(final HMIAction action) {
        return this.m_actionsToStopOnUnApplication != null && this.m_actionsToStopOnUnApplication.contains(action);
    }
    
    @Override
    public boolean isActionToExecuteOnApplication(final HMIAction action) {
        return this.m_actionsToExecuteOnApplication != null && this.m_actionsToExecuteOnApplication.contains(action);
    }
    
    @Override
    public boolean isActionToExecuteOnExecution(final HMIAction action) {
        return this.m_actionsToExecuteOnExecution != null && this.m_actionsToExecuteOnExecution.contains(action);
    }
    
    @Override
    public boolean isActionToStopOnExecution(final HMIAction action) {
        return this.m_actionsToStopOnExecution != null && this.m_actionsToStopOnExecution.contains(action);
    }
    
    @Override
    public List<HMIAction> getActionsOrder() {
        return this.m_actionsOrder;
    }
    
    @Override
    public AreaOfEffect getEmptyCellNeededAreaOfEffect() {
        return this.m_emptyCellNeeded;
    }
    
    public void setRecomputeAreaOfEffectDisplay(final boolean recomputeAreaOfEffectDisplay) {
        this.m_recomputeAreaOfEffectDisplay = recomputeAreaOfEffectDisplay;
    }
    
    @Override
    public boolean recomputeAreaOfEffectDisplay() {
        return this.m_recomputeAreaOfEffectDisplay;
    }
    
    @Override
    public boolean isCriterionGrayable() {
        return this.m_criterionGrayable;
    }
    
    public void setCriterionGrayable(final boolean criterionGrayable) {
        this.m_criterionGrayable = criterionGrayable;
    }
    
    @Override
    public boolean doNotNotify() {
        return this.m_doNotNotify;
    }
    
    public void setDoNotNotify(final boolean doNotNotify) {
        this.m_doNotNotify = doNotNotify;
    }
    
    public void addProperties(final int... effectProperties) {
        for (int i = 0; i < effectProperties.length; ++i) {
            final int id = effectProperties[i];
            final RunningEffectPropertyType property = RunningEffectPropertyType.getPropertyFromId(id);
            this.addProperty(property);
        }
    }
    
    public void addProperty(final RunningEffectPropertyType property) {
        if (property == null) {
            return;
        }
        if (this.m_properties == null) {
            this.m_properties = new HashSet<RunningEffectPropertyType>();
        }
        this.m_properties.add(property);
    }
    
    @Override
    public boolean hasProperty(final RunningEffectPropertyType property) {
        return this.m_properties != null && this.m_properties.contains(property);
    }
    
    @Override
    public EffectExecutionResult execute(final EffectContainer cont, final EffectUser launcher, final EffectContext context, final Constants<? extends StaticRunningEffect> constants, final int targetCellx, final int targetCelly, final short targetCellz, final EffectUser target, final EffectExecutionParameters params, final boolean withResult) {
        if (!this.checkEmptyCellsNeeded(launcher, context, targetCellx, targetCelly, targetCellz)) {
            return null;
        }
        final StaticRunningEffect<Effect, EffectContainer> sre = (StaticRunningEffect<Effect, EffectContainer>)((StaticRunningEffect)constants.getObjectFromId(this.getActionId())).newInstance(context, null);
        final EffectExecutionResult executionResult = sre.run(this, cont, context, launcher, targetCellx, targetCelly, targetCellz, this.isPersonal() ? launcher : target, params);
        if (sre instanceof RunningEffect) {
            ((RunningEffect)sre).release();
        }
        if (!withResult) {
            executionResult.clear();
            return null;
        }
        return executionResult;
    }
    
    private boolean checkEmptyCellsNeeded(final EffectUser launcher, final EffectContext context, final int targetCellx, final int targetCelly, final short targetCellz) {
        if (this.getEmptyCellNeededAreaOfEffect() == null) {
            return true;
        }
        final Iterable iterable = TargetFinder.getInstance().getTargets(launcher, context.getTargetInformationProvider(), this.getEmptyCellNeededAreaOfEffect(), targetCellx, targetCelly, targetCellz, this.getTargetValidator());
        for (final Object next : iterable) {
            if (next instanceof AbstractEffectArea && !((AbstractEffectArea)next).isBlockingMovement()) {
                continue;
            }
            return false;
        }
        final FightMap fightMap = context.getFightMap();
        if (fightMap == null) {
            return true;
        }
        Point3 sourceCell;
        Direction8 dir;
        if (launcher != null) {
            sourceCell = launcher.getPosition();
            dir = launcher.getDirection();
        }
        else {
            sourceCell = new Point3(targetCellx, targetCelly, targetCellz);
            dir = Direction8.NORTH_EAST;
        }
        final Iterable<int[]> cells = this.getEmptyCellNeededAreaOfEffect().getCells(targetCellx, targetCelly, targetCellz, sourceCell.getX(), sourceCell.getY(), sourceCell.getZ(), dir);
        for (final int[] cell : cells) {
            if (!fightMap.isInsideOrBorder(cell[0], cell[1])) {
                return false;
            }
            if (fightMap.getObstacle(cell[0], cell[1]) != null) {
                return false;
            }
        }
        return true;
    }
    
    static {
        EMPTY_INT_ARRAY = new int[0];
        EMPTY_FLOAT_ARRAY = new float[0];
        INITIALIZED_TO_0_INT_ARRAY = new int[] { 0 };
    }
}
