package com.ankamagames.wakfu.client.core.effect.manager;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;

public class EffectManager extends BaseEffectManager
{
    private static final Logger m_logger;
    private static final EffectManager INSTANCE;
    private BinaryLoader<StaticEffectBinaryData> m_loader;
    
    protected EffectManager() {
        super();
        this.m_loader = new BinaryLoader<StaticEffectBinaryData>() {
            @Nullable
            @Override
            public StaticEffectBinaryData createFromId(final int effectId) {
                try {
                    final StaticEffectBinaryData data = new StaticEffectBinaryData();
                    if (BinaryDocumentManager.getInstance().getId(effectId, data)) {
                        return data;
                    }
                }
                catch (Exception e) {
                    EffectManager.m_logger.error((Object)("erreur lors du chargement de l'effet " + effectId), (Throwable)e);
                }
                return null;
            }
        };
    }
    
    public void setLoader(final BinaryLoader<StaticEffectBinaryData> loader) {
        this.m_loader = loader;
    }
    
    public static EffectManager getInstance() {
        return EffectManager.INSTANCE;
    }
    
    @Nullable
    @Override
    protected final WakfuEffect createEffect(final int effectId) {
        if (effectId <= 0) {
            return null;
        }
        final StaticEffectBinaryData data = this.m_loader.createFromId(effectId);
        if (data == null) {
            return null;
        }
        return this.createEffectFromBinaryForm(data);
    }
    
    public final WakfuEffect loadAndAddEffect(final int effectId) {
        final WakfuEffect effect = this.createEffect(effectId);
        if (effect != null) {
            this.addEffect(effect);
        }
        return effect;
    }
    
    protected final WakfuEffect createEffectFromBinaryForm(final StaticEffectBinaryData bs) {
        final boolean isActiveEffect = bs.isActiveParentType();
        WakfuStandardEffect effect;
        if (isActiveEffect) {
            if (bs.isFightEffectParentType()) {
                effect = this.createFightEffect(bs);
            }
            else {
                effect = this.createWorldEffect(bs);
            }
        }
        else {
            effect = this.createStandardEffect(bs);
        }
        effect.setTriggerTargetType(TriggerTargetType.getTriggerTargetTypeFromId(bs.getTriggerTargetType()));
        effect.setTriggerCasterType(TriggerCasterType.getTriggerCasterTypeFromId(bs.getTriggerCasterType()));
        effect.setTriggerListenerType(TriggerListenerType.getTriggerListenerTypeFromId(bs.getTriggerListenerType()));
        effect.setScriptFileId(bs.getScriptFileId());
        effect.setIsDurationInCasterTurn(bs.isDurationInCasterTurn());
        effect.setDisplayInSpellDescription(bs.isDisplayInSpellDescription());
        effect.setDisplayInStateBar(bs.isDisplayInStateBar());
        effect.setRecomputeAreaOfEffectDisplay(bs.isRecomputeAreaOfEffectDisplay());
        effect.addProperties(bs.getEffectProperties());
        this.generateHmiActions(bs, bs.getEffectId(), effect);
        return effect;
    }
    
