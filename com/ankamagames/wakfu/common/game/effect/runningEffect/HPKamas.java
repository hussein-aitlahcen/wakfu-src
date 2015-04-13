package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.inventory.moderation.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class HPKamas extends HPLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private float m_kamaPerPDVRatio;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HPKamas.PARAMETERS_LIST_SET;
    }
    
    private HPKamas() {
        super();
    }
    
    public HPKamas(final Elements element, final ComputeMode mode) {
        super(element, mode);
        this.m_isStatic = true;
    }
    
    @Override
    public HPKamas newInstance() {
        HPKamas re;
        try {
            re = (HPKamas)HPKamas.m_staticPool.borrowObject();
            re.m_pool = HPKamas.m_staticPool;
        }
        catch (Exception e) {
            re = new HPKamas();
            re.m_pool = null;
            re.m_isStatic = false;
            HPKamas.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPKamas : " + e.getMessage()));
        }
        re.m_staticElement = this.m_staticElement;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        int kamas = (int)(this.m_value * this.m_kamaPerPDVRatio);
        if (this.getCaster() instanceof BasicCharacterInfo) {
            final BasicCharacterInfo caster = (BasicCharacterInfo)this.getCaster();
            final int count = caster.getKamasCount();
            if (count < kamas) {
                kamas = count;
                this.m_value = ValueRounder.randomRound(kamas / this.m_kamaPerPDVRatio);
            }
            caster.substractKamas(kamas);
            ItemTracker.log(Level.INFO, caster.getOwnerId(), caster.getId(), caster.getOwnerId(), caster.getId(), "HpKamas", this.getEffectId(), caster.getInstanceId(), -1, -1L, -1L, -kamas);
        }
        super.executeOverride(linkedRE, trigger);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
            this.m_kamaPerPDVRatio = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
        }
        this.computeModificatorWithDefaults();
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
        m_staticPool = new MonitoredPool(new ObjectFactory<HPKamas>() {
            @Override
            public HPKamas makeObject() {
                return new HPKamas(null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Perte de point de vie/Kamas limit\u00e9 par le portefeuille", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nombre de Pdv", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Ratio perte de Kama/PDV", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
