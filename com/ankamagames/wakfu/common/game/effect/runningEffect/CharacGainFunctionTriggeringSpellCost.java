package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CharacGainFunctionTriggeringSpellCost extends CharacGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacGainFunctionTriggeringSpellCost.PARAMETERS_LIST_SET;
    }
    
    public CharacGainFunctionTriggeringSpellCost() {
        super();
        this.setTriggersToExecute();
    }
    
    public CharacGainFunctionTriggeringSpellCost(final CharacteristicType charac) {
        super(charac);
    }
    
    @Override
    public CharacGainFunctionTriggeringSpellCost newInstance() {
        CharacGainFunctionTriggeringSpellCost re;
        try {
            re = (CharacGainFunctionTriggeringSpellCost)CharacGainFunctionTriggeringSpellCost.m_staticPool.borrowObject();
            re.m_pool = CharacGainFunctionTriggeringSpellCost.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacGainFunctionTriggeringSpellCost();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacGainFunctionTriggeringSpellCost.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacGainFunctionTriggeringSpellCost : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = 0;
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 1) {
            return;
        }
        if (triggerRE == null) {
            return;
        }
        if (!(this.m_charac instanceof FighterCharacteristicType)) {
            return;
        }
        float percentOfCost;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 0) {
            percentOfCost = 100.0f;
        }
        else {
            percentOfCost = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel());
        }
        if (this.getContext() == null) {
            return;
        }
        final WakfuSpellCaster spellCaster = (WakfuSpellCaster)this.getContext().getSpellCaster();
        if (spellCaster == null) {
            return;
        }
        if (spellCaster.getSpellLevel() == null) {
            return;
        }
        int cost = 0;
        switch ((FighterCharacteristicType)this.m_charac) {
            case AP: {
                cost = spellCaster.computeApCost();
                break;
            }
            case MP: {
                cost = spellCaster.computeMpCost();
                break;
            }
            case WP: {
                cost = spellCaster.computeWpCost();
                break;
            }
            default: {
                CharacGainFunctionTriggeringSpellCost.m_logger.error((Object)("Trying to compute triggering spell cost on a 'non-cost' charac : " + this.m_charac));
                cost = 0;
                break;
            }
        }
        this.m_value = ValueRounder.randomRound(cost * percentOfCost / 100.0f);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacGainFunctionTriggeringSpellCost>() {
            @Override
            public CharacGainFunctionTriggeringSpellCost makeObject() {
                return new CharacGainFunctionTriggeringSpellCost();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Default (100% du co\u00fbt)", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Rollback d'un % du cout du sort", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% du co\u00fbt rendu", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