    private void generateHmiActions(final StaticEffectBinaryData bs, final int effectId, final WakfuStandardEffect effect) {
        if (bs.getHmiAction().trim().length() == 0) {
            return;
        }
        final String[] arr$;
        final String[] hmis = arr$ = StringUtils.split(bs.getHmiAction(), '~');
        for (final String hmi : arr$) {
            final String[] params = hmi.split("\\|", -1);
            if (params.length % 2 != 0) {
                EffectManager.m_logger.error((Object)("HMI error : Nombre de param\u00e8tres d\u00e9cod\u00e9s: " + params.length + " Attendu: 8 [" + bs.getHmiAction() + "]"));
            }
            else {
                Byte hmiStart = 0;
                Byte hmiEnd = 0;
                Byte hmiType = 0;
                String hmiData = "";
                boolean hmiBroadcast = false;
                for (int i = 0; i < params.length; i += 2) {
                    final String paramName = params[i];
                    final String paramValue = params[i + 1];
                    if (paramName.equals("start")) {
                        hmiStart = Byte.parseByte(paramValue);
                    }
                    else if (paramName.equals("end")) {
                        hmiEnd = Byte.parseByte(paramValue);
                    }
                    else if (paramName.equals("type")) {
                        hmiType = Byte.parseByte(paramValue);
                    }
                    else if (paramName.equals("data")) {
                        hmiData = paramValue;
                    }
                    else if (paramName.equals("broadcast")) {
                        hmiBroadcast = Boolean.parseBoolean(paramValue);
                    }
                }
                final HMIAction action = HMIActionManager.getInstance().registerNewAction(hmiType, hmiData, hmiBroadcast);
                if (action != null) {
                    switch (hmiStart) {
                        case 0: {
                            effect.addActionToExecuteOnApplication(action);
                            break;
                        }
                        case 1: {
                            effect.addActionToExecuteOnExecution(action);
                            break;
                        }
                        case 2: {
                            effect.addActionToExecuteOnUnApplication(action);
                            break;
                        }
                        default: {
                            EffectManager.m_logger.error((Object)("Impossible d'enregistrer le d\u00e9but d'une HMIAction pour l'effet " + effectId + " : type de d\u00e9but non connu : " + hmiStart));
                            break;
                        }
                    }
                    switch (hmiEnd) {
                        case 11: {
                            effect.addActionToStopOnExecution(action);
                            break;
                        }
                        case 12: {
                            effect.addActionToStopOnUnApplication(action);
                            break;
                        }
                        case 10: {
                            action.setInstant(true);
                            break;
                        }
                        default: {
                            EffectManager.m_logger.error((Object)("Impossible d'enregistrer la fin d'une HMIAction pour l'effet " + effectId + " : type de fin non connu : " + hmiEnd));
                            break;
                        }
                    }
                }
                else {
                    EffectManager.m_logger.error((Object)("Erreur lors du chargement de l'HMIAction de type " + hmiType + " sur l'effet " + effectId));
                }
            }
        }
    }
    
    private WakfuStandardEffect createStandardEffect(final StaticEffectBinaryData bs) {
        return new WakfuStandardEffect(bs.getEffectId(), bs.getActionId(), this.getAreaOfEffect(bs), bs.getTriggersBeforeComputation(), bs.getTriggersBeforeExecution(), bs.getTriggersForUnapplication(), bs.getTriggersAfterExecution(), bs.getTriggersAfterAllExecutions(), bs.getTriggersNotRelatedToExecutions(), bs.getTriggersAdditionnal(), this.computeFlags(bs), this.getTargetValidator(bs), bs.isAffectedByLocalisation(), bs.getParams(), bs.getProbabilityBase(), bs.getProbabilityInc(), bs.isSelfTrigger(), bs.isTriggerTargetIsSelf(), bs.isPrecalculateTriggerTarget(), bs.isStoreOnSelf(), bs.getContainerMinLevel(), bs.getContainerMaxLevel(), compileCriterion(bs), bs.getMaxExecution(), bs.getMaxExecutionIncr(), bs.getMaxTargetCount(), bs.isNotifyInChat(), bs.isDontTriggerAnything(), false, false, bs.isPersonal(), getEmptyCells(bs), bs.isNotifyInChatForCaster(), bs.isNotifyInChatForTarget(), bs.isNotifyInChatWithCasterName());
    }
    
    private WakfuStandardEffect createWorldEffect(final StaticEffectBinaryData bs) {
        return new WakfuWorldEffectImpl(bs.getEffectId(), bs.getActionId(), this.getAreaOfEffect(bs), bs.getTriggersBeforeComputation(), bs.getTriggersBeforeExecution(), bs.getTriggersForUnapplication(), bs.getTriggersAfterExecution(), bs.getTriggersAfterAllExecutions(), bs.getTriggersNotRelatedToExecutions(), bs.getTriggersAdditionnal(), this.computeFlags(bs), this.getTargetValidator(bs), bs.isAffectedByLocalisation(), this.getDurationBase(bs), this.getDurationIncrement(bs), bs.getParams(), bs.getProbabilityBase(), bs.getProbabilityInc(), bs.isSelfTrigger(), bs.isTriggerTargetIsSelf(), bs.isPrecalculateTriggerTarget(), bs.isStoreOnSelf(), bs.getContainerMinLevel(), bs.getContainerMaxLevel(), compileCriterion(bs), bs.getMaxExecution(), bs.getMaxExecutionIncr(), bs.getMaxTargetCount(), bs.isNotifyInChat(), bs.isDontTriggerAnything(), true, bs.isPersonal(), getEmptyCells(bs), bs.isNotifyInChatForCaster(), bs.isNotifyInChatForTarget(), bs.isNotifyInChatWithCasterName());
    }
    
