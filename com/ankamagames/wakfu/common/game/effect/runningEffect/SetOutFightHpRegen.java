package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SetOutFightHpRegen extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetOutFightHpRegen.PARAMETERS_LIST_SET;
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        SetOutFightHpRegen re;
        try {
            re = (SetOutFightHpRegen)SetOutFightHpRegen.m_staticPool.borrowObject();
            re.m_pool = SetOutFightHpRegen.m_staticPool;
        }
        catch (Exception e) {
            re = new SetOutFightHpRegen();
            re.m_pool = null;
            SetOutFightHpRegen.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacGain : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null || !(this.m_target instanceof BasicCharacterInfo)) {
            return;
        }
        ((BasicCharacterInfo)this.m_target).setCustomHpRegen(this.m_value);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 1) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.RANDOM);
        }
        else {
            this.m_value = 0;
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_target == null || !(this.m_target instanceof BasicCharacterInfo)) {
            return;
        }
        ((BasicCharacterInfo)this.m_target).resetCustomHpRegen();
        super.unapplyOverride();
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
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetOutFightHpRegen>() {
            @Override
            public SetOutFightHpRegen makeObject() {
                return new SetOutFightHpRegen();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Change la valeur de hp regen", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nouvelle valeur", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
