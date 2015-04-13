package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class RandomHPLoss extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final ObjectPool m_staticPool;
    private Elements m_element;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RandomHPLoss.PARAMETERS_LIST_SET;
    }
    
    private RandomHPLoss() {
        super();
    }
    
    public RandomHPLoss(final Elements element) {
        super();
        this.m_element = element;
    }
    
    @Override
    public RandomHPLoss newInstance() {
        RandomHPLoss re;
        try {
            re = (RandomHPLoss)RandomHPLoss.m_staticPool.borrowObject();
            re.m_pool = RandomHPLoss.m_staticPool;
        }
        catch (Exception e) {
            re = new RandomHPLoss();
            re.m_pool = null;
            RandomHPLoss.m_logger.error((Object)("Erreur lors d'un newInstance sur un HPLoss : " + e.getMessage()));
        }
        re.m_element = this.m_element;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final HPLoss hpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, this.getElement(), HPLoss.ComputeMode.CLASSIC, this.m_value, this.m_target);
        hpLoss.setTargetCell(this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ());
        hpLoss.setCaster(this.m_caster);
        hpLoss.computeModificator(hpLoss.defaultCondition(), this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).checkFlags(1L), this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).isAffectedByLocalisation());
        ((RunningEffect<WakfuEffect, EC>)hpLoss).setGenericEffect((WakfuEffect)this.m_genericEffect);
        hpLoss.forceInstant();
        hpLoss.askForExecution();
        this.setNotified(true);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final int min = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.RANDOM);
        final int max = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.RANDOM);
        this.m_value = MathHelper.random(min, max);
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
    protected boolean canBeExecutedOnKO() {
        return true;
    }
    
    @Override
    public Elements getElement() {
        return this.m_element;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Type, Min, Max", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Min", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Max", WakfuRunningEffectParameterType.VALUE) }) });
        m_staticPool = new MonitoredPool(new ObjectFactory<RandomHPLoss>() {
            @Override
            public RandomHPLoss makeObject() {
                return new RandomHPLoss((RandomHPLoss$1)null);
            }
        });
    }
}
