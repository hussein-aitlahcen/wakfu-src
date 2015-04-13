package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SpellAttack extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SpellAttack.PARAMETERS_LIST_SET;
    }
    
    @Override
    public SpellAttack newInstance() {
        SpellAttack wre;
        try {
            wre = (SpellAttack)SpellAttack.m_staticPool.borrowObject();
            wre.m_pool = SpellAttack.m_staticPool;
        }
        catch (Exception e) {
            wre = new SpellAttack();
            wre.m_pool = null;
            wre.m_isStatic = false;
            SpellAttack.m_logger.error((Object)("Erreur lors d'un checkOut sur un SpellAttack : " + e.getMessage()));
        }
        return wre;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target != null) {
            if (this.m_caster instanceof BasicCharacterInfo) {
                final int spellId = ((WakfuEffect)this.m_genericEffect).getParam(0, (short)1, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                final EffectUser target = this.getTarget();
                ((BasicCharacterInfo)this.m_caster).castSpellEffects(spellId, new Point3(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude()));
            }
        }
        else {
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
        m_staticPool = new MonitoredPool(new ObjectFactory<SpellAttack>() {
            @Override
            public SpellAttack makeObject() {
                return new SpellAttack();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Attaque avec un sort", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("spellId", WakfuRunningEffectParameterType.ID) }) });
    }
}
