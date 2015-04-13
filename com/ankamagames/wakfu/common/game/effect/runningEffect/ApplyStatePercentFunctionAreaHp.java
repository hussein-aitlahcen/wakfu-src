package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ApplyStatePercentFunctionAreaHp extends ApplyState
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ApplyStatePercentFunctionAreaHp.PARAMETERS_LIST_SET;
    }
    
    public ApplyStatePercentFunctionAreaHp() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ApplyStatePercentFunctionAreaHp newInstance() {
        ApplyStatePercentFunctionAreaHp re;
        try {
            re = (ApplyStatePercentFunctionAreaHp)ApplyStatePercentFunctionAreaHp.m_staticPool.borrowObject();
            re.m_pool = ApplyStatePercentFunctionAreaHp.m_staticPool;
        }
        catch (Exception e) {
            re = new ApplyStatePercentFunctionAreaHp();
            re.m_pool = null;
            re.m_isStatic = false;
            ApplyStatePercentFunctionAreaHp.m_logger.error((Object)("Erreur lors d'un checkOut sur un ApplyStatePercentFunctionAreaHp : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected int getApplyPercent() {
        if (this.m_caster.hasCharacteristic(FighterCharacteristicType.AREA_HP) && this.m_genericEffect != null) {
            return ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) * this.m_caster.getCharacteristicValue(FighterCharacteristicType.AREA_HP);
        }
        return 0;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ApplyStatePercentFunctionAreaHp>() {
            @Override
            public ApplyStatePercentFunctionAreaHp makeObject() {
                return new ApplyStatePercentFunctionAreaHp();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("apply avec gestion des resistances/boosts", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("pourcentage d'application / AREA_HP", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
