package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class LinkCasterAndTarget extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return LinkCasterAndTarget.PARAMETERS_LIST_SET;
    }
    
    public LinkCasterAndTarget() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public LinkCasterAndTarget newInstance() {
        LinkCasterAndTarget re;
        try {
            re = (LinkCasterAndTarget)LinkCasterAndTarget.m_staticPool.borrowObject();
            re.m_pool = LinkCasterAndTarget.m_staticPool;
        }
        catch (Exception e) {
            re = new LinkCasterAndTarget();
            re.m_pool = null;
            re.m_isStatic = false;
            LinkCasterAndTarget.m_logger.error((Object)("Erreur lors d'un checkOut sur un LinkCasterAndTarget : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(2123);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        boolean valid = true;
        if (this.m_target == null || this.m_caster == null) {
            valid = false;
        }
        else if (this.m_target.isActiveProperty(FightPropertyType.STABILIZED) || this.m_caster.isActiveProperty(FightPropertyType.STABILIZED)) {
            valid = false;
        }
        else if (!(this.m_target instanceof BasicCharacterInfo) || !(this.m_caster instanceof BasicCharacterInfo)) {
            valid = false;
        }
        if (!valid) {
            this.setNotified(true);
            return;
        }
        final BasicCharacterInfo caster = (BasicCharacterInfo)this.m_caster;
        final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
        caster.unlink();
        target.unlink();
        caster.linkTo(target);
        target.linkTo(caster);
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_target == null || this.m_caster == null) {
            return;
        }
        if (!(this.m_target instanceof BasicCharacterInfo) || !(this.m_caster instanceof BasicCharacterInfo)) {
            return;
        }
        ((BasicCharacterInfo)this.m_caster).unlink();
        ((BasicCharacterInfo)this.m_target).unlink();
        if (this.isValueComputationEnabled()) {
            if (this.getManagerWhereIamStored() == this.m_caster.getRunningEffectManager()) {
                this.m_target.getRunningEffectManager().removeLinkedToContainer(this.getEffectContainer(), true);
            }
            else {
                this.m_caster.getRunningEffectManager().removeLinkedToContainer(this.getEffectContainer(), true);
            }
        }
        super.unapplyOverride();
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<LinkCasterAndTarget>() {
            @Override
            public LinkCasterAndTarget makeObject() {
                return new LinkCasterAndTarget();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Se lie avec la cible", new WakfuRunningEffectParameter[0]) });
    }
}
