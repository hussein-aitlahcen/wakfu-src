package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class RaiseOutOfCombat extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RaiseOutOfCombat.PARAMETERS_LIST_SET;
    }
    
    public RaiseOutOfCombat() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RaiseOutOfCombat newInstance() {
        RaiseOutOfCombat re;
        try {
            re = (RaiseOutOfCombat)RaiseOutOfCombat.m_staticPool.borrowObject();
            re.m_pool = RaiseOutOfCombat.m_staticPool;
        }
        catch (Exception e) {
            re = new RaiseOutOfCombat();
            re.m_pool = null;
            RaiseOutOfCombat.m_logger.error((Object)("Erreur lors d'un checkOut sur un NullEffect : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(203);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final float xpPenaltyFractionToCompensate = ((WakfuEffect)this.m_genericEffect).getParam(1);
        final int hpValue = (int)Math.max(1.0f, this.m_target.getCharacteristicValue(FighterCharacteristicType.HP) * ((WakfuEffect)this.m_genericEffect).getParam(0));
        if (this.m_target != null && this.m_target instanceof BasicCharacterInfo) {
            final BasicCharacterInfo character = (BasicCharacterInfo)this.m_target;
            this.m_target.getCharacteristic(FighterCharacteristicType.HP).set(hpValue);
            character.raiseOutOfCombat();
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
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
    public void unapplyOverride() {
        super.unapplyOverride();
    }
    
    @Override
    public boolean unapplicationMustBeNotified() {
        return super.unapplicationMustBeNotified();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RaiseOutOfCombat>() {
            @Override
            public RaiseOutOfCombat makeObject() {
                return new RaiseOutOfCombat();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("R\u00e9surrection (hors combat)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Pdv rendus (en % du total)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("P\u00e9nalit\u00e9 d'xp mitig\u00e9e (en % de la p\u00e9nalit\u00e9 inflig\u00e9e)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
