package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class BestElementHpGain extends HPGain
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final ObjectPool m_staticPool;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return BestElementHpGain.PARAMETERS_LIST_SET;
    }
    
    public BestElementHpGain() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public BestElementHpGain newInstance() {
        BestElementHpGain re;
        try {
            re = (BestElementHpGain)BestElementHpGain.m_staticPool.borrowObject();
            re.m_pool = BestElementHpGain.m_staticPool;
        }
        catch (Exception e) {
            re = new BestElementHpGain();
            re.m_pool = null;
            re.m_isStatic = false;
            BestElementHpGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un BestElementHpGain : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void onApplication() {
        this.m_element = RunningEffectUtils.getCasterBestElement(this);
        super.onApplication();
    }
    
    @Override
    public void initValueFromParams() {
        this.setParamsToDefault();
        if (this.m_genericEffect == null) {
            return;
        }
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
        this.m_element = RunningEffectUtils.getCasterBestElement(this);
        super.effectiveComputeValue(triggerRE);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Valeur de base", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Prendre en compte le niveau du caster comme multiplicateur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Niveau du caster en multiplicateur de la valeur de base ", WakfuRunningEffectParameterType.CONFIG) }) });
        m_staticPool = new MonitoredPool(new ObjectFactory<BestElementHpGain>() {
            @Override
            public BestElementHpGain makeObject() {
                return new BestElementHpGain();
            }
        });
    }
}
