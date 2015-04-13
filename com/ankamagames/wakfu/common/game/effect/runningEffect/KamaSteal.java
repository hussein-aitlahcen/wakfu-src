package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.inventory.moderation.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class KamaSteal extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return KamaSteal.PARAMETERS_LIST_SET;
    }
    
    @Override
    public KamaSteal newInstance() {
        KamaSteal re;
        try {
            re = (KamaSteal)KamaSteal.m_staticPool.borrowObject();
            re.m_pool = KamaSteal.m_staticPool;
        }
        catch (Exception e) {
            re = new KamaSteal();
            re.m_pool = null;
            re.m_isStatic = false;
            KamaSteal.m_logger.error((Object)("Erreur lors d'un newInstance sur KamaSteal : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.isValueComputationEnabled() && this.m_value > 0 && this.getTarget() != null && this.getCaster() instanceof BasicCharacterInfo && this.getTarget() instanceof BasicCharacterInfo) {
            final BasicCharacterInfo caster = (BasicCharacterInfo)this.getCaster();
            final BasicCharacterInfo target = (BasicCharacterInfo)this.getTarget();
            final int nbk = target.substractKamas(this.m_value);
            caster.addKamas(this.m_value = nbk);
            ItemTracker.log(Level.INFO, target.getOwnerId(), target.getId(), caster.getOwnerId(), caster.getId(), "KamaSteal", this.getEffectId(), target.getInstanceId(), -1, -1L, -1L, nbk);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        this.m_value = 0;
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        }
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<KamaSteal>() {
            @Override
            public KamaSteal makeObject() {
                return new KamaSteal();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("nombre de kama vol\u00e9s fixes", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nombres de kamas vol\u00e9s", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
