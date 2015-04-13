package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class TeleportCaster extends Teleport
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final ObjectPool m_staticPool;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return TeleportCaster.PARAMETERS_LIST_SET;
    }
    
    @Override
    public TeleportCaster newInstance() {
        return checkOut(null, null, null, null, null);
    }
    
    public static TeleportCaster checkOut(final EffectContext<WakfuEffect> context, final WakfuEffect genericEffect, final EffectUser caster, final WakfuEffectContainer effectContainer, final Point3 arrivalCell) {
        TeleportCaster re;
        try {
            re = (TeleportCaster)TeleportCaster.m_staticPool.borrowObject();
            re.m_pool = TeleportCaster.m_staticPool;
        }
        catch (Exception e) {
            re = new TeleportCaster();
            re.m_pool = null;
            re.m_isStatic = false;
            TeleportCaster.m_logger.error((Object)("Erreur lors d'un checkOut sur un Push : " + e.getMessage()));
        }
        re.m_canBeExecuted = true;
        re.m_context = (EffectContext<FX>)context;
        re.m_genericEffect = (FX)genericEffect;
        re.m_caster = caster;
        re.m_effectContainer = (EC)effectContainer;
        if (arrivalCell != null) {
            re.setTargetCell(arrivalCell.getX(), arrivalCell.getY(), arrivalCell.getZ());
        }
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(197);
    }
    
    @Override
    protected EffectUser getCharacterToTeleport() {
        if (this.m_genericEffect == null || ((WakfuEffect)this.m_genericEffect).getParamsCount() == 0) {
            return this.m_caster;
        }
        final boolean canTpAreaOwner = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
        if (canTpAreaOwner && this.m_caster instanceof BasicEffectArea) {
            final BasicEffectArea area = (BasicEffectArea)this.m_caster;
            final EffectUser owner = area.getOwner();
            if (owner != null) {
                return owner;
            }
            TeleportCaster.m_logger.error((Object)("On cherche a t\u00e9l\u00e9porter le propri\u00e9taire d'une zone d'effet mais celui-ci est inconnu " + area.getBaseId()));
        }
        return this.m_caster;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            this.m_byPassProperties = (((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 4) {
            this.m_checkFightMap = (((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        super.effectiveComputeValue(triggerRE);
    }
    
    @Override
    protected boolean canTeleportCarried() {
        return this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2 && ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
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
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Pas de param, on tp uniquement le caster", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("On peut tp le propri\u00e9taire de la zone", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Tp owner : 0 = non (defaut), 1 = oui", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Autorise de tp un perso port\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Tp owner : 0 = non (defaut), 1 = oui", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Tp carried : 0 = non (defaut), 1 = ou", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Autorise de tp un perso port\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Tp owner : 0 = non (defaut), 1 = oui", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Tp carried : 0 = non (defaut), 1 = oui", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Tp Forc\u00e9e (by pass stabilis\u00e9 et autres propri\u00e9t\u00e9) 0 = non (defaut), 1 = oui", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Autorise de tp un perso port\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Tp owner : 0 = non (defaut), 1 = oui", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Tp carried : 0 = non (defaut), 1 = oui", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Tp Forc\u00e9e (by pass stabilis\u00e9 et autres propri\u00e9t\u00e9) 0 = non (defaut), 1 = oui", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Verifie les cellules bloquantes 0 = non, 1 = oui (defaut)", WakfuRunningEffectParameterType.CONFIG) }) });
        m_staticPool = new MonitoredPool(new ObjectFactory<TeleportCaster>() {
            @Override
            public TeleportCaster makeObject() {
                return new TeleportCaster();
            }
        });
    }
}
