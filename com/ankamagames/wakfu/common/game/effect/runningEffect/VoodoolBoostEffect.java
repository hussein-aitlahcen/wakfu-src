package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class VoodoolBoostEffect extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return VoodoolBoostEffect.PARAMETERS_LIST_SET;
    }
    
    public VoodoolBoostEffect() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public VoodoolBoostEffect newInstance() {
        VoodoolBoostEffect re;
        try {
            re = (VoodoolBoostEffect)VoodoolBoostEffect.m_staticPool.borrowObject();
            re.m_pool = VoodoolBoostEffect.m_staticPool;
        }
        catch (Exception e) {
            re = new VoodoolBoostEffect();
            re.m_pool = null;
            re.m_isStatic = false;
            VoodoolBoostEffect.m_logger.error((Object)("Erreur lors d'un checkOut sur un VoodoolBoostEffect : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (triggerRE == null || this.m_caster == null) {
            return;
        }
        if (!(triggerRE instanceof HPLoss)) {
            return;
        }
        final int bonus = this.m_caster.getCharacteristicValue(FighterCharacteristicType.VOODOOL_HP_LOSS_BONUS);
        final int value = triggerRE.getValue();
        triggerRE.forceValue(value + value * bonus / 100);
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
        m_staticPool = new MonitoredPool(new ObjectFactory<VoodoolBoostEffect>() {
            @Override
            public VoodoolBoostEffect makeObject() {
                return new VoodoolBoostEffect();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
