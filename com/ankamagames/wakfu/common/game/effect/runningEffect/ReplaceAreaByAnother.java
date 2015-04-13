package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class ReplaceAreaByAnother extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final int REMOVE_ALL_AREAS = -1;
    private int m_zoneToRemove;
    private int m_zoneToAdd;
    private boolean m_newZoneInfinite;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ReplaceAreaByAnother.PARAMETERS_LIST_SET;
    }
    
    public ReplaceAreaByAnother() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ReplaceAreaByAnother newInstance() {
        ReplaceAreaByAnother re;
        try {
            re = (ReplaceAreaByAnother)ReplaceAreaByAnother.m_staticPool.borrowObject();
            re.m_pool = ReplaceAreaByAnother.m_staticPool;
        }
        catch (Exception e) {
            re = new ReplaceAreaByAnother();
            re.m_pool = null;
            re.m_isStatic = false;
            ReplaceAreaByAnother.m_logger.error((Object)("Erreur lors d'un checkOut sur un RemoveArea : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 1) {
            return;
        }
        this.m_zoneToRemove = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 2) {
            this.m_zoneToAdd = -1;
        }
        else {
            this.m_zoneToAdd = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 3) {
            this.m_newZoneInfinite = true;
        }
        else {
            this.m_newZoneInfinite = (((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_context == null) {
            return;
        }
        BasicEffectArea areaToRemove = null;
        if (this.m_target == null || !(this.m_target instanceof BasicEffectArea)) {
            final Collection<BasicEffectArea> activeEffectAreas = this.m_context.getEffectAreaManager().getActiveEffectAreas();
            for (final BasicEffectArea area : activeEffectAreas) {
                if (this.cannotBeRemoved(area)) {
                    continue;
                }
                if (area.contains(this.m_targetCell)) {
                    areaToRemove = area;
                    break;
                }
            }
            this.m_target = areaToRemove;
        }
        else {
            areaToRemove = (BasicEffectArea)this.m_target;
            if (this.cannotBeRemoved(areaToRemove)) {
                areaToRemove = null;
            }
        }
        if (areaToRemove == null) {
            this.setNotified();
            return;
        }
        if (!areaToRemove.canBeDestroyed()) {
            this.setNotified();
            return;
        }
        this.notifyExecution(triggerRE, trigger);
        final EffectUser owner = areaToRemove.getOwner();
        this.m_context.getEffectAreaManager().removeEffectArea(areaToRemove);
        if (this.m_zoneToAdd == -1) {
            return;
        }
        final SetEffectArea setEffectArea = SetEffectArea.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_targetCell, this.m_zoneToAdd);
        setEffectArea.setCaster(owner);
        setEffectArea.setShouldBeInfinite(this.m_newZoneInfinite);
        setEffectArea.setZoneLevel((short)1);
        ((RunningEffect<WakfuEffect, EC>)setEffectArea).setGenericEffect((WakfuEffect)this.m_genericEffect);
        ((RunningEffect<FX, WakfuEffectContainer>)setEffectArea).setEffectContainer(((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer());
        setEffectArea.setParent(this);
        setEffectArea.askForExecution();
        if (owner != null) {
            owner.getRunningEffectManager().storeEffect(setEffectArea);
        }
    }
    
    protected boolean cannotBeRemoved(final BasicEffectArea area) {
        return this.m_zoneToRemove != -1 && area.getBaseId() != this.m_zoneToRemove;
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
        this.m_zoneToRemove = 0;
        this.m_zoneToAdd = 0;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ReplaceAreaByAnother>() {
            @Override
            public ReplaceAreaByAnother makeObject() {
                return new ReplaceAreaByAnother();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Zones", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Zone a retirer (-1 pour tout retirer)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Zone a poser (infinie) (-1 pour ne rien poser)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Zones", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Zone a retirer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Zone a poser (-1 pour ne rien poser)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Dur\u00e9e (0=dur\u00e9e de l'effet, 1=infinie(defaut))", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
