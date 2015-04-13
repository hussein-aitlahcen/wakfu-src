package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class MoveAwayFromCell extends MovementEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return MoveAwayFromCell.PARAMETERS_LIST_SET;
    }
    
    public MoveAwayFromCell() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public MoveAwayFromCell newInstance() {
        MoveAwayFromCell re;
        try {
            re = (MoveAwayFromCell)MoveAwayFromCell.m_staticPool.borrowObject();
            re.m_pool = MoveAwayFromCell.m_staticPool;
        }
        catch (Exception e) {
            re = new MoveAwayFromCell();
            re.m_pool = null;
            re.m_isStatic = false;
            MoveAwayFromCell.m_logger.error((Object)("Erreur lors d'un checkOut sur un MoveAwayFromCell : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    boolean getCloser() {
        return false;
    }
    
    @Override
    public boolean validatePrecondition() {
        return this.m_targetCell != null && !this.moverIsCarried() && !this.moverIsRooted() && !this.moverCantMoveAwayOrRepell();
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
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<MoveAwayFromCell>() {
            @Override
            public MoveAwayFromCell makeObject() {
                return new MoveAwayFromCell();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Distance a parcourir", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
