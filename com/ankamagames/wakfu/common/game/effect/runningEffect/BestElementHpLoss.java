package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class BestElementHpLoss extends HPLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return BestElementHpLoss.PARAMETERS_LIST_SET;
    }
    
    public BestElementHpLoss() {
        super(null, ComputeMode.CLASSIC);
        this.setTriggersToExecute();
    }
    
    @Override
    public BestElementHpLoss newInstance() {
        BestElementHpLoss re;
        try {
            re = (BestElementHpLoss)BestElementHpLoss.m_staticPool.borrowObject();
            re.m_pool = BestElementHpLoss.m_staticPool;
        }
        catch (Exception e) {
            re = new BestElementHpLoss();
            re.m_pool = null;
            re.m_isStatic = false;
            BestElementHpLoss.m_logger.error((Object)("Erreur lors d'un checkOut sur un BestElementHpLoss : " + e.getMessage()));
        }
        this.copyParams(re);
        return re;
    }
    
    @Override
    protected void extractParams(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.defaultCondition();
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() <= 1) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1 && this.m_caster instanceof BasicFighter) {
            final float value = ((WakfuEffect)this.m_genericEffect).getParam(0);
            final float inc = ((WakfuStandardEffect)this.m_genericEffect).getParamInc(0);
            this.m_value = RunningEffectUtils.likePreviousLevelRound(value + inc * this.getContainerLevel() * ((BasicFighter)this.m_caster).getLevel(), inc);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_staticElement = RunningEffectUtils.getCasterBestElement(this);
        super.effectiveComputeValue(triggerRE);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<BestElementHpLoss>() {
            @Override
            public BestElementHpLoss makeObject() {
                return new BestElementHpLoss();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Dommages de base", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Dmg ou valeur %", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Prendre en compte le niveau du caster comme multiplicateur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Dmg ou valeur %", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Niveau du caster en multiplicateur de la valeur de base ", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
