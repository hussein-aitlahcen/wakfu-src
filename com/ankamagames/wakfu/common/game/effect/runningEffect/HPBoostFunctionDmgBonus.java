package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class HPBoostFunctionDmgBonus extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final int CASE_AIR_DMG = 1;
    private static final int CASE_EARTH_DMG = 2;
    private static final int CASE_FIRE_DMG = 3;
    private static final int CASE_WATER_DMG = 4;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    protected boolean m_addCurrentValue;
    protected boolean m_substractCurrentValue;
    protected int m_valueToAdd;
    private BinarSerialPart ADDITIONAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HPBoostFunctionDmgBonus.PARAMETERS_LIST_SET;
    }
    
    public HPBoostFunctionDmgBonus() {
        super();
        this.m_addCurrentValue = true;
        this.m_substractCurrentValue = true;
        this.ADDITIONAL_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(HPBoostFunctionDmgBonus.this.m_addCurrentValue ? 1 : 0));
                buffer.put((byte)(HPBoostFunctionDmgBonus.this.m_substractCurrentValue ? 1 : 0));
                buffer.putInt(HPBoostFunctionDmgBonus.this.m_valueToAdd);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                HPBoostFunctionDmgBonus.this.m_addCurrentValue = (buffer.get() == 1);
                HPBoostFunctionDmgBonus.this.m_substractCurrentValue = (buffer.get() == 1);
                HPBoostFunctionDmgBonus.this.m_valueToAdd = buffer.getInt();
            }
            
            @Override
            public int expectedSize() {
                return 6;
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public HPBoostFunctionDmgBonus newInstance() {
        HPBoostFunctionDmgBonus re;
        try {
            re = (HPBoostFunctionDmgBonus)HPBoostFunctionDmgBonus.m_staticPool.borrowObject();
            re.m_pool = HPBoostFunctionDmgBonus.m_staticPool;
        }
        catch (Exception e) {
            re = new HPBoostFunctionDmgBonus();
            re.m_isStatic = false;
            re.m_pool = null;
            HPBoostFunctionDmgBonus.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacBuff : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(3);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target == null || !this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            this.setNotified(true);
            return;
        }
        final AbstractCharacteristic hp = this.m_target.getCharacteristic(FighterCharacteristicType.HP);
        final int currentMax = hp.max();
        final int currentPlainMax = hp.plainMax();
        hp.updateMaxValue(this.m_value);
        if (this.isValueComputationEnabled()) {
            this.m_value = hp.plainMax() - currentPlainMax;
            this.m_valueToAdd = hp.max() - currentMax;
        }
        if (this.m_addCurrentValue) {
            hp.add(this.m_valueToAdd);
            this.m_executed = true;
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect != null) {
            final short level = this.getContainerLevel();
            final int dmgType = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
            final int valuePercent = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
            switch (dmgType) {
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
            if (((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM) == 0) {
                this.m_addCurrentValue = false;
            }
            if (((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.RANDOM) == 0) {
                this.m_substractCurrentValue = false;
            }
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_executed && this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            final AbstractCharacteristic hp = this.m_target.getCharacteristic(FighterCharacteristicType.HP);
            hp.substract((this.m_valueToAdd > hp.value()) ? (hp.value() - 1) : this.m_valueToAdd);
            hp.updateMaxValue(-this.m_value);
        }
        super.unapplyOverride();
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
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HPBoostFunctionDmgBonus>() {
            @Override
            public HPBoostFunctionDmgBonus makeObject() {
                return new HPBoostFunctionDmgBonus();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Boost de HP en fonction du bonus dmg d'un element", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (1 pour dmg air, 2 pour dmg terre, 3 pour dmg feu, 4 pour dmg eau )", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("% de la valeur \u00e0 ajouter", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Augmente la valeur courante (0 non 1 oui)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Diminue \u00e0 la d\u00e9sapplication (0 g\u00e9n\u00e9ralement. Si vous h\u00e9sitez, demandez ! Bug \u00e0 l'horizon)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
