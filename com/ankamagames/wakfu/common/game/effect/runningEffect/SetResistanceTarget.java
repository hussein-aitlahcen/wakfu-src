package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SetResistanceTarget extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetResistanceTarget.PARAMETERS_LIST_SET;
    }
    
    public SetResistanceTarget() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public SetResistanceTarget newInstance() {
        SetResistanceTarget re;
        try {
            re = (SetResistanceTarget)SetResistanceTarget.m_staticPool.borrowObject();
            re.m_pool = SetResistanceTarget.m_staticPool;
        }
        catch (Exception e) {
            re = new SetResistanceTarget();
            re.m_pool = null;
            re.m_isStatic = false;
            SetResistanceTarget.m_logger.error((Object)("Erreur lors d'un checkOut sur un SetResistanceTarget : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        this.setNotified();
        if (this.m_caster == null || this.m_target == null) {
            return;
        }
        if (this.m_target instanceof BasicCharacterInfo) {
            ((BasicCharacterInfo)this.m_target).setResistanceTarget(this.m_caster);
        }
    }
    
    @Override
    public void unapplyOverride() {
        super.unapplyOverride();
        if (this.m_target == null) {
            return;
        }
        if (this.m_target instanceof BasicCharacterInfo) {
            ((BasicCharacterInfo)this.m_target).setResistanceTarget(null);
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
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetResistanceTarget>() {
            @Override
            public SetResistanceTarget makeObject() {
                return new SetResistanceTarget();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
