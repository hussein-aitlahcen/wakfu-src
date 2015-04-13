package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.hpLoss.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class HPLoss extends WakfuRunningEffect implements ArmorLossProvider
{
    public static final int DMG_BOOST = 1;
    public static final int RESISTANCE = 2;
    public static final int DMG_REBOUND = 4;
    public static final int DMG_ABSORB = 8;
    public static final int FINAL_MOD = 16;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final ObjectPool m_staticPool;
    protected Elements m_staticElement;
    protected ComputeMode m_staticcomputeMode;
    protected int m_condition;
    private int m_resistPercentBonus;
    private int m_dmgPercentBonus;
    protected int m_armorLoss;
    protected int m_barrierLoss;
    private final BinarSerialPart m_additionalData;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HPLoss.PARAMETERS_LIST_SET;
    }
    
    protected HPLoss() {
        super();
        this.m_additionalData = new BinarSerialPart(8) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(HPLoss.this.m_armorLoss);
                buffer.putInt(HPLoss.this.m_barrierLoss);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                HPLoss.this.m_armorLoss = buffer.getInt();
                HPLoss.this.m_barrierLoss = buffer.getInt();
            }
        };
    }
    
    public HPLoss(final Elements element, final ComputeMode mode) {
        super();
        this.m_additionalData = new BinarSerialPart(8) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(HPLoss.this.m_armorLoss);
                buffer.putInt(HPLoss.this.m_barrierLoss);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                HPLoss.this.m_armorLoss = buffer.getInt();
                HPLoss.this.m_barrierLoss = buffer.getInt();
            }
        };
        this.m_staticElement = element;
        this.m_staticcomputeMode = mode;
        this.setTriggersToExecute();
    }
    
    public static HPLoss checkOut(final EffectContext<WakfuEffect> context, final Elements element, final ComputeMode mode, final int hpLost, final EffectUser target) {
        HPLoss re;
        try {
            re = (HPLoss)HPLoss.m_staticPool.borrowObject();
            re.m_pool = HPLoss.m_staticPool;
        }
        catch (Exception e) {
            re = new HPLoss(element, mode);
            re.m_pool = null;
            re.m_isStatic = false;
            HPLoss.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPLoss : " + e.getMessage()));
        }
        re.initializeHPLoss(context, element, mode, hpLost, target);
        return re;
    }
    
    protected void initializeHPLoss(final EffectContext<WakfuEffect> context, final Elements element, final ComputeMode mode, final int hpLost, final EffectUser target) {
        this.m_id = RunningEffectConstants.HP_LOSS.getId();
        this.m_status = RunningEffectConstants.HP_LOSS.getObject().getRunningEffectStatus();
        this.m_staticElement = element;
        this.m_staticcomputeMode = mode;
        this.setTriggersToExecute();
        this.m_target = target;
        this.m_value = hpLost;
        this.m_maxExecutionCount = -1;
        this.defaultCondition();
        this.m_context = (EffectContext<FX>)context;
        this.m_dmgPercentBonus = 0;
        this.m_resistPercentBonus = 0;
    }
    
    @Override
    public HPLoss newInstance() {
        HPLoss re;
        try {
            re = (HPLoss)HPLoss.m_staticPool.borrowObject();
            re.m_pool = HPLoss.m_staticPool;
        }
        catch (Exception e) {
            re = new HPLoss();
            re.m_pool = null;
            HPLoss.m_logger.error((Object)("Erreur lors d'un newInstance sur un HPLoss : " + e.getMessage()));
        }
        this.copyParams(re);
        return re;
    }
    
    protected void copyParams(final HPLoss re) {
        re.m_armorLoss = this.m_armorLoss;
        re.m_barrierLoss = this.m_barrierLoss;
        re.m_staticElement = this.m_staticElement;
        re.m_staticcomputeMode = this.m_staticcomputeMode;
    }
    
    @Override
    public void release() {
        super.release();
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(2);
        final Elements currentElement = this.getElement();
        RunningEffectUtils.setTriggerForElement(currentElement, this);
        if (this.hasProperty(RunningEffectPropertyType.ZONE_EFFECT)) {
            this.m_triggers.set(2215);
        }
        if (this.hasProperty(RunningEffectPropertyType.MELEE_EFFECT)) {
            this.m_triggers.set(2218);
        }
        if (this.hasProperty(RunningEffectPropertyType.RANGED_EFFECT)) {
            this.m_triggers.set(2217);
        }
        if (this.hasProperty(RunningEffectPropertyType.SINGLE_TARGET_EFFECT)) {
            this.m_triggers.set(2216);
        }
    }
    
    protected void addCustomTriggersToExecute(final BitSet triggers) {
        if (triggers != null) {
            this.m_triggers.or(triggers);
        }
    }
    
    @Override
    public void update(final int whatToUpdate, final float howMuchToUpate, final boolean set) {
        super.update(whatToUpdate, howMuchToUpate, set);
        switch (whatToUpdate) {
            case 0: {
                if (!set) {
                    this.m_value += ValueRounder.randomRound(this.m_value * howMuchToUpate / 100.0f);
                    break;
                }
                this.m_value = ValueRounder.randomRound(this.m_value * howMuchToUpate / 100.0f);
                break;
            }
            case 1: {
                if (set) {
                    this.m_value = ValueRounder.randomRound(howMuchToUpate);
                    break;
                }
                this.m_value += (int)howMuchToUpate;
                break;
            }
            case 6: {
                this.m_dmgPercentBonus = ValueRounder.randomRound(howMuchToUpate);
                break;
            }
            case 5: {
                this.m_resistPercentBonus = ValueRounder.randomRound(howMuchToUpate);
                break;
            }
        }
        this.m_value = Math.max(0, this.m_value);
    }
    
    @Override
    public boolean canBeExecuted() {
        return super.canBeExecuted() && (!this.getTriggersToExecute().get(2010) || this.m_target == null || this.m_target.hasCharacteristic(FighterCharacteristicType.HP) || this.m_target.hasCharacteristic(FighterCharacteristicType.AREA_HP) || this.m_target.hasCharacteristic(FighterCharacteristicType.BOMB_COOLDOWN));
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        int rebound = 0;
        if (this.isValueComputationEnabled()) {
            this.dmgAbsorb();
            rebound = this.computeRebound();
        }
        if (this.isValueComputationEnabled() && this.m_target != null && this.m_caster != null && (this.m_genericEffect == null || !((WakfuEffect)this.m_genericEffect).hasProperty(RunningEffectPropertyType.DONT_USE_BLOCK))) {
            final AbstractCharacteristic blockCharac = this.m_target.getCharacteristic(FighterCharacteristicType.BLOCK);
            if (blockCharac != null) {
                final int blockValue = blockCharac.value();
                if (blockValue > 0 && DiceRoll.roll(100) <= blockValue) {
                    this.m_value = (int)Math.ceil(this.m_value * 0.7f);
                    this.setExecutionStatus((byte)7);
                    if (this.getContext() instanceof WakfuFightEffectContext) {
                        final WakfuFightEffectContext context = (WakfuFightEffectContext)this.getContext();
                        context.onBlockAchieved(this.m_target);
                    }
                }
            }
        }
        if (this.m_value < 0) {
            this.m_value = 0;
        }
        if (this.m_target != null && this.m_value >= 0) {
            if (!this.m_target.isOffPlay()) {
                if (this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
                    this.substractHp();
                }
                else {
                    this.setNotified();
                }
            }
            else if (this.m_target.hasCharacteristic(FighterCharacteristicType.KO_TIME_BEFORE_DEATH)) {
                this.m_value = 1;
                this.m_target.getCharacteristic(FighterCharacteristicType.KO_TIME_BEFORE_DEATH).substract(this.m_value);
            }
            else {
                this.setNotified();
            }
            this.setBackstabTriggerIfNecessary();
            if (this.isValueComputationEnabled() && rebound > 0 && ((this.m_target.hasCharacteristic(FighterCharacteristicType.HP) && !this.m_target.getCharacteristic(FighterCharacteristicType.HP).isZero()) || (this.m_target.hasCharacteristic(FighterCharacteristicType.AREA_HP) && !this.m_target.getCharacteristic(FighterCharacteristicType.AREA_HP).isZero()))) {
                this.notifyExecution(linkedRE, trigger);
                this.executeRebound(rebound);
            }
        }
        else {
            this.setNotified(true);
        }
    }
    
    private void substractHp() {
        this.substractHp(this.m_value, this.m_caster, this.m_target);
    }
    
    public void substractHp(int value, final EffectUser caster, final EffectUser target) {
        final AbstractCharacteristic barrier = caster.getCharacteristic(FighterCharacteristicType.BARRIER);
        if (barrier != null && barrier.max() > 0 && barrier.isNonZero()) {
            final int reallyRemove = barrier.substract(value / 2);
            value -= reallyRemove;
            if (this.isValueComputationEnabled()) {
                this.m_barrierLoss = reallyRemove;
            }
        }
        final AbstractCharacteristic armor = target.getCharacteristic(FighterCharacteristicType.ARMOR);
        if (armor != null && armor.max() > 0 && armor.isNonZero()) {
            final int reallyRemove2 = armor.substract(value);
            value -= reallyRemove2;
            if (this.isValueComputationEnabled()) {
                this.m_armorLoss = reallyRemove2;
            }
        }
        final AbstractCharacteristic virtualHp = target.getCharacteristic(FighterCharacteristicType.VIRTUAL_HP);
        if (virtualHp != null && virtualHp.max() > 0 && virtualHp.isNonZero()) {
            final int reallyRemove3 = virtualHp.substract(value);
            value -= reallyRemove3;
        }
        if (value > 0) {
            target.getCharacteristic(FighterCharacteristicType.HP).substract(value);
        }
    }
    
    private void setBackstabTriggerIfNecessary() {
        final PartLocalisator partLocalisator = this.m_target.getPartLocalisator();
        if (partLocalisator == null || this.m_caster == null) {
            return;
        }
        final Part partInSight = partLocalisator.getMainPartInSightFromPosition(this.m_caster.getWorldCellX(), this.m_caster.getWorldCellY(), this.m_caster.getWorldCellAltitude());
        if (partInSight.getPartId() == 2) {
            this.m_triggers.set(29);
        }
    }
    
    private void dmgAbsorb() {
        if ((this.m_condition & 0x8) == 0x0) {
            return;
        }
        if (this.m_target.hasCharacteristic(FighterCharacteristicType.DMG_ABSORB) && !this.m_target.isActiveProperty(FightPropertyType.UNDEAD)) {
            final int absorbRate = this.m_target.getCharacteristicValue(FighterCharacteristicType.DMG_ABSORB);
            final int absorbValue = ValueRounder.randomRound(this.m_value * absorbRate / 100.0f);
            if (absorbValue > 0) {
                if (this.m_target != null && this.m_caster != this.m_target) {
                    final WakfuRunningEffect hpgain = HPGain.checkOut((EffectContext<WakfuEffect>)this.m_context, this.getElement());
                    hpgain.setExecutionStatus((byte)4);
                    hpgain.forceValue(absorbValue);
                    ((RunningEffect<WakfuEffect, EC>)hpgain).setGenericEffect(((RunningEffect<WakfuEffect, EC>)this).getGenericEffect());
                    hpgain.setTarget(this.m_target);
                    hpgain.trigger((byte)1);
                    hpgain.askForExecution();
                }
                this.m_value -= absorbValue;
            }
        }
    }
    
    private int computeRebound() {
        if ((this.m_condition & 0x4) == 0x0) {
            return 0;
        }
        if (!this.m_target.hasCharacteristic(FighterCharacteristicType.DMG_REBOUND) || this.m_caster.isActiveProperty(WorldPropertyType.ULTIMATE_BOSS)) {
            return 0;
        }
        final int reboundRES = this.m_target.getCharacteristicValue(FighterCharacteristicType.DMG_REBOUND);
        final int rebound = ValueRounder.randomRound(this.m_value * reboundRES / 100.0f);
        if (rebound > 0) {
            this.m_value -= rebound;
        }
        return rebound;
    }
    
    private void executeRebound(final int rebound) {
        if (this.m_caster != null && this.m_caster != this.m_target) {
            final HPLoss reboundRE = (HPLoss)this.newParameterizedInstance();
            if (((WakfuEffect)this.m_genericEffect).notifyInChat()) {
                ((RunningEffect<DefaultFightInstantEffectWithChatNotif, EC>)reboundRE).setGenericEffect(DefaultFightInstantEffectWithChatNotif.getInstance());
            }
            else {
                ((RunningEffect<DefaultFightInstantEffectAndDontTrigger, EC>)reboundRE).setGenericEffect(DefaultFightInstantEffectAndDontTrigger.getInstance());
            }
            reboundRE.setCaster(this.m_target);
            reboundRE.forceValue(rebound);
            reboundRE.forceDontTriggerAnything();
            reboundRE.disableValueComputation();
            reboundRE.m_condition = 0;
            reboundRE.applyOnTargets(this.m_caster);
            reboundRE.release();
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (!this.extractParamsAndComputeBaseValue(triggerRE)) {
            return;
        }
        this.computeModificator(this.m_condition);
    }
    
    private boolean extractParamsAndComputeBaseValue(final RunningEffect triggerRE) {
        if (this.m_target == null) {
            return false;
        }
        if (this.m_target.isOffPlay()) {
            this.m_value = 1;
            return false;
        }
        if (this.m_target instanceof BasicEffectArea && this.m_target.hasCharacteristic(FighterCharacteristicType.AREA_HP) && !this.m_target.isActiveProperty(EffectAreaPropertyType.NORMAL_HP_LOSS)) {
            this.m_value = 0;
            return false;
        }
        if (!this.m_target.hasCharacteristic(FighterCharacteristicType.HP) && !this.m_target.hasCharacteristic(FighterCharacteristicType.AREA_HP)) {
            return false;
        }
        this.extractParams(triggerRE);
        this.computeBaseValue();
        return true;
    }
    
    public void computeBaseValue() {
        switch (this.m_staticcomputeMode) {
            case PERCENT_OF_MAX_CASTER_HP: {
                if (this.m_caster != null) {
                    this.m_value = ValueRounder.randomRound(getHpMax(this.m_caster) * this.m_value / 100.0f);
                    break;
                }
                this.m_value = 0;
                break;
            }
            case PERCENT_OF_MAX_CASTER_HP_WITH_VIRTUAL: {
                if (this.m_caster != null) {
                    this.m_value = ValueRounder.randomRound(getHpMaxWithVirtual(this.m_caster) * this.m_value / 100.0f);
                    break;
                }
                this.m_value = 0;
                break;
            }
            case PERCENT_OF_CURRENT_CASTER_HP: {
                if (this.m_caster != null) {
                    final EffectUser ref = this.m_caster;
                    this.m_value = ValueRounder.randomRound(getHpValue(ref) * this.m_value / 100.0f);
                    break;
                }
                this.m_value = 0;
                break;
            }
            case PERCENT_OF_MAX_TARGET_HP: {
                if (this.m_target != null) {
                    this.m_value = ValueRounder.randomRound(getHpMax(this.m_target) * this.m_value / 100.0f);
                    break;
                }
                this.m_value = 0;
                break;
            }
            case PERCENT_OF_MAX_TARGET_HP_WITH_VIRTUAL: {
                if (this.m_target != null) {
                    this.m_value = ValueRounder.randomRound(getHpMaxWithVirtual(this.m_target) * this.m_value / 100.0f);
                    break;
                }
                this.m_value = 0;
                break;
            }
            case PERCENT_OF_CURRENT_TARGET_HP: {
                if (this.m_target != null) {
                    this.m_value = ValueRounder.randomRound(getHpValue(this.m_target) * this.m_value / 100.0f);
                    break;
                }
                this.m_value = 0;
                break;
            }
            case PERCENT_OF_CURRENT_CASTER_NEGATIVE_HP: {
                if (this.m_caster != null) {
                    this.m_value = ValueRounder.randomRound((getHpMax(this.m_caster) - getHpValue(this.m_caster)) * this.m_value / 100.0f);
                    break;
                }
                this.m_value = 0;
                break;
            }
            case PERCENT_OF_CURRENT_TARGET_NEGATIVE_HP: {
                if (this.m_target != null) {
                    this.m_value = ValueRounder.randomRound((getHpMax(this.m_target) - getHpValue(this.m_target)) * this.m_value / 100.0f);
                    break;
                }
                this.m_value = 0;
                break;
            }
        }
    }
    
    private static int getHpValue(final EffectUser ref) {
        return ref.getCharacteristicValue(FighterCharacteristicType.HP);
    }
    
    private static int getHpValueWithVirtual(final EffectUser ref) {
        int hpValue = ref.getCharacteristicValue(FighterCharacteristicType.HP);
        if (ref.hasCharacteristic(FighterCharacteristicType.VIRTUAL_HP)) {
            hpValue += ref.getCharacteristicValue(FighterCharacteristicType.VIRTUAL_HP);
        }
        return hpValue;
    }
    
    private static int getHpMax(final EffectUser reference) {
        return reference.getCharacteristic(FighterCharacteristicType.HP).max();
    }
    
    private static int getHpMaxWithVirtual(final EffectUser reference) {
        int hpMax = reference.getCharacteristic(FighterCharacteristicType.HP).max();
        if (reference.hasCharacteristic(FighterCharacteristicType.VIRTUAL_HP)) {
            hpMax += reference.getCharacteristic(FighterCharacteristicType.VIRTUAL_HP).max();
        }
        return hpMax;
    }
    
    protected void extractParams(final RunningEffect triggerRE) {
        this.defaultCondition();
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            switch (((WakfuEffect)this.m_genericEffect).getParamsCount()) {
                case 1: {
                    this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                    break;
                }
                case 2: {
                    this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                    this.m_condition = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                    break;
                }
                case 3: {
                    this.m_value = DiceRoll.roll(((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL), ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL), ((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL));
                    break;
                }
                case 4: {
                    this.m_value = DiceRoll.roll(((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL), ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL), ((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL));
                    this.m_condition = ((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                    break;
                }
                default: {
                    HPLoss.m_logger.error((Object)("Nombre de param\u00e8tres incorrect dans un HPLoss : " + ((WakfuEffect)this.m_genericEffect).getParamsCount()));
                    this.m_value = 0;
                    break;
                }
            }
        }
    }
    
    protected int defaultCondition() {
        return this.m_condition = 31;
    }
    
    public void computeModificatorWithDefaults() {
        final int condition = 15;
        this.computeModificator(15);
    }
    
    public void computeModificator(final int conditions) {
        this.computeModificator(conditions, this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).checkFlags(1L), this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).isAffectedByLocalisation());
    }
    
    public void computeModificator(final int conditions, final boolean critical, final boolean localised) {
        Item weapon = null;
        if (this.m_effectContainer != null && ((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 12) {
            weapon = (Item)this.m_effectContainer;
        }
        final HpLossComputer computer = new HpLossComputerImpl(this.m_caster, this.m_target, this.m_staticElement, (WakfuEffect)this.m_genericEffect);
        computer.setValue(this.m_value);
        computer.setConditions(conditions);
        computer.setAffectedByLocalisation(localised);
        computer.setWeaponUsed(weapon);
        computer.setIsCritical(critical);
        computer.setCustomResistPercentBonus(this.m_resistPercentBonus);
        computer.setCustomDmgPercentBonus(this.m_dmgPercentBonus);
        computer.computeWithModificator();
        RunningEffectUtils.setTriggerForElement(computer.getElementForResistance(), this);
        this.m_value = computer.getValue();
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
    protected boolean canBeExecutedOnKO() {
        return true;
    }
    
    @Override
    public Elements getElement() {
        Elements elements = this.m_staticElement;
        if (this.m_effectContainer != null && ((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 12 && this.m_target != null) {
            byte maxValue = 0;
            byte value = this.m_target.getPropertyValue(ItemPropertyType.CHANGE_DAMAGE_ELEMENT_PHYSICAL);
            if (value > maxValue) {
                maxValue = value;
                elements = Elements.PHYSICAL;
            }
            value = this.m_target.getPropertyValue(ItemPropertyType.CHANGE_DAMAGE_ELEMENT_FIRE);
            if (value > maxValue) {
                maxValue = value;
                elements = Elements.FIRE;
            }
            value = this.m_target.getPropertyValue(ItemPropertyType.CHANGE_DAMAGE_ELEMENT_WATER);
            if (value > maxValue) {
                maxValue = value;
                elements = Elements.WATER;
            }
            value = this.m_target.getPropertyValue(ItemPropertyType.CHANGE_DAMAGE_ELEMENT_EARTH);
            if (value > maxValue) {
                maxValue = value;
                elements = Elements.EARTH;
            }
            value = this.m_target.getPropertyValue(ItemPropertyType.CHANGE_DAMAGE_ELEMENT_AIR);
            if (value > maxValue) {
                maxValue = value;
                elements = Elements.AIR;
            }
            value = this.m_target.getPropertyValue(ItemPropertyType.CHANGE_DAMAGE_ELEMENT_STASIS);
            if (value > maxValue) {
                maxValue = value;
                elements = Elements.STASIS;
            }
        }
        return elements;
    }
    
    @Override
    protected boolean checkIsNotValidTargetProperty() {
        return this.isNotValidTargetAndContainerNotSpecialState();
    }
    
    @Override
    public int getArmorLoss() {
        return this.m_armorLoss;
    }
    
    @Override
    public int getBarrierLoss() {
        return this.m_barrierLoss;
    }
    
    public void setCondition(final int condition) {
        this.m_condition = condition;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.m_additionalData;
    }
    
    @Override
    public void onCheckIn() {
        this.m_resistPercentBonus = 0;
        this.m_dmgPercentBonus = 0;
        this.m_armorLoss = 0;
        this.m_barrierLoss = 0;
        super.onCheckIn();
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Dommages fixes", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Dmg ou valeur %", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Dommages fixes + modificateur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Dmg ou valeur %", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("mod : boost(1) / res (2) / rebound (4) / absorb(8) ", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Dommages variables", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("+", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Dommages variables", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("+", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("mod : boost(1) / res (2) / rebound (4) / absorb(8) ", WakfuRunningEffectParameterType.CONFIG) }) });
        m_staticPool = new MonitoredPool(new ObjectFactory<HPLoss>() {
            @Override
            public HPLoss makeObject() {
                return new HPLoss();
            }
        });
    }
    
    public enum ComputeMode
    {
        CLASSIC, 
        PERCENT_OF_MAX_CASTER_HP, 
        PERCENT_OF_MAX_CASTER_HP_WITH_VIRTUAL, 
        PERCENT_OF_CURRENT_CASTER_HP, 
        PERCENT_OF_MAX_TARGET_HP, 
        PERCENT_OF_MAX_TARGET_HP_WITH_VIRTUAL, 
        PERCENT_OF_CURRENT_TARGET_HP, 
        PERCENT_OF_CURRENT_CASTER_NEGATIVE_HP, 
        PERCENT_OF_CURRENT_TARGET_NEGATIVE_HP;
    }
}