    private WakfuStandardEffect createFightEffect(final StaticEffectBinaryData bs) {
        return new WakfuFightEffectImpl(bs.getEffectId(), bs.getActionId(), this.getAreaOfEffect(bs), bs.getTriggersBeforeComputation(), bs.getTriggersBeforeExecution(), bs.getTriggersForUnapplication(), bs.getTriggersAfterExecution(), bs.getTriggersAfterAllExecutions(), bs.getTriggersNotRelatedToExecutions(), bs.getTriggersAdditionnal(), this.computeFlags(bs), this.getTargetValidator(bs), bs.isAffectedByLocalisation(), this.getDurationBase(bs), this.getDurationIncrement(bs), bs.isEndsAtEndOfTurn(), bs.isDurationInFullTurns(), bs.getApplyDelayBase(), bs.getApplyDelayIncrement(), bs.getParams(), bs.getProbabilityBase(), bs.getProbabilityInc(), bs.isSelfTrigger(), bs.isTriggerTargetIsSelf(), bs.isPrecalculateTriggerTarget(), bs.isStoreOnSelf(), bs.getContainerMinLevel(), bs.getContainerMaxLevel(), compileCriterion(bs), bs.getMaxExecution(), bs.getMaxExecutionIncr(), bs.getMaxTargetCount(), bs.isNotifyInChat(), bs.isDontTriggerAnything(), true, bs.isPersonal(), getEmptyCells(bs), bs.isDecursable(), bs.isNotifyInChatForCaster(), bs.isNotifyInChatForTarget(), bs.isNotifyInChatWithCasterName());
    }
    
    private FightTargetValidator getTargetValidator(final StaticEffectBinaryData bs) {
        return new FightTargetValidator(bs.getTargetValidator());
    }
    
    private long computeFlags(final StaticEffectBinaryData bs) {
        long flags = 0L;
        if (bs.getCriticalState().startsWith("CRITICAL")) {
            flags |= 0x1L;
        }
        return flags;
    }
    
    private AreaOfEffect getAreaOfEffect(final StaticEffectBinaryData bs) {
        AreaOfEffect area = null;
        try {
            area = AreaOfEffectEnum.newInstance(bs.getAreaShape(), bs.getAreaSize(), bs.getAreaOrderingMethod());
        }
        catch (IllegalArgumentException e) {
            EffectManager.m_logger.error((Object)("Illegal Argument exception pour l'AOE de l'effect d'id : " + bs.getEffectId()));
        }
        return area;
    }
    
    private int getDurationBase(final StaticEffectBinaryData bs) {
        final boolean activeEffect = bs.isActiveParentType();
        return activeEffect ? bs.getDurationBase() : -1;
    }
    
    private float getDurationIncrement(final StaticEffectBinaryData bs) {
        final boolean activeEffect = bs.isActiveParentType();
        return activeEffect ? bs.getDurationInc() : -1.0f;
    }
    
    private static SimpleCriterion compileCriterion(final StaticEffectBinaryData bs) {
        SimpleCriterion crit = null;
        try {
            crit = CriteriaCompiler.compileBoolean(bs.getEffectCriterion());
        }
        catch (Exception e) {
            EffectManager.m_logger.error((Object)("Erreur lors de la compilation du crit\u00e8re de l'effet " + bs.getEffectId() + " de l'item " + bs.getParentId() + " de type " + bs.getEffectParentType()), (Throwable)e);
        }
        return crit;
    }
    
    private static AreaOfEffect getEmptyCells(final StaticEffectBinaryData bs) {
        final short emptyCellsAreaShape = bs.getEmptyCellsAreaShape();
        final int[] emptyCellstAreaSize = bs.getEmptyCellsAreaSize();
        final short emptyCellAreaOrderingMethod = bs.getEmptyCellsAreaOrderingMethod();
        AreaOfEffect emptyCells = null;
        try {
            if (emptyCellsAreaShape > 0) {
                emptyCells = AreaOfEffectEnum.newInstance(emptyCellsAreaShape, emptyCellstAreaSize, emptyCellAreaOrderingMethod);
            }
        }
        catch (IllegalArgumentException e) {
            EffectManager.m_logger.error((Object)("zone d'effet vide incorrecte pour l'effet d'id : " + bs.getEffectId()));
        }
        return emptyCells;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectManager.class);
        INSTANCE = new EffectManager();
    }
}
