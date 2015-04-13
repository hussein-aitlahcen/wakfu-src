package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SetTeamId extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private byte m_previousTeamId;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetTeamId.PARAMETERS_LIST_SET;
    }
    
    public SetTeamId() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public SetTeamId newInstance() {
        SetTeamId re;
        try {
            re = (SetTeamId)SetTeamId.m_staticPool.borrowObject();
            re.m_pool = SetTeamId.m_staticPool;
        }
        catch (Exception e) {
            re = new SetTeamId();
            re.m_pool = null;
            re.m_isStatic = false;
            SetTeamId.m_logger.error((Object)("Erreur lors d'un checkOut sur un SetTeamId : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = -1;
        this.m_previousTeamId = -1;
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null) {
            this.setNotified();
            return;
        }
        if (this.m_value == -1) {
            this.setNotified();
            return;
        }
        if (!(this.m_target instanceof FightEffectUser)) {
            this.setNotified();
            return;
        }
        this.m_previousTeamId = ((FightEffectUser)this.m_target).getTeamId();
        ((FightEffectUser)this.m_target).setTeamId((byte)this.m_value);
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_previousTeamId < 0 && !(this.m_target instanceof FightEffectUser)) {
            return;
        }
        ((FightEffectUser)this.m_target).setTeamId(this.m_previousTeamId);
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
        this.m_value = -1;
        this.m_previousTeamId = -1;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetTeamId>() {
            @Override
            public SetTeamId makeObject() {
                return new SetTeamId();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Id de team", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de team", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
