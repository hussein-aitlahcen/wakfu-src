package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ChangeSpellTargetCellByCasterCell extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ChangeSpellTargetCellByCasterCell.PARAMETERS_LIST_SET;
    }
    
    public ChangeSpellTargetCellByCasterCell() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ChangeSpellTargetCellByCasterCell newInstance() {
        ChangeSpellTargetCellByCasterCell re;
        try {
            re = (ChangeSpellTargetCellByCasterCell)ChangeSpellTargetCellByCasterCell.m_staticPool.borrowObject();
            re.m_pool = ChangeSpellTargetCellByCasterCell.m_staticPool;
        }
        catch (Exception e) {
            re = new ChangeSpellTargetCellByCasterCell();
            re.m_pool = null;
            re.m_isStatic = false;
            ChangeSpellTargetCellByCasterCell.m_logger.error((Object)("Erreur lors d'un checkOut sur un ChangeSpellTargetCellByCasterCell : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!(this.m_context instanceof WakfuFightEffectContext) || this.m_caster == null) {
            return;
        }
        ((WakfuFightEffectContext)this.m_context).getFight().changeSpellCastTargetCell(this.m_caster.getPosition());
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
        m_staticPool = new MonitoredPool(new ObjectFactory<ChangeSpellTargetCellByCasterCell>() {
            @Override
            public ChangeSpellTargetCellByCasterCell makeObject() {
                return new ChangeSpellTargetCellByCasterCell();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
