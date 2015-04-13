package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SpellBreak extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SpellBreak.PARAMETERS_LIST_SET;
    }
    
    @Override
    public SpellBreak newInstance() {
        SpellBreak re;
        try {
            re = (SpellBreak)SpellBreak.m_staticPool.borrowObject();
            re.m_pool = SpellBreak.m_staticPool;
        }
        catch (Exception e) {
            re = new SpellBreak();
            re.m_pool = null;
            re.m_isStatic = false;
            SpellBreak.m_logger.error((Object)("Erreur lors d'un checkOut sur un SpellBreak : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target != null) {
            this.m_target.addProperty(FightPropertyType.IS_CURRENT_SPELL_CAST_BROKEN);
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_target != null) {
            this.m_target.removeProperty(FightPropertyType.IS_CURRENT_SPELL_CAST_BROKEN);
        }
        super.unapplyOverride();
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
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SpellBreak>() {
            @Override
            public SpellBreak makeObject() {
                return new SpellBreak();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Annulation d'un lancer de sort", new WakfuRunningEffectParameter[0]) });
    }
}
