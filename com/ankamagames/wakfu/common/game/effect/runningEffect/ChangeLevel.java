package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ChangeLevel extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ChangeLevel.PARAMETERS_LIST_SET;
    }
    
    public ChangeLevel() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ChangeLevel newInstance() {
        ChangeLevel re;
        try {
            re = (ChangeLevel)ChangeLevel.m_staticPool.borrowObject();
            re.m_pool = ChangeLevel.m_staticPool;
        }
        catch (Exception e) {
            re = new ChangeLevel();
            re.m_pool = null;
            re.m_isStatic = false;
            ChangeLevel.m_logger.error((Object)("Erreur lors d'un checkOut sur un ChangeLevel : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_target == null || this.m_caster == null) {
            return;
        }
        if (this.m_target instanceof BasicCharacterInfo) {
            this.m_value = ((BasicCharacterInfo)this.m_target).getLevel();
        }
        else if (this.m_target instanceof AbstractEffectArea) {
            this.m_value = ((AbstractEffectArea)this.m_target).getLevel();
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_caster == null || !(this.m_caster instanceof BasicCharacterInfo) || ((BasicCharacterInfo)this.m_caster).getBreedId() != 1620 || this.m_value == 0) {
            this.setNotified();
            return;
        }
        final BasicCharacterInfo caster = (BasicCharacterInfo)this.m_caster;
        final AbstractMonsterBreed breed = (AbstractMonsterBreed)caster.getBreed();
        final short levelMax = breed.getLevelMax();
        final short currentLevel = caster.getLevel();
        if (currentLevel == levelMax) {
            this.setNotified();
            return;
        }
        final short controllerLevel = ((BasicCharacterInfo)this.m_caster).getOriginalController().getLevel();
        if (currentLevel >= controllerLevel) {
            this.setNotified();
            return;
        }
        final int previousHpMax = caster.getCharacteristicMax(FighterCharacteristicType.HP);
        final int previousHpValue = caster.getCharacteristicValue(FighterCharacteristicType.HP);
        final short newLevel = (short)Math.min(Math.min(currentLevel + this.m_value, levelMax), controllerLevel);
        caster.setLevel(newLevel);
        caster.onGobgobChangedLevel();
        final int newHpMax = caster.getCharacteristicMax(FighterCharacteristicType.HP);
        final int hpDiff = newHpMax - previousHpMax;
        final int newHpValue = previousHpValue + hpDiff;
        caster.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(newHpValue);
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ChangeLevel>() {
            @Override
            public ChangeLevel makeObject() {
                return new ChangeLevel();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
