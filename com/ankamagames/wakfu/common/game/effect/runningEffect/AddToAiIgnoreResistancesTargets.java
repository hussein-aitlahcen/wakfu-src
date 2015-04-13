package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class AddToAiIgnoreResistancesTargets extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AddToAiIgnoreResistancesTargets.PARAMETERS_LIST_SET;
    }
    
    public AddToAiIgnoreResistancesTargets() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public AddToAiIgnoreResistancesTargets newInstance() {
        AddToAiIgnoreResistancesTargets re;
        try {
            re = (AddToAiIgnoreResistancesTargets)AddToAiIgnoreResistancesTargets.m_staticPool.borrowObject();
            re.m_pool = AddToAiIgnoreResistancesTargets.m_staticPool;
        }
        catch (Exception e) {
            re = new AddToAiIgnoreResistancesTargets();
            re.m_pool = null;
            re.m_isStatic = false;
            AddToAiIgnoreResistancesTargets.m_logger.error((Object)("Erreur lors d'un checkOut sur un AddToAiIgnoreResistancesTargets : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_caster == null || this.m_target == null) {
            this.setNotified();
            return;
        }
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            this.setNotified();
            return;
        }
        final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
        target.addToIgnoreResistancesTargets(this.m_caster);
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_caster == null || this.m_target == null || !(this.m_target instanceof BasicCharacterInfo)) {
            return;
        }
        ((BasicCharacterInfo)this.m_target).removeFromIgnoreResistancesTargets(this.m_caster);
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
        m_staticPool = new MonitoredPool(new ObjectFactory<AddToAiIgnoreResistancesTargets>() {
            @Override
            public AddToAiIgnoreResistancesTargets makeObject() {
                return new AddToAiIgnoreResistancesTargets();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
