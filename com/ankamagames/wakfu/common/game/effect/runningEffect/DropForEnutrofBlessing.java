package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.item.loot.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;

public final class DropForEnutrofBlessing extends WakfuRunningEffect
{
    private static final ObjectPool POOL;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return DropForEnutrofBlessing.PARAMETERS_LIST_SET;
    }
    
    public DropForEnutrofBlessing() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public DropForEnutrofBlessing newInstance() {
        DropForEnutrofBlessing re;
        try {
            re = (DropForEnutrofBlessing)DropForEnutrofBlessing.POOL.borrowObject();
            re.m_pool = DropForEnutrofBlessing.POOL;
        }
        catch (Exception e) {
            re = new DropForEnutrofBlessing();
            re.m_pool = null;
            re.m_isStatic = false;
            DropForEnutrofBlessing.m_logger.error((Object)("Erreur lors d'un checkOut sur un DropForEnutrofBlessing : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_target == null) {
            this.setNotified();
            return;
        }
        if (this.m_context instanceof WakfuFightEffectContext && this.m_target instanceof Looter) {
            this.m_value = ((WakfuFightEffectContext)this.m_context).getFight().dropEnutrofBlessing((Looter)this.m_target);
        }
        else {
            this.setNotified();
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
        return false;
    }
    
    static {
        POOL = new MonitoredPool(new Factory());
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
    
    private static class Factory extends ObjectFactory<DropForEnutrofBlessing>
    {
        @Override
        public DropForEnutrofBlessing makeObject() {
            return new DropForEnutrofBlessing();
        }
    }
}
