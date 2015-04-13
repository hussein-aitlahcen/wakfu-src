package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpLossFunctionFighterLevel extends HPLoss
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private FighterCharacteristicType m_charac;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossFunctionFighterLevel.PARAMETERS_LIST_SET;
    }
    
    public HpLossFunctionFighterLevel() {
        super();
        this.setTriggersToExecute();
    }
    
    public HpLossFunctionFighterLevel(final Elements element, final ComputeMode mode, final FighterCharacteristicType charac) {
        super(element, mode);
        this.m_charac = charac;
        this.setTriggersToExecute();
    }
    
    @Override
    public HpLossFunctionFighterLevel newInstance() {
        HpLossFunctionFighterLevel re;
        try {
            re = (HpLossFunctionFighterLevel)HpLossFunctionFighterLevel.m_staticPool.borrowObject();
            re.m_pool = HpLossFunctionFighterLevel.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossFunctionFighterLevel();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossFunctionFighterLevel.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpLossFunctionFighterLevel : " + e.getMessage()));
        }
        this.copyParams(re);
        return re;
    }
    
    @Override
    protected void copyParams(final HPLoss re) {
        super.copyParams(re);
        ((HpLossFunctionFighterLevel)re).m_charac = this.m_charac;
    }
    
    @Override
    protected void extractParams(final RunningEffect triggerRE) {
        this.m_condition = 0;
        final short level = this.getContainerLevel();
        if (this.m_genericEffect == null) {
            return;
        }
        final float fValue = ((WakfuEffect)this.m_genericEffect).getParam(0, level);
        if (this.m_caster instanceof BasicFighter) {
            this.m_value = Math.round(fValue * ((BasicFighter)this.m_caster).getLevel());
        }
        else {
            this.m_value = 0;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 1) {
            this.m_condition = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossFunctionFighterLevel>() {
            @Override
            public HpLossFunctionFighterLevel makeObject() {
                return new HpLossFunctionFighterLevel();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Degats par niveau du caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("D\u00e9g\u00e2ts par niveau", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Avec Modificateur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("D\u00e9g\u00e2ts par niveau", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("mod : boost(1) / res (2) / rebound (4) / absorb(8) (defaut = 0)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
