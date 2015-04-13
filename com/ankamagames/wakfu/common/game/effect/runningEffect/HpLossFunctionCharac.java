package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpLossFunctionCharac extends HPLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private FighterCharacteristicType m_charac;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossFunctionCharac.PARAMETERS_LIST_SET;
    }
    
    public HpLossFunctionCharac() {
        super();
        this.setTriggersToExecute();
    }
    
    public HpLossFunctionCharac(final Elements element, final ComputeMode mode, final FighterCharacteristicType charac) {
        super(element, mode);
        this.m_charac = charac;
        this.setTriggersToExecute();
    }
    
    @Override
    public HpLossFunctionCharac newInstance() {
        HpLossFunctionCharac re;
        try {
            re = (HpLossFunctionCharac)HpLossFunctionCharac.m_staticPool.borrowObject();
            re.m_pool = HpLossFunctionCharac.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossFunctionCharac();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossFunctionCharac.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpLossFunctionCharac : " + e.getMessage()));
        }
        this.copyParams(re);
        return re;
    }
    
    @Override
    protected void copyParams(final HPLoss re) {
        super.copyParams(re);
        ((HpLossFunctionCharac)re).m_charac = this.m_charac;
    }
    
    @Override
    protected void extractParams(final RunningEffect triggerRE) {
        this.m_condition = 0;
        final short level = this.getContainerLevel();
        if (this.m_genericEffect == null) {
            return;
        }
        final float fValue = ((WakfuEffect)this.m_genericEffect).getParam(0, level);
        if (this.m_caster != null && this.m_caster.hasCharacteristic(this.m_charac)) {
            this.m_value = Math.round(fValue * this.m_caster.getCharacteristic(this.m_charac).max());
        }
        else {
            this.m_value = 0;
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossFunctionCharac>() {
            @Override
            public HpLossFunctionCharac makeObject() {
                return new HpLossFunctionCharac();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Degats par valeur max de la charac du caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("D\u00e9g\u00e2ts / valeur max", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
