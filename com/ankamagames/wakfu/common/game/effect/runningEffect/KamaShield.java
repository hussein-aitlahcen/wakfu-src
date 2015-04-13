package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.inventory.moderation.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class KamaShield extends WakfuRunningEffect
{
    private static final MonitoredPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private float m_kamaToHPRatio;
    private int m_percentOfHPAbsorbed;
    private int m_maxHPLossAbsorption;
    private int m_minKamaLoss;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return KamaShield.PARAMETERS_LIST_SET;
    }
    
    @Override
    public KamaShield newInstance() {
        KamaShield result;
        try {
            result = (KamaShield)KamaShield.m_staticPool.borrowObject();
            result.m_pool = KamaShield.m_staticPool;
        }
        catch (Exception e) {
            KamaShield.m_logger.warn((Object)("Erreur lors de newInstance sur un " + this.getClass().getSimpleName()));
            result = new KamaShield();
            result.m_pool = null;
            result.m_isStatic = false;
        }
        return result;
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
        return false;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        this.m_kamaToHPRatio = ((WakfuEffect)this.m_genericEffect).getParam(0, level);
        this.m_percentOfHPAbsorbed = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
        this.m_maxHPLossAbsorption = (int)Math.floor(((WakfuEffect)this.m_genericEffect).getParam(2, level) / this.m_kamaToHPRatio);
        this.m_minKamaLoss = ((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.RANDOM);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (triggerRE != null && trigger) {
            this.updateHPLoss(triggerRE);
        }
        this.setNotified(true);
    }
    
    void updateHPLoss(final RunningEffect triggerRE) {
        final EffectUser hpLossTarget = triggerRE.getTarget();
        if (!(hpLossTarget instanceof BasicCharacterInfo)) {
            return;
        }
        final BasicCharacterInfo target = (BasicCharacterInfo)hpLossTarget;
        final int funds = target.getKamasCount();
        if (funds < this.m_minKamaLoss) {
            return;
        }
        final int maxHpLossAbsorptionWithCurrentFunds = (int)Math.floor(funds / this.m_kamaToHPRatio);
        int hpLossAbsorption = this.randomRound(triggerRE.getValue() * this.m_percentOfHPAbsorbed / 100.0f);
        if (hpLossAbsorption > this.m_maxHPLossAbsorption) {
            hpLossAbsorption = this.m_maxHPLossAbsorption;
        }
        if (hpLossAbsorption > maxHpLossAbsorptionWithCurrentFunds) {
            hpLossAbsorption = maxHpLossAbsorptionWithCurrentFunds;
        }
        final int kamaLoss = Math.max((int)Math.ceil(hpLossAbsorption * this.m_kamaToHPRatio), this.m_minKamaLoss);
        target.substractKamas(kamaLoss);
        ItemTracker.log(Level.INFO, target.getOwnerId(), target.getId(), target.getOwnerId(), target.getId(), "KamaShield", this.getEffectId(), target.getInstanceId(), -1, -1L, -1L, -kamaLoss);
        triggerRE.update(1, -hpLossAbsorption, false);
    }
    
    protected int randomRound(final float v) {
        return ValueRounder.randomRound(v);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<KamaShield>() {
            @Override
            public KamaShield makeObject() {
                return new KamaShield();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Absorbe des d\u00e9g\u00e2ts subis et les convertit en perte de Kamas", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Kamas perdus par point de d\u00e9g\u00e2t annul\u00e9", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("% des d\u00e9g\u00e2ts absorb\u00e9s", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Perte de Kamas maximale pour 1 source de d\u00e9g\u00e2ts", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Perte de Kamas minimale pour 1 source de d\u00e9g\u00e2ts", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
