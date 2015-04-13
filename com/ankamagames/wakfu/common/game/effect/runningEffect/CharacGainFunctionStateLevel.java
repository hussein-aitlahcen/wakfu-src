package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CharacGainFunctionStateLevel extends CharacGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_applyOnCaster;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacGainFunctionStateLevel.PARAMETERS_LIST_SET;
    }
    
    public CharacGainFunctionStateLevel() {
        super();
        this.m_applyOnCaster = false;
        this.setTriggersToExecute();
    }
    
    public CharacGainFunctionStateLevel(final CharacteristicType charac) {
        super(charac);
        this.m_applyOnCaster = false;
    }
    
    @Override
    public CharacGainFunctionStateLevel newInstance() {
        CharacGainFunctionStateLevel re;
        try {
            re = (CharacGainFunctionStateLevel)CharacGainFunctionStateLevel.m_staticPool.borrowObject();
            re.m_pool = CharacGainFunctionStateLevel.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacGainFunctionStateLevel();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacGainFunctionStateLevel.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacGainFunctionStateLevel : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        re.m_applyOnCaster = false;
        return re;
    }
    
    @Override
    protected EffectUser getEffectExecutionTarget() {
        return this.m_applyOnCaster ? this.m_caster : this.m_target;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = 0;
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() != 5) {
            return;
        }
        final boolean m_applyOnCaster = 0 != ((WakfuEffect)this.m_genericEffect).getParam(4, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final EffectUser gainTarget = m_applyOnCaster ? this.m_caster : this.m_target;
        if (gainTarget == null) {
            CharacGainFunctionStateLevel.m_logger.error((Object)("Unable to compute value for a null target for the gain ! applyOnCaster:" + m_applyOnCaster + " effect_id=" + ((WakfuEffect)this.m_genericEffect).getEffectId()));
            return;
        }
        final float valuePerStateLevelIncrement = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel());
        final float stateLevelIncrement = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel());
        if (stateLevelIncrement == 0.0f) {
            CharacGainFunctionStateLevel.m_logger.error((Object)("Increment can't be 0 in CharacGainFucntionStateLevel ! effect_id=" + ((WakfuEffect)this.m_genericEffect).getEffectId()), (Throwable)new Exception());
            return;
        }
        final float valuePerStateLevel = valuePerStateLevelIncrement / stateLevelIncrement;
        final boolean testOnTarget = 0 == ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final EffectUser targetToTest = testOnTarget ? this.m_target : this.m_caster;
        if (targetToTest == null) {
            CharacGainFunctionStateLevel.m_logger.error((Object)("Unable to compute value for a null target for the gain ! effect_id=" + ((WakfuEffect)this.m_genericEffect).getEffectId()));
            return;
        }
        final int stateId = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final RunningEffectManager rem = targetToTest.getRunningEffectManager();
        if (rem == null || !(rem instanceof TimedRunningEffectManager)) {
            CharacGainFunctionStateLevel.m_logger.error((Object)("Unable to compute value for a target with an invalide REM. Target : " + this.m_target + " REM : " + rem + " effect_id:" + ((WakfuEffect)this.m_genericEffect).getEffectId()));
            return;
        }
        final TimedRunningEffectManager runningEffectManager = (TimedRunningEffectManager)rem;
        final StateRunningEffect runningState = runningEffectManager.getRunningState(stateId);
        if (runningState == null) {
            return;
        }
        this.m_value = ValueRounder.randomRound(runningState.getState().getLevel() * valuePerStateLevel);
        this.changeTargetByCasterIfNecessary(m_applyOnCaster);
    }
    
    private void changeTargetByCasterIfNecessary(final boolean m_applyOnCaster) {
        if (m_applyOnCaster) {
            this.m_target = this.m_caster;
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacGainFunctionStateLevel>() {
            @Override
            public CharacGainFunctionStateLevel makeObject() {
                return new CharacGainFunctionStateLevel();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Gain de Charac fn du level d'un \u00e9tat (+ x / y niveaux)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur de x", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("valeur de y", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("id de l'\u00e9tat", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("\u00e9tat test\u00e9 sur : cible(0) ou caster(1)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("gain appliqu\u00e9 sur : cible(0) ou caster(1)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
