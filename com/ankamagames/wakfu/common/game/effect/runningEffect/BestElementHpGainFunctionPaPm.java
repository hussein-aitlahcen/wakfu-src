package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class BestElementHpGainFunctionPaPm extends EffectValueFunctionPaPm
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final ObjectPool m_staticPool;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return BestElementHpGainFunctionPaPm.PARAMETERS_LIST_SET;
    }
    
    public BestElementHpGainFunctionPaPm() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public BestElementHpGainFunctionPaPm newInstance() {
        BestElementHpGainFunctionPaPm re;
        try {
            re = (BestElementHpGainFunctionPaPm)BestElementHpGainFunctionPaPm.m_staticPool.borrowObject();
            re.m_pool = BestElementHpGainFunctionPaPm.m_staticPool;
        }
        catch (Exception e) {
            re = new BestElementHpGainFunctionPaPm();
            re.m_pool = null;
            re.m_isStatic = false;
            BestElementHpGainFunctionPaPm.m_logger.error((Object)("Erreur lors d'un checkOut sur un BestElementHpGainFunctionPaPm : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void computeElement(final short containerLevel) {
        this.m_element = RunningEffectUtils.getCasterBestElement(this);
    }
    
    @Override
    protected void executeSubEffect() {
        final HPGain hpGain = HPGain.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_element);
        hpGain.setCaster(this.m_caster);
        hpGain.setTarget(this.m_target);
        ((RunningEffect<WakfuEffect, EC>)hpGain).setGenericEffect((WakfuEffect)this.m_genericEffect);
        hpGain.forceValue(this.m_value);
        hpGain.trigger((byte)1);
        hpGain.modifyValueWithModificatorIfNecessary();
        hpGain.trigger((byte)2);
        hpGain.execute(null, false);
    }
    
    @Override
    protected void computeDoubleHpLoss(final short containerLevel) {
        this.m_doubleHPLoss = false;
    }
    
    @Override
    protected void computeRemainingMp(final short containerLevel) {
    }
    
    @Override
    protected void computeRemainingAp(final short containerLevel) {
    }
    
    @Override
    protected float computeDmgPerAp(final short containerLevel) {
        float dmgPerAp = ((WakfuEffect)this.m_genericEffect).getParam(0);
        final float inc = ((WakfuStandardEffect)this.m_genericEffect).getParamInc(0);
        final boolean valueFunctionCasterLevel = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
        if (valueFunctionCasterLevel && this.m_caster instanceof BasicFighter) {
            dmgPerAp += inc * this.getContainerLevel() * ((BasicFighter)this.m_caster).getLevel();
        }
        else {
            dmgPerAp = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        return dmgPerAp;
    }
    
    @Override
    protected float computeDmgPerMp(final short containerLevel) {
        float dmgPerMp = ((WakfuEffect)this.m_genericEffect).getParam(1);
        final float inc = ((WakfuStandardEffect)this.m_genericEffect).getParamInc(1);
        final boolean valueFunctionCasterLevel = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
        if (valueFunctionCasterLevel && this.m_caster instanceof BasicFighter) {
            dmgPerMp += inc * this.getContainerLevel() * ((BasicFighter)this.m_caster).getLevel();
        }
        else {
            dmgPerMp = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        return dmgPerMp;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("valeur par PA, valeur par PM", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur par PA", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("valeur par PM", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("valeur par PA, valeur par PM, fct level caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur par PA", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("valeur par PM", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Niveau du caster en multiplicateur de l'increment ", WakfuRunningEffectParameterType.CONFIG) }) });
        m_staticPool = new MonitoredPool(new ObjectFactory<BestElementHpGainFunctionPaPm>() {
            @Override
            public BestElementHpGainFunctionPaPm makeObject() {
                return new BestElementHpGainFunctionPaPm();
            }
        });
    }
}
