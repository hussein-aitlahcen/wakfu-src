package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SetHourEffectArea extends SetEffectArea
{
    private static final ObjectPool m_hourEffectAreaPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private byte m_hour;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetHourEffectArea.PARAMETERS_LIST_SET;
    }
    
    @Override
    protected ObjectPool getPool() {
        return SetHourEffectArea.m_hourEffectAreaPool;
    }
    
    @Override
    public SetHourEffectArea newInstance() {
        final SetHourEffectArea re = (SetHourEffectArea)super.newInstance();
        re.m_hour = 0;
        return re;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_hour = 0;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_area == null) {
            final AbstractHourEffectArea modelHour = StaticEffectAreaManager.getInstance().getHourAreas(this.m_value);
            if (modelHour == null) {
                SetHourEffectArea.m_logger.error((Object)("EffectArea inexistante pour l'id " + this.m_value));
                this.setNotified(true);
                return;
            }
            if (this.m_hour == 0 && this.m_genericEffect != null) {
                final short level = this.getContainerLevel();
                this.m_hour = (byte)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
            }
            final short level = this.getContainerLevel();
            this.m_area = modelHour.instanceAnother(new EffectAreaParameters(this.m_newTargetId, this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), this.m_context, this.m_caster, level, this.m_caster.getDirection()));
            ((AbstractHourEffectArea)this.m_area).setHour(this.m_hour);
        }
        final ArrayList<BasicEffectArea> areasToRemove = new ArrayList<BasicEffectArea>();
        for (final BasicEffectArea area : this.m_context.getEffectAreaManager().getActiveEffectAreas()) {
            if (area.getType() == EffectAreaType.HOUR.getTypeId() && area.getOwner() == this.m_caster && ((AbstractHourEffectArea)area).getHour() == this.m_hour) {
                areasToRemove.add(area);
            }
        }
        for (final BasicEffectArea area : areasToRemove) {
            this.m_context.getEffectAreaManager().removeEffectArea(area);
        }
        if (this.m_area != null) {
            this.notifyExecution(linkedRE, trigger);
            this.m_context.getEffectAreaManager().addEffectArea(this.m_area);
        }
        else {
            this.setNotified(true);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        super.effectiveComputeValue(triggerRE);
        this.m_hour = (byte)((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.RANDOM);
    }
    
    static {
        m_hourEffectAreaPool = new MonitoredPool(new ObjectFactory<SetHourEffectArea>() {
            @Override
            public SetHourEffectArea makeObject() {
                return new SetHourEffectArea();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Param standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id de la zone heure", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("heure", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
