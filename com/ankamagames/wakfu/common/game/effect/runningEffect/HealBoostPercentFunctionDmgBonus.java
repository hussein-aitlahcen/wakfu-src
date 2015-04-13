package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class HealBoostPercentFunctionDmgBonus extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final int CASE_AIR_DMG = 1;
    private static final int CASE_EARTH_DMG = 2;
    private static final int CASE_FIRE_DMG = 3;
    private static final int CASE_WATER_DMG = 4;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_valueDiff;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HealBoostPercentFunctionDmgBonus.PARAMETERS_LIST_SET;
    }
    
    public HealBoostPercentFunctionDmgBonus() {
        super();
        this.m_valueDiff = 0;
        this.setTriggersToExecute();
    }
    
    @Override
    public HealBoostPercentFunctionDmgBonus newInstance() {
        HealBoostPercentFunctionDmgBonus re;
        try {
            re = (HealBoostPercentFunctionDmgBonus)HealBoostPercentFunctionDmgBonus.m_staticPool.borrowObject();
            re.m_pool = HealBoostPercentFunctionDmgBonus.m_staticPool;
        }
        catch (Exception e) {
            re = new HealBoostPercentFunctionDmgBonus();
            re.m_pool = null;
            HealBoostPercentFunctionDmgBonus.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacGain : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.HEAL_IN_PERCENT)) {
            final AbstractCharacteristic charac = this.m_target.getCharacteristic(FighterCharacteristicType.HEAL_IN_PERCENT);
            final int value = charac.value();
            charac.add(this.m_value);
            this.m_valueDiff = charac.value() - value;
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.getCaster() == null) {
            this.m_value = 0;
            return;
        }
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() > 0) {
            final int valueType = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
            final int valuePercent = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
            switch (valueType) {
                case 1: {
                    if (this.getCaster().hasCharacteristic(FighterCharacteristicType.DMG_AIR_PERCENT)) {
                        this.m_value = this.getCaster().getCharacteristicValue(FighterCharacteristicType.DMG_AIR_PERCENT);
                    }
                    if (this.getCaster().hasCharacteristic(FighterCharacteristicType.AIR_MASTERY)) {
                        this.m_value += this.getCaster().getCharacteristicValue(FighterCharacteristicType.AIR_MASTERY);
                        break;
                    }
                    break;
                }
                case 2: {
                    if (this.getCaster().hasCharacteristic(FighterCharacteristicType.DMG_EARTH_PERCENT)) {
                        this.m_value = this.getCaster().getCharacteristicValue(FighterCharacteristicType.DMG_EARTH_PERCENT);
                    }
                    if (this.getCaster().hasCharacteristic(FighterCharacteristicType.EARTH_MASTERY)) {
                        this.m_value += this.getCaster().getCharacteristicValue(FighterCharacteristicType.EARTH_MASTERY);
                        break;
                    }
                    break;
                }
                case 3: {
                    if (this.getCaster().hasCharacteristic(FighterCharacteristicType.DMG_FIRE_PERCENT)) {
                        this.m_value = this.getCaster().getCharacteristicValue(FighterCharacteristicType.DMG_FIRE_PERCENT);
                    }
                    if (this.getCaster().hasCharacteristic(FighterCharacteristicType.FIRE_MASTERY)) {
                        this.m_value += this.getCaster().getCharacteristicValue(FighterCharacteristicType.FIRE_MASTERY);
                        break;
                    }
                    break;
                }
                case 4: {
                    if (this.getCaster().hasCharacteristic(FighterCharacteristicType.DMG_WATER_PERCENT)) {
                        this.m_value = this.getCaster().getCharacteristicValue(FighterCharacteristicType.DMG_WATER_PERCENT);
                    }
                    if (this.getCaster().hasCharacteristic(FighterCharacteristicType.WATER_MASTERY)) {
                        this.m_value += this.getCaster().getCharacteristicValue(FighterCharacteristicType.WATER_MASTERY);
                        break;
                    }
                    break;
                }
                default: {
                    this.m_value = 0;
                    break;
                }
            }
            if (this.getCaster().hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
                this.m_value += this.getCaster().getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT);
            }
            this.m_value = this.m_value * valuePercent / 100;
        }
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
    public void unapplyOverride() {
        if (this.m_executed && this.hasDuration() && this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.HEAL_IN_PERCENT)) {
            final AbstractCharacteristic characteristic = this.m_target.getCharacteristic(FighterCharacteristicType.HEAL_IN_PERCENT);
            final int value = characteristic.value() - this.m_valueDiff;
            characteristic.set(value);
        }
        super.unapplyOverride();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HealBoostPercentFunctionDmgBonus>() {
            @Override
            public HealBoostPercentFunctionDmgBonus makeObject() {
                return new HealBoostPercentFunctionDmgBonus();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Boost bonus soin en fonction d'une carac dommage", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (1 pour dmg air, 2 pour dmg terre, 3 pour dmg feu, 4 pour dmg eau )", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("% de la valeur \u00e0 ajouter", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
