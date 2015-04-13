package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class GetCloser extends MovementEffect
{
    protected static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return GetCloser.PARAMETERS_LIST_SET;
    }
    
    @Override
    public GetCloser newInstance() {
        GetCloser re;
        try {
            re = (GetCloser)GetCloser.m_staticPool.borrowObject();
            re.m_pool = GetCloser.m_staticPool;
        }
        catch (Exception e) {
            re = new GetCloser();
            re.m_pool = null;
            GetCloser.m_logger.error((Object)("Erreur lors d'un checkOut sur un ArenaRunningEffect : " + e.getMessage()));
        }
        re.m_lifePointsToLose = this.m_lifePointsToLose;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(193);
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    public boolean hasSerializedAdditionnalInfoForExecution() {
        return true;
    }
    
    @Override
    public boolean validatePrecondition() {
        return this.m_caster != null && !this.moverIsCarried() && !this.moverIsRooted() && !this.moverCantGetCloser();
    }
    
    @Override
    boolean doesCollide() {
        return false;
    }
    
    @Override
    public MovementEffectUser getMover() {
        if (this.m_caster instanceof MovementEffectUser) {
            return (MovementEffectUser)this.m_caster;
        }
        return null;
    }
    
    @Override
    Point3 getReferentialCell() {
        return this.m_targetCell;
    }
    
    @Override
    boolean getCloser() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<GetCloser>() {
            @Override
            public GetCloser makeObject() {
                return new GetCloser();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Se rapproche au maximum de la cible", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Rapproche le caster de la cible", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nombre de case d'\u00e9cart (0 possible)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
