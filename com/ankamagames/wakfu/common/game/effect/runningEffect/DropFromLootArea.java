package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.item.loot.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class DropFromLootArea extends WakfuRunningEffect
{
    private static final ObjectPool POOL;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_improvedDrop;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return DropFromLootArea.PARAMETERS_LIST_SET;
    }
    
    public DropFromLootArea() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public DropFromLootArea newInstance() {
        DropFromLootArea re;
        try {
            re = (DropFromLootArea)DropFromLootArea.POOL.borrowObject();
            re.m_pool = DropFromLootArea.POOL;
        }
        catch (Exception e) {
            re = new DropFromLootArea();
            re.m_pool = null;
            re.m_isStatic = false;
            DropFromLootArea.m_logger.error((Object)("Erreur lors d'un checkOut sur un DropFromLootArea : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_improvedDrop = (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1 && ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (!(this.m_effectContainer instanceof AbstractLootEffectArea)) {
            this.setNotified();
            return;
        }
        if (!(this.m_target instanceof Looter)) {
            this.setNotified();
            return;
        }
        final AreaOwnerProvider area = (AreaOwnerProvider)this.m_effectContainer;
        if (!(this.m_context instanceof WakfuFightEffectContext)) {
            this.setNotified();
            return;
        }
        final EffectUser areaOwner = area.getOwner();
        if (!(areaOwner instanceof Dropper)) {
            this.setNotified();
            return;
        }
        this.m_value = ((WakfuFightEffectContext)this.m_context).getFight().dropEnutrofPurse((Dropper)areaOwner, (Looter)this.m_target, true, this.m_improvedDrop);
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
        this.m_improvedDrop = false;
        super.onCheckIn();
    }
    
    static {
        POOL = new MonitoredPool(new ObjectFactory<DropFromLootArea>() {
            @Override
            public DropFromLootArea makeObject() {
                return new DropFromLootArea();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Drop basique", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Drop am\u00e9lior\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Drop Am\u00e9lior\u00e9 (1 = oui, 0 = non (defaut)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
