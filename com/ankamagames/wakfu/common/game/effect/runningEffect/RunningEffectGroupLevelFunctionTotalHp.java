package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class RunningEffectGroupLevelFunctionTotalHp extends RunningEffectGroupLevelFunctionCharacteristic
{
    private static final ObjectPool m_staticPool;
    
    public RunningEffectGroupLevelFunctionTotalHp() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupLevelFunctionTotalHp newInstance() {
        RunningEffectGroupLevelFunctionTotalHp re;
        try {
            re = (RunningEffectGroupLevelFunctionTotalHp)RunningEffectGroupLevelFunctionTotalHp.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupLevelFunctionTotalHp.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupLevelFunctionTotalHp();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupLevelFunctionTotalHp.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupLevelFunctionTotalHp : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = super.getExecutionParameters(linkedRE, disableProbabilityComputation);
        EffectUser characOwner;
        if (this.m_checkOnCaster) {
            characOwner = this.m_caster;
        }
        else {
            characOwner = this.m_target;
        }
        if (characOwner == null) {
            return params;
        }
        final AbstractCharacteristic hp = characOwner.getCharacteristic(FighterCharacteristicType.HP);
        final AbstractCharacteristic virtualHp = characOwner.getCharacteristic(FighterCharacteristicType.VIRTUAL_HP);
        int max = 0;
        int value = 0;
        if (hp != null) {
            max = hp.max();
            value = hp.value();
        }
        if (virtualHp != null) {
            max += virtualHp.max();
            value += virtualHp.value();
        }
        int characValue;
        if (hp != null || virtualHp != null) {
            if (this.m_valueToUse == 1) {
                characValue = max;
            }
            else if (this.m_valueToUse == 2) {
                characValue = value * 100 / max;
            }
            else if (this.m_valueToUse == 3) {
                characValue = (max - value) * 100 / max;
            }
            else if (this.m_valueToUse == 4) {
                characValue = max - value;
            }
            else {
                characValue = value;
            }
        }
        else {
            characValue = 0;
        }
        int forcedLevel;
        if (this.m_basedOnStep) {
            if (this.m_characStep == 0) {
                forcedLevel = characValue;
            }
            else {
                forcedLevel = (int)Math.floor(characValue / this.m_characStep);
            }
        }
        else {
            forcedLevel = Math.round(this.m_levelBase + this.m_levelIncrement * characValue);
        }
        params.setForcedLevel(forcedLevel);
        return params;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupLevelFunctionTotalHp>() {
            @Override
            public RunningEffectGroupLevelFunctionTotalHp makeObject() {
                return new RunningEffectGroupLevelFunctionTotalHp();
            }
        });
    }
}
