package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class TeleportCasterBehindTarget extends Teleport
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return TeleportCasterBehindTarget.PARAMETERS_LIST_SET;
    }
    
    public TeleportCasterBehindTarget() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public TeleportCasterBehindTarget newInstance() {
        TeleportCasterBehindTarget re;
        try {
            re = (TeleportCasterBehindTarget)TeleportCasterBehindTarget.m_staticPool.borrowObject();
            re.m_pool = TeleportCasterBehindTarget.m_staticPool;
        }
        catch (Exception e) {
            re = new TeleportCasterBehindTarget();
            re.m_pool = null;
            re.m_isStatic = false;
            TeleportCasterBehindTarget.m_logger.error((Object)("Erreur lors d'un checkOut sur un TeleportCasterBehindTarget : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_canBeExecuted = true;
        if (this.m_target == null) {
            this.m_canBeExecuted = false;
            return;
        }
        final Point3 targetPos = this.m_target.getPosition();
        final Direction8 targetDirection = this.m_target.getDirection();
        if (targetPos == null || targetDirection == null) {
            this.m_canBeExecuted = false;
            return;
        }
        this.m_targetCell.set(targetPos);
        this.m_targetCell.sub(targetDirection.m_x, targetDirection.m_y, 0);
        super.effectiveComputeValue(triggerRE);
    }
    
    @Override
    protected EffectUser getCharacterToTeleport() {
        return this.m_caster;
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
        m_staticPool = new MonitoredPool(new ObjectFactory<TeleportCasterBehindTarget>() {
            @Override
            public TeleportCasterBehindTarget makeObject() {
                return new TeleportCasterBehindTarget();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
