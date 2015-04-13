package com.ankamagames.wakfu.common.game.effect.runningEffect;

import cern.jet.random.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.hpLoss.*;
import cern.jet.random.engine.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class HPGain extends WakfuRunningEffect
{
    private static final Normal RANDOM;
    private static final BitSet EMPTY_BIT_SET;
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_fixedValue;
    private boolean m_useBonusDamage;
    private boolean m_useHealBonus;
    private boolean m_useHealResistance;
    protected Elements m_element;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HPGain.PARAMETERS_LIST_SET;
    }
    
    protected HPGain() {
        super();
        this.m_useHealBonus = true;
        this.m_useHealResistance = true;
    }
    
    public HPGain(final Elements element) {
        super();
        this.m_useHealBonus = true;
        this.m_useHealResistance = true;
        this.m_element = element;
        this.setTriggersToExecute();
    }
    
    @Override
    public Elements getElement() {
        return this.m_element;
    }
    
    public static HPGain checkOut(final EffectContext<WakfuEffect> context, final Elements element) {
        HPGain re;
        try {
            re = (HPGain)HPGain.m_staticPool.borrowObject();
            re.m_pool = HPGain.m_staticPool;
        }
        catch (Exception e) {
            re = new HPGain();
            re.m_pool = null;
            re.m_isStatic = false;
            HPGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPGain : " + e.getMessage()));
        }
        re.m_element = element;
        re.m_id = RunningEffectConstants.HP_GAIN.getId();
        re.m_status = RunningEffectConstants.HP_GAIN.getObject().getRunningEffectStatus();
        re.m_maxExecutionCount = -1;
        re.m_context = (EffectContext<FX>)context;
        re.setTriggersToExecute();
        return re;
    }
    
    @Override
    public HPGain newInstance() {
        HPGain re;
        try {
            re = (HPGain)HPGain.m_staticPool.borrowObject();
            re.m_pool = HPGain.m_staticPool;
        }
        catch (Exception e) {
            re = new HPGain();
            re.m_pool = null;
            re.m_isStatic = false;
            HPGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un HPGain : " + e.getMessage()));
        }
        re.m_element = this.m_element;
        return re;
    }
    
    @Override
    public void release() {
        super.release();
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(1);
    }
    
    @Override
    public void update(final int whatToUpdate, final float howMuchToUpate, final boolean set) {
        super.update(whatToUpdate, howMuchToUpate, set);
        switch (whatToUpdate) {
            case 0: {
                if (!set) {
                    this.m_value += (int)(this.m_value * howMuchToUpate / 100.0f);
                    break;
                }
                this.m_value = ValueRounder.randomRound(this.m_value * howMuchToUpate / 100.0f);
                break;
            }
            case 1: {
                if (!set) {
                    this.m_value += (int)howMuchToUpate;
                    break;
                }
                this.m_value = ValueRounder.randomRound(howMuchToUpate);
                break;
            }
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (!this.isAuthorizedTarget()) {
            this.setNotified();
            return;
        }
        this.executeOnAuthorizedTarget();
    }
    
    protected void executeOnAuthorizedTarget() {
        if (this.m_target == null || !this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            this.setNotified(true);
            return;
        }
        if (!this.m_target.isActiveProperty(FightPropertyType.UNDEAD)) {
            this.addHp();
        }
        else {
            final int hpLossValue = this.m_value;
            final HPLoss hpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, this.getElement(), HPLoss.ComputeMode.CLASSIC, hpLossValue, this.m_target);
            hpLoss.setExecutionParameters(WakfuEffectExecutionParameters.checkOut(true, true, (WakfuRunningEffect)this.getParent()));
            this.setAppropriateGenericEffect(hpLoss);
            hpLoss.setParent(this);
            hpLoss.setCaster(this.m_caster);
            hpLoss.askForExecution();
            this.setNotified(true);
        }
    }
    
    private void setAppropriateGenericEffect(final HPLoss hpLoss) {
        if (((WakfuEffect)this.m_genericEffect).notifyInChat()) {
            if (((WakfuEffect)this.m_genericEffect).dontTriggerAnything()) {
                ((RunningEffect<WakfuStandardEffect, EC>)hpLoss).setGenericEffect(DefaultEffectManager.getInstance().getDefaultEffect(-11));
            }
            else {
                ((RunningEffect<WakfuStandardEffect, EC>)hpLoss).setGenericEffect(DefaultEffectManager.getInstance().getDefaultEffect(-3));
            }
        }
        else if (((WakfuEffect)this.m_genericEffect).dontTriggerAnything()) {
            ((RunningEffect<WakfuStandardEffect, EC>)hpLoss).setGenericEffect(DefaultEffectManager.getInstance().getDefaultEffect(-8));
        }
        else {
            ((RunningEffect<WakfuStandardEffect, EC>)hpLoss).setGenericEffect(DefaultEffectManager.getInstance().getDefaultEffect(-12));
        }
    }
    
    protected void addHp() {
        if (this.m_target instanceof BasicCharacterInfo && (this.isValueComputationEnabled() || this.getTriggersToExecute().get(2138))) {
            this.m_value = ((BasicCharacterInfo)this.m_target).getReducedHealValue(this.m_value);
        }
        final AbstractCharacteristic charac = this.m_target.getCharacteristic(FighterCharacteristicType.HP);
        final int value = this.m_value;
        int remaining = value - charac.add(value);
        final AbstractCharacteristic virtualHp = this.m_target.getCharacteristic(FighterCharacteristicType.VIRTUAL_HP);
        if (virtualHp != null && virtualHp.max() > 0) {
            final int reallyAdd = virtualHp.add(remaining);
            remaining -= reallyAdd;
        }
        this.m_value -= remaining;
        if (this.m_target instanceof BasicCharacterInfo) {
            ((BasicCharacterInfo)this.m_target).onHeal(this.m_value);
        }
    }
    
    private boolean isAuthorizedTarget() {
        if (this.m_target == null) {
            return false;
        }
        if (this.m_target.getEffectUserType() == 2) {
            final int type = ((BasicEffectArea)this.m_target).getType();
            if (type == EffectAreaType.BOMB.getTypeId() || type == EffectAreaType.BARREL.getTypeId() || type == EffectAreaType.FECA_GLYPH.getTypeId() || type == EffectAreaType.BEACON.getTypeId()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected boolean checkIsNotValidTargetProperty() {
        return this.isNotValidTargetAndContainerNotSpecialState();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.initValueFromParams();
        if (this.m_value == -1) {
            if (triggerRE == null) {
                HPGain.m_logger.error((Object)("Value is -1. Unable to copy value of triggeringEffect : triggerRE==null. Effect id : " + ((WakfuEffect)this.m_genericEffect).getEffectId()));
                this.m_value = 0;
            }
            else {
                this.m_value = triggerRE.getValue();
            }
        }
        this.modifyValueWithModificatorIfNecessary();
    }
    
    protected void modifyValueWithModificatorIfNecessary() {
        if (!this.m_fixedValue) {
            final int percentModificator = this.getValuePercentModificator();
            this.m_value += this.m_value * percentModificator / 100;
        }
        this.m_value = Math.max(0, ValueRounder.randomRound(this.m_value));
    }
    
    public void initValueFromParams() {
        final short level = this.getContainerLevel();
        this.setParamsToDefault();
        if (this.m_genericEffect != null) {
            switch (((WakfuEffect)this.m_genericEffect).getParamsCount()) {
                case 1: {
                    this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
                    if (this.m_element == Elements.PHYSICAL) {
                        this.m_fixedValue = true;
                        break;
                    }
                    this.m_fixedValue = false;
                    break;
                }
                case 2: {
                    this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
                    this.m_fixedValue = (((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM) == 1);
                    break;
                }
                case 3: {
                    this.m_value = DiceRoll.roll(((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM), ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM), ((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM));
                    this.m_fixedValue = false;
                    break;
                }
                case 4: {
                    this.m_value = DiceRoll.roll(((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM), ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM), ((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM));
                    this.m_fixedValue = (((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.RANDOM) == 1);
                    break;
                }
                case 5: {
                    final int mean = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
                    final int variance = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
                    final int min = ((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM);
                    final int max = ((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.RANDOM);
                    final int v = (int)HPGain.RANDOM.nextDouble(mean, variance);
                    this.m_value = MathHelper.clamp(v, min, max);
                    break;
                }
                case 6: {
                    this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
                    this.m_fixedValue = false;
                    this.m_useBonusDamage = (((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM) == 1);
                    this.m_useHealBonus = (((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM) == 1);
                    this.m_useHealResistance = (((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.RANDOM) == 1);
                    break;
                }
            }
        }
    }
    
    protected void setParamsToDefault() {
        this.m_useBonusDamage = true;
        this.m_useHealBonus = true;
        this.m_useHealResistance = true;
    }
    
    public boolean isFixedValue() {
        return this.m_fixedValue;
    }
    
    private int getValuePercentModificator() {
        int percentModificator = 0;
        if (this.m_element != null && this.m_caster.hasCharacteristic(this.m_element.getDamageBonusCharacteristic())) {
            percentModificator += this.m_caster.getCharacteristicValue(this.m_element.getDamageBonusCharacteristic());
        }
        if (this.m_useBonusDamage && this.m_caster.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
            percentModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT);
        }
        if (this.m_useHealBonus && this.m_caster.hasCharacteristic(FighterCharacteristicType.HEAL_IN_PERCENT)) {
            percentModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.HEAL_IN_PERCENT);
        }
        if (this.m_useHealResistance && this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.RES_HEAL_PERCENT)) {
            percentModificator -= this.m_target.getCharacteristicValue(FighterCharacteristicType.RES_HEAL_PERCENT);
        }
        if (this.m_target.isActiveProperty(FightPropertyType.UNDEAD)) {
            if (this.m_target.hasCharacteristic(FighterCharacteristicType.RES_IN_PERCENT)) {
                percentModificator -= this.m_target.getCharacteristicValue(FighterCharacteristicType.RES_IN_PERCENT);
            }
            if (this.m_element != null && this.m_target.hasCharacteristic(this.m_element.getResistanceBonusCharacteristic())) {
                percentModificator -= this.m_target.getCharacteristicValue(this.m_element.getResistanceBonusCharacteristic());
            }
        }
        if (this.hasProperty(RunningEffectPropertyType.SINGLE_TARGET_EFFECT) && this.m_caster.hasCharacteristic(FighterCharacteristicType.SINGLE_TARGET_DMG)) {
            percentModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.SINGLE_TARGET_DMG);
        }
        if (HpLossComputerImpl.isMeleeEffect(this.m_caster, this.m_target, (WakfuEffect)this.m_genericEffect) && this.m_caster.hasCharacteristic(FighterCharacteristicType.MELEE_DMG)) {
            percentModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.MELEE_DMG);
        }
        if (HpLossComputerImpl.isRangedEffect(this.m_caster, this.m_target, (WakfuEffect)this.m_genericEffect) && this.m_caster.hasCharacteristic(FighterCharacteristicType.RANGED_DMG)) {
            percentModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.RANGED_DMG);
        }
        if (this.hasProperty(RunningEffectPropertyType.ZONE_EFFECT) && this.m_caster.hasCharacteristic(FighterCharacteristicType.AOE_DMG)) {
            percentModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.AOE_DMG);
        }
        return percentModificator;
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
    public BitSet getTriggersToExecute() {
        if (this.m_target != null && this.m_target.isActiveProperty(FightPropertyType.UNDEAD)) {
            return HPGain.EMPTY_BIT_SET;
        }
        return super.getTriggersToExecute();
    }
    
    @Override
    public void onCheckIn() {
        this.m_fixedValue = false;
        this.m_useBonusDamage = true;
        this.m_useHealBonus = true;
        this.m_useHealResistance = true;
        super.onCheckIn();
    }
    
    static {
        RANDOM = new Normal(0.0, 0.0, new MersenneTwister());
        EMPTY_BIT_SET = new BitSet();
        m_staticPool = new MonitoredPool(new ObjectFactory<HPGain>() {
            @Override
            public HPGain makeObject() {
                return new HPGain();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("gain de PdV", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (-1=valeur de l'effet d\u00e9clencheur)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("gain de PdV, fixe ou non ", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("fixe (0=non, 1=oui, defaut = non)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("gain de PdV xdx+x", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("+", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("gain de PdV xdx +x, fixe ou non", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("+", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("fixe (0=non, 1=oui, defaut = non)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Gain de Pdv - Distibution Normale", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("mean", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("variance", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("min", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("max", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("param qui sert a rien ", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Gain de Pdv - Choix des modificateurs", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (-1=valeur de l'effet d\u00e9clencheur)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Bonus aux d\u00e9g\u00e2ts (0=non, 1=oui) (ne sert plus)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Bonus aux soins (0=non, 1=oui)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("R\u00e9sistances aux soins (0=non, 1=oui)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("param qui sert a rien 1", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("param qui sert a rien 2", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
