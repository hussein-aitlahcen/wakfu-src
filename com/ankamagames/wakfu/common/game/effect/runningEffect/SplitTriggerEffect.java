package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SplitTriggerEffect extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_capAtCurrentHp;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SplitTriggerEffect.PARAMETERS_LIST_SET;
    }
    
    @Override
    public SplitTriggerEffect newInstance() {
        SplitTriggerEffect re;
        try {
            re = (SplitTriggerEffect)SplitTriggerEffect.m_staticPool.borrowObject();
            re.m_pool = SplitTriggerEffect.m_staticPool;
        }
        catch (Exception e) {
            re = new SplitTriggerEffect();
            re.m_isStatic = false;
            re.m_pool = null;
            SplitTriggerEffect.m_logger.error((Object)("Erreur lors d'un checkOut sur un SplitTriggerEffect : " + e.getMessage()));
        }
        re.m_capAtCurrentHp = this.m_capAtCurrentHp;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        final RunningEffect triggeringEffect = this.getTriggeringEffect(linkedRE);
        if (triggeringEffect == null || this.m_caster == null) {
            this.setNotified();
            return;
        }
        if (triggeringEffect.getId() == RunningEffectConstants.DEATH.getId()) {
            this.setNotified();
            return;
        }
        if (this.m_value >= 100) {
            triggeringEffect.setTarget(this.m_target);
            return;
        }
        final int initialValue = triggeringEffect.getValue();
        int valueToRedirect = initialValue * this.m_value / 100;
        if (this.m_capAtCurrentHp && this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            valueToRedirect = Math.min(valueToRedirect, this.m_target.getCharacteristicValue(FighterCharacteristicType.HP));
        }
        if (valueToRedirect == 0) {
            this.setNotified();
            return;
        }
        final RunningEffect applyToTarget = triggeringEffect.newParameterizedInstance();
        triggeringEffect.forceValue(initialValue - valueToRedirect);
        applyToTarget.setCaster(this.getTriggeringCaster(triggeringEffect));
        applyToTarget.setTarget(this.m_target);
        applyToTarget.setExecutionParameters(WakfuEffectExecutionParameters.checkOut(true, true, null));
        applyToTarget.forceValue(valueToRedirect);
        applyToTarget.disableValueComputation();
        applyToTarget.askForExecution();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 0) {
            this.m_value = 100;
        }
        else {
            this.m_value = Math.min(((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.RANDOM), 100);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            this.m_capAtCurrentHp = (((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
    }
    
    @Override
    public void onCheckIn() {
        this.m_capAtCurrentHp = false;
        super.onCheckIn();
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SplitTriggerEffect>() {
            @Override
            public SplitTriggerEffect makeObject() {
                return new SplitTriggerEffect();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("modifie la cible de l'effet d\u00e9clencheur par la cible de cet effet", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("partage la valeur de l'effet declencheur entre caster et target", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de la valeur de l'effet declencheur redirig\u00e9 vers la cible de cet effet (inf ou egal a 100)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Ne d\u00e9passe pas la valeur de hp de la nouvelle cible, ne fonctionne que sur les pertes de pdv", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de la valeur de l'effet declencheur redirig\u00e9 vers la cible de cet effet (inf ou egal a 100)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Ne d\u00e9passe pas la valeur de hp de la nouvelle cible (0=non (defaut), 1=oui)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
