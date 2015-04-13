package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class MakeFlee extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_isCheckedOut;
    private static final ObjectPool POOL;
    
    public static MakeFlee checkOut() {
        MakeFlee obj;
        try {
            obj = (MakeFlee)MakeFlee.POOL.borrowObject();
            obj.m_isCheckedOut = true;
        }
        catch (Exception e) {
            obj = new MakeFlee();
            MakeFlee.m_logger.error((Object)("Erreur lors d'un checkOut sur un objet de type MakeFlee : " + e.getMessage()));
        }
        return obj;
    }
    
    @Override
    public void release() {
        if (this.m_isCheckedOut) {
            try {
                MakeFlee.POOL.returnObject(this);
                this.m_isCheckedOut = false;
            }
            catch (Exception e) {
                MakeFlee.m_logger.error((Object)"Exception lors du retour au pool", (Throwable)e);
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return MakeFlee.PARAMETERS_LIST_SET;
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        MakeFlee re;
        try {
            re = (MakeFlee)MakeFlee.POOL.borrowObject();
            re.m_pool = MakeFlee.POOL;
        }
        catch (Exception e) {
            MakeFlee.m_logger.error((Object)("Erreur lors d'un checkOut sur un RE:Raise : " + e.getMessage()));
            re = new MakeFlee();
            re.m_isStatic = false;
            re.m_pool = null;
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null) {
            return;
        }
        this.notifyExecution(triggerRE, trigger);
        if (this.m_target instanceof BasicCharacterInfo) {
            ((BasicCharacterInfo)this.m_target).escapeFight();
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
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Pas de param", new WakfuRunningEffectParameter[0]) });
        POOL = new MonitoredPool(new ObjectFactory<MakeFlee>() {
            @Override
            public MakeFlee makeObject() {
                return new MakeFlee();
            }
        });
    }
}
