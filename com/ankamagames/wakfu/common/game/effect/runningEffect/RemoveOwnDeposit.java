package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RemoveOwnDeposit extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RemoveOwnDeposit.PARAMETERS_LIST_SET;
    }
    
    public RemoveOwnDeposit() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RemoveOwnDeposit newInstance() {
        RemoveOwnDeposit re;
        try {
            re = (RemoveOwnDeposit)RemoveOwnDeposit.m_staticPool.borrowObject();
            re.m_pool = RemoveOwnDeposit.m_staticPool;
        }
        catch (Exception e) {
            re = new RemoveOwnDeposit();
            re.m_pool = null;
            re.m_isStatic = false;
            RemoveOwnDeposit.m_logger.error((Object)("Erreur lors d'un checkOut sur un RemoveEffectArea : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_context == null) {
            return;
        }
        final Collection<BasicEffectArea> activeEffectAreas = this.m_context.getEffectAreaManager().getActiveEffectAreas();
        final ArrayList<BasicEffectArea> areaToRemove = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea area : activeEffectAreas) {
            if (area.contains(this.m_targetCell) && area.getType() == EffectAreaType.ENUTROF_DEPOSIT.getTypeId() && area.getOwner() == ((BasicCharacterInfo)this.m_caster).getOriginalController()) {
                areaToRemove.add(area);
            }
        }
        if (areaToRemove.isEmpty()) {
            return;
        }
        for (final BasicEffectArea area : areaToRemove) {
            this.m_context.getEffectAreaManager().removeEffectArea(area);
        }
    }
    
    @Override
    public boolean useCaster() {
        return false;
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
        m_staticPool = new MonitoredPool(new ObjectFactory<RemoveOwnDeposit>() {
            @Override
            public RemoveOwnDeposit makeObject() {
                return new RemoveOwnDeposit();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Default", new WakfuRunningEffectParameter[0]) });
    }
}
