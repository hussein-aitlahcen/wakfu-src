package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CharacGainForOsaFusion extends CharacGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacGainForOsaFusion.PARAMETERS_LIST_SET;
    }
    
    public CharacGainForOsaFusion() {
        super();
        this.setTriggersToExecute();
    }
    
    public CharacGainForOsaFusion(final CharacteristicType charac) {
        super(charac);
    }
    
    @Override
    public CharacGainForOsaFusion newInstance() {
        CharacGainForOsaFusion re;
        try {
            re = (CharacGainForOsaFusion)CharacGainForOsaFusion.m_staticPool.borrowObject();
            re.m_pool = CharacGainForOsaFusion.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacGainForOsaFusion();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacGainForOsaFusion.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacBuffForOsaFusion : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = 0;
        if (this.m_caster == null || !(this.m_caster instanceof BasicCharacterInfo)) {
            return;
        }
        if (!(this.m_context instanceof WakfuFightEffectContext)) {
            return;
        }
        final AbstractFight fight = ((WakfuFightEffectContext)this.m_context).getFight();
        final Collection<BasicCharacterInfo> casterControlled = (Collection<BasicCharacterInfo>)fight.getFightersControlledBy((BasicCharacterInfo)this.m_caster);
        short gobgobLevel = -1;
        for (final BasicCharacterInfo controlled : casterControlled) {
            if (controlled.getBreedId() == 1620) {
                gobgobLevel = controlled.getLevel();
                break;
            }
        }
        if (gobgobLevel == -1) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 4) {
            final float incrementByGobGobLevel = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            final boolean percentOfCurrentValue = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
            final boolean upperRound = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
            final float valueCap = ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            final float baseValue = gobgobLevel / incrementByGobGobLevel;
            if (upperRound) {
                this.m_value = MathHelper.fastCeil(baseValue);
            }
            else {
                this.m_value = MathHelper.fastFloor(baseValue);
            }
            if (valueCap != -1.0f) {
                this.m_value = (int)Math.min(this.m_value, valueCap);
            }
            this.m_valuePerCentOfCurrentValue = percentOfCurrentValue;
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacGainForOsaFusion>() {
            @Override
            public CharacGainForOsaFusion makeObject() {
                return new CharacGainForOsaFusion();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("param\u00e8trage standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("pallier de niveau du gobgob", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("en % de la charac 1=oui 0=non", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("arrondi au sup\u00e9rieur 1=oui 0=non", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("cap du gain (-1 = pas de cap)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
