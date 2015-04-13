package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RunningEffectGroupProbabilityFunctionPaPmPw extends RunningEffectGroup
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET_PROBA_FUNCTION_PW_PM_PW;
    private boolean m_executeActionCost;
    private float m_probaPerAP;
    private float m_probaPerMP;
    private float m_probaPerWP;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupProbabilityFunctionPaPmPw.PARAMETERS_LIST_SET_PROBA_FUNCTION_PW_PM_PW;
    }
    
    public RunningEffectGroupProbabilityFunctionPaPmPw() {
        super();
        this.m_executeActionCost = false;
        this.m_probaPerAP = 0.0f;
        this.m_probaPerMP = 0.0f;
        this.m_probaPerWP = 0.0f;
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupProbabilityFunctionPaPmPw newInstance() {
        RunningEffectGroupProbabilityFunctionPaPmPw re;
        try {
            re = (RunningEffectGroupProbabilityFunctionPaPmPw)RunningEffectGroupProbabilityFunctionPaPmPw.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupProbabilityFunctionPaPmPw.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupProbabilityFunctionPaPmPw();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupProbabilityFunctionPaPmPw.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupLevelFuntionState : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        super.effectiveComputeValue(triggerRE);
        this.m_probaPerAP = ((WakfuEffect)this.m_genericEffect).getParam(6, this.getContainerLevel());
        this.m_probaPerMP = ((WakfuEffect)this.m_genericEffect).getParam(7, this.getContainerLevel());
        this.m_probaPerWP = ((WakfuEffect)this.m_genericEffect).getParam(8, this.getContainerLevel());
        this.m_executeActionCost = (1 == ((WakfuEffect)this.m_genericEffect).getParam(9, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL));
    }
    
    @Override
    public boolean canBeExecuted() {
        if (!super.canBeExecuted()) {
            return false;
        }
        if (!this.isValueComputationEnabled()) {
            return true;
        }
        if (this.m_caster == null) {
            return false;
        }
        final int remainingAP = (this.m_probaPerAP == 0.0f) ? 0 : this.m_caster.getCharacteristicValue(FighterCharacteristicType.AP);
        final int remainingMP = (this.m_probaPerMP == 0.0f) ? 0 : this.m_caster.getCharacteristicValue(FighterCharacteristicType.MP);
        final int remainingWP = (this.m_probaPerWP == 0.0f) ? 0 : this.m_caster.getCharacteristicValue(FighterCharacteristicType.WP);
        final int proba = (int)(this.m_probaPerAP * remainingAP + this.m_probaPerMP * remainingMP + this.m_probaPerWP * remainingWP);
        return proba > 0 && (proba >= 100 || DiceRoll.roll(100) <= proba);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_executeActionCost && this.m_caster != null) {
            final int remainingAP = (this.m_probaPerAP == 0.0f) ? 0 : this.m_caster.getCharacteristicValue(FighterCharacteristicType.AP);
            final int remainingMP = (this.m_probaPerMP == 0.0f) ? 0 : this.m_caster.getCharacteristicValue(FighterCharacteristicType.MP);
            final int remainingWP = (this.m_probaPerWP == 0.0f) ? 0 : this.m_caster.getCharacteristicValue(FighterCharacteristicType.WP);
            final ActionCost actionCost = ActionCost.checkOut((EffectContext<WakfuEffect>)this.m_context, new SpellCost((byte)remainingAP, (byte)remainingMP, (byte)remainingWP), this.m_caster);
            actionCost.setCaster(this.m_caster);
            actionCost.setRunningEffectStatus(RunningEffectStatus.NEUTRAL);
            actionCost.execute(null, false);
        }
        super.executeOverride(linkedRE, trigger);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_executeActionCost = false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupProbabilityFunctionPaPmPw>() {
            @Override
            public RunningEffectGroupProbabilityFunctionPaPmPw makeObject() {
                return new RunningEffectGroupProbabilityFunctionPaPmPw();
            }
        });
        PARAMETERS_LIST_SET_PROBA_FUNCTION_PW_PM_PW = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Utilise le nombde de PA/PM/PW restants pour calculer la proba d'ex\u00e9cution du groupe d'effet", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de proba par PA", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de proba par PM", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% de proba par PW", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Consomme PA/PM/PW \u00e0 la fin (0=non, 1 = oui)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
