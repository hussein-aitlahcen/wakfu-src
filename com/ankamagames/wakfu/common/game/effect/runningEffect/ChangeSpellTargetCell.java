package com.ankamagames.wakfu.common.game.effect.runningEffect;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ChangeSpellTargetCell extends WakfuRunningEffect
{
    private BitSet m_listenedTriggers;
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ChangeSpellTargetCell.PARAMETERS_LIST_SET;
    }
    
    public ChangeSpellTargetCell() {
        super();
        this.setTriggersToExecute();
        (this.m_listenedTriggers = new BitSet()).set(1024);
    }
    
    @Override
    public ChangeSpellTargetCell newInstance() {
        ChangeSpellTargetCell re;
        try {
            re = (ChangeSpellTargetCell)ChangeSpellTargetCell.m_staticPool.borrowObject();
            re.m_pool = ChangeSpellTargetCell.m_staticPool;
        }
        catch (Exception e) {
            re = new ChangeSpellTargetCell();
            re.m_pool = null;
            re.m_isStatic = false;
            ChangeSpellTargetCell.m_logger.error((Object)("Erreur lors d'un checkOut sur un ChangeSpellTargetCell : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!(this.m_context instanceof WakfuFightEffectContext)) {
            return;
        }
        ((WakfuFightEffectContext)this.m_context).getFight().changeSpellCastTargetCell(this.m_targetCell);
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
        m_staticPool = new MonitoredPool(new ObjectFactory<ChangeSpellTargetCell>() {
            @Override
            public ChangeSpellTargetCell makeObject() {
                return new ChangeSpellTargetCell();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Desc", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
