package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SpellRebound extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SpellRebound.PARAMETERS_LIST_SET;
    }
    
    @Override
    public SpellRebound newInstance() {
        SpellRebound re;
        try {
            re = (SpellRebound)SpellRebound.m_staticPool.borrowObject();
            re.m_pool = SpellRebound.m_staticPool;
        }
        catch (Exception e) {
            re = new SpellRebound();
            re.m_pool = null;
            re.m_isStatic = false;
            SpellRebound.m_logger.error((Object)("Erreur lors d'un checkOut sur un SpellRebound : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (linkedRE != null && trigger) {
            if (linkedRE.getCaster() != null && !linkedRE.getCaster().isActiveProperty(WorldPropertyType.BOSS)) {
                final EffectUser user = linkedRE.getCaster();
                linkedRE.setTargetCell(user.getWorldCellX(), user.getWorldCellY(), user.getWorldCellAltitude());
            }
            else {
                this.setNotified(true);
            }
        }
        else if (this.isValueComputationEnabled()) {
            this.setNotified(true);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SpellRebound>() {
            @Override
            public SpellRebound makeObject() {
                return new SpellRebound();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Renvoi de sort (complet)", new WakfuRunningEffectParameter[0]) });
    }
}
