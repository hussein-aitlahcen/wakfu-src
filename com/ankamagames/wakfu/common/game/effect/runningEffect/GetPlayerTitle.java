package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class GetPlayerTitle extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public GetPlayerTitle newInstance() {
        GetPlayerTitle re;
        try {
            re = (GetPlayerTitle)GetPlayerTitle.m_staticPool.borrowObject();
            re.m_pool = GetPlayerTitle.m_staticPool;
        }
        catch (Exception e) {
            re = new GetPlayerTitle();
            re.m_pool = null;
            re.m_isStatic = false;
            GetPlayerTitle.m_logger.error((Object)("Erreur lors d'un checkOut sur un GetPlayerTitle : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return GetPlayerTitle.PARAMETERS_LIST_SET;
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
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, (short)0, RoundingMethod.RANDOM);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_caster != null && this.m_caster instanceof BasicCharacterInfo) {
            ((BasicCharacterInfo)this.m_caster).addAvailableTitle(this.m_value);
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<GetPlayerTitle>() {
            @Override
            public GetPlayerTitle makeObject() {
                return new GetPlayerTitle();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("id de titre", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
