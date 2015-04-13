package com.ankamagames.wakfu.common.game.effect.runningEffect;

import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class Carry extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    public static final TLongIntHashMap CARRIER_EFFECT_ID;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return Carry.PARAMETERS_LIST_SET;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(2224);
    }
    
    @Override
    public Carry newInstance() {
        Carry wre;
        try {
            wre = (Carry)Carry.m_staticPool.borrowObject();
            wre.m_pool = Carry.m_staticPool;
        }
        catch (Exception e) {
            wre = new Carry();
            wre.m_pool = null;
            wre.m_isStatic = false;
            Carry.m_logger.error((Object)("Erreur lors d'un checkOut sur un Carry : " + e.getMessage()));
        }
        return wre;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        boolean executed = false;
        if (!(this.m_target instanceof CarryTarget) || !(this.m_caster instanceof Carrier)) {
            this.setNotified(true);
            return;
        }
        if (this.m_target.isActiveProperty(FightPropertyType.STABILIZED) || this.m_target.isActiveProperty(FightPropertyType.CANNOT_BE_CARRIED)) {
            this.setNotified(true);
            return;
        }
        final int startx = this.m_target.getWorldCellX();
        final int starty = this.m_target.getWorldCellY();
        final short startz = this.m_target.getWorldCellAltitude();
        final CarryTarget abstractFighter = (CarryTarget)this.m_target;
        final Carrier caster = (Carrier)this.m_caster;
        if (caster.carry(abstractFighter)) {
            executed = true;
            Carry.CARRIER_EFFECT_ID.put(this.m_caster.getId(), this.getEffectId());
            this.notifyExecution(linkedRE, trigger);
            if (this.m_context.getEffectAreaManager() != null) {
                this.m_context.getEffectAreaManager().checkInAndOut(startx, starty, startz, this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), this.m_target);
            }
        }
        if (!executed) {
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
        m_staticPool = new MonitoredPool(new ObjectFactory<Carry>() {
            @Override
            public Carry makeObject() {
                return new Carry();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Porte la cible", new WakfuRunningEffectParameter[0]) });
        CARRIER_EFFECT_ID = new TLongIntHashMap();
    }
}
