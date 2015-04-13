package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class RevealInvisibility extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RevealInvisibility.PARAMETERS_LIST_SET;
    }
    
    @Override
    public RevealInvisibility newInstance() {
        RevealInvisibility re;
        try {
            re = (RevealInvisibility)RevealInvisibility.m_staticPool.borrowObject();
            re.m_pool = RevealInvisibility.m_staticPool;
        }
        catch (Exception e) {
            re = new RevealInvisibility();
            re.m_pool = null;
            re.m_isStatic = false;
            RevealInvisibility.m_logger.error((Object)("Erreur lors d'un newInstance sur RevealInvisibility : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_caster != null && this.m_caster instanceof BasicCharacterInfo && this.m_target != null && this.m_target instanceof BasicCharacterInfo) {
            final BasicCharacterInfo basicCharacterInfo = (BasicCharacterInfo)this.m_caster;
            final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
            if (target.isActiveProperty(FightPropertyType.INVISIBLE) && basicCharacterInfo.getCurrentFight() != null) {
                ((AbstractFight)basicCharacterInfo.getCurrentFight()).revealInvisibleCharacterForAll(target);
            }
        }
        this.setNotified(true);
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
        m_staticPool = new MonitoredPool(new ObjectFactory<RevealInvisibility>() {
            @Override
            public RevealInvisibility makeObject() {
                return new RevealInvisibility();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Pas de params", new WakfuRunningEffectParameter[0]) });
    }
}
