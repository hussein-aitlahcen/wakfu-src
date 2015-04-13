package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class HpLossFunctionPaPm extends EffectValueFunctionPaPm
{
    private static final ObjectPool m_staticPool;
    
    public HpLossFunctionPaPm() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public HpLossFunctionPaPm newInstance() {
        HpLossFunctionPaPm re;
        try {
            re = (HpLossFunctionPaPm)HpLossFunctionPaPm.m_staticPool.borrowObject();
            re.m_pool = HpLossFunctionPaPm.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossFunctionPaPm();
            re.m_pool = null;
            HpLossFunctionPaPm.m_logger.error((Object)("Erreur lors d'un newInstance sur un HPLoss : " + e.getMessage()));
        }
        re.m_element = this.m_element;
        return re;
    }
    
    @Override
    protected void computeElement(final short containerLevel) {
        this.m_element = Elements.getElementFromId((byte)((WakfuEffect)this.m_genericEffect).getParam(0, containerLevel, RoundingMethod.RANDOM));
    }
    
    @Override
    protected void executeSubEffect() {
        final HPLoss hpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_element, HPLoss.ComputeMode.CLASSIC, this.m_value, this.m_target);
        hpLoss.setCaster(this.m_caster);
        ((RunningEffect<WakfuEffect, EC>)hpLoss).setGenericEffect((WakfuEffect)this.m_genericEffect);
        hpLoss.trigger((byte)1);
        hpLoss.computeModificatorWithDefaults();
        hpLoss.trigger((byte)2);
        hpLoss.execute(null, false);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossFunctionPaPm>() {
            @Override
            public HpLossFunctionPaPm makeObject() {
                return new HpLossFunctionPaPm();
            }
        });
    }
}
