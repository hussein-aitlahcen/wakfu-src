package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpLossPercentOfValue extends HPLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private float m_percent;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossPercentOfValue.PARAMETERS_LIST_SET;
    }
    
    public HpLossPercentOfValue() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public HpLossPercentOfValue newInstance() {
        HpLossPercentOfValue re;
        try {
            re = (HpLossPercentOfValue)HpLossPercentOfValue.m_staticPool.borrowObject();
            re.m_pool = HpLossPercentOfValue.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossPercentOfValue();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossPercentOfValue.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpLossPercentOfValue : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void update(final int whatToUpdate, final float howMuchToUpate, final boolean set) {
        super.update(whatToUpdate, howMuchToUpate, set);
        switch (whatToUpdate) {
            case 0: {
                if (!set) {
                    this.m_value += (int)(this.m_value * howMuchToUpate / 100.0f);
                    break;
                }
                break;
            }
            case 1: {
                if (set) {
                    this.m_value = ValueRounder.randomRound(howMuchToUpate);
                    break;
                }
                this.m_value += (int)howMuchToUpate;
                break;
            }
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (linkedRE != null) {
            this.m_value = Math.max(1, Math.round(linkedRE.getValue() * this.m_percent / 100.0f));
        }
        super.executeOverride(linkedRE, trigger);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        this.m_value = 0;
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_percent = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossPercentOfValue>() {
            @Override
            public HpLossPercentOfValue makeObject() {
                return new HpLossPercentOfValue();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("% de la valeur de l'effet d\u00e9clencheur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% de la valeur de l'effet d\u00e9clencheur", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
