package com.ankamagames.wakfu.common.game.effect.runningEffect.util.hpLoss;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import java.math.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.item.*;

public final class HpLossComputerImpl implements HpLossComputer
{
    private static Logger m_logger;
    private static final float RESIST_PERCENT_BY_100_RES = 20.0f;
    private static final int MELEE_DAMAGE_RANGE = 2;
    public static final int BACK_ADDITIONNAL_DAMAGE_IN_PERCENT = 25;
    public static final int SIDE_ADDITIONNAL_DAMAGE_IN_PERCENT = 10;
    private static final int MAXIMUM_DAMAGE_REDUCTION_PRE_MOD = 0;
    private static final int MAXIMUM_DAMAGE_REDUCTION_POST_MOD = -75;
    private final EffectUser m_caster;
    private final EffectUser m_target;
    private final Elements m_element;
    private final WakfuEffect m_effect;
    private Elements m_elementForResistance;
    private boolean m_isAffectedByLocalisation;
    private int m_conditions;
    private boolean m_isCritical;
    private int m_customDmgPercentBonus;
    private int m_customResistPercentBonus;
    private int m_preModificator;
    private int m_postModificator;
    private int m_value;
    
    public HpLossComputerImpl(final EffectUser caster, final EffectUser target, final Elements element, final WakfuEffect effect) {
        super();
        this.m_target = target;
        this.m_caster = caster;
        this.m_element = element;
        this.m_effect = effect;
        this.m_elementForResistance = this.m_element;
    }
    
    @Override
    public void computeWithModificator() {
        if (SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.NEW_HP_LOSS_FORMULA)) {
            this.computeNewValue();
        }
        else {
            this.computeClassicValue();
        }
    }
    
    private void computeNewValue() {
        final int resistance = this.computeResistanceModificator();
        final int bonusDmgModificator = this.computeBonusDmgModificator();
        final double reduction = computeReduction(resistance);
        float value = this.m_value;
        value *= (float)((1.0f + bonusDmgModificator / 100.0f) * reduction);
        final int localisationModificator = this.getLocalisationModificator();
        value *= (100 + localisationModificator) / 100.0f;
        final int finalResDmgModificator = this.getFinalResDmgModificator();
        value *= (100 + finalResDmgModificator) / 100.0f;
        this.m_value = Math.max(1, ValueRounder.randomRound(value));
    }
    
    public static double computeReduction(final int resistance) {
        final double res = Math.pow(0.800000011920929, resistance / 100.0f);
        return round(res, 2);
    }
    
    private static double round(final double res, final int decimalPlace) {
        final BigDecimal bigDecimal = new BigDecimal(res);
        return bigDecimal.setScale(decimalPlace, 2).doubleValue();
    }
    
    private void computeClassicValue() {
        float value = this.m_value;
        this.m_preModificator = 0;
        this.m_postModificator = 0;
        this.m_preModificator -= this.computeResistanceModificator();
        this.m_preModificator += this.computeBonusDmgModificator();
        this.m_postModificator = this.computePostModificator();
        this.clampModificators();
        value = value * (100 + this.m_preModificator) / 100.0f * (100 + this.m_postModificator) / 100.0f;
        this.m_value = Math.max(1, ValueRounder.randomRound(value));
    }
    
    private void clampModificators() {
        this.m_preModificator = Math.max(0, this.m_preModificator);
        this.m_postModificator = Math.max(-75, this.m_postModificator);
    }
    
    private int computeLocalisationBonusCharac() {
        final Part part = this.getPartLocalisation();
        if (part == null) {
            return 0;
        }
        int localisationModificator = 0;
        switch (part.getPartId()) {
            case 2: {
                if (this.m_caster.hasCharacteristic(FighterCharacteristicType.BACKSTAB_BONUS)) {
                    localisationModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.BACKSTAB_BONUS);
                }
                if (this.m_target.hasCharacteristic(FighterCharacteristicType.RES_BACKSTAB)) {
                    localisationModificator -= this.m_target.getCharacteristicValue(FighterCharacteristicType.RES_BACKSTAB);
                    break;
                }
                break;
            }
        }
        return localisationModificator;
    }
    
    private Part getPartLocalisation() {
        if (this.m_element == Elements.PHYSICAL || !this.m_isAffectedByLocalisation) {
            return null;
        }
        if (this.m_target.getPartLocalisator() == null) {
            return null;
        }
        final Part part = this.m_target.getPartLocalisator().getMainPartInSightFromPosition(this.m_caster.getWorldCellX(), this.m_caster.getWorldCellY(), this.m_caster.getWorldCellAltitude());
        if (part == null) {
            HpLossComputerImpl.m_logger.error((Object)"Impossible de r\u00e9cup\u00e9rer un partLocalisation...");
            return null;
        }
        return part;
    }
    
    private int computeResistanceModificator() {
        if ((this.m_conditions & 0x2) == 0x0) {
            return 0;
        }
        if (this.m_element == Elements.STASIS) {
            this.m_elementForResistance = this.getTargetLowerResist();
        }
        else {
            this.m_elementForResistance = this.m_element;
        }
        int resistanceModificator = this.m_customResistPercentBonus;
        final EffectUser resistanceTarget = this.m_target.getResistanceTarget();
        if (resistanceTarget.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
            if (resistanceTarget.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
                resistanceModificator += resistanceTarget.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT);
            }
            final FighterCharacteristicType resistanceBonusCharacteristic = this.m_elementForResistance.getDamageBonusCharacteristic();
            if (resistanceTarget.hasCharacteristic(resistanceBonusCharacteristic)) {
                resistanceModificator += resistanceTarget.getCharacteristicValue(this.m_elementForResistance.getDamageBonusCharacteristic());
            }
        }
        else {
            if (resistanceTarget.hasCharacteristic(FighterCharacteristicType.RES_IN_PERCENT)) {
                resistanceModificator += resistanceTarget.getCharacteristicValue(FighterCharacteristicType.RES_IN_PERCENT);
            }
            final FighterCharacteristicType resistanceBonusCharacteristic = this.m_elementForResistance.getResistanceBonusCharacteristic();
            if (resistanceTarget.hasCharacteristic(resistanceBonusCharacteristic)) {
                resistanceModificator += resistanceTarget.getCharacteristicValue(this.m_elementForResistance.getResistanceBonusCharacteristic());
            }
        }
        return resistanceModificator;
    }
    
    private Elements getTargetLowerResist() {
        final List<Elements> elements = new ArrayList<Elements>();
        if (this.m_target.hasCharacteristic(Elements.EARTH.getResistanceBonusCharacteristic())) {
            elements.add(Elements.EARTH);
        }
        if (this.m_target.hasCharacteristic(Elements.AIR.getResistanceBonusCharacteristic())) {
            elements.add(Elements.AIR);
        }
        if (this.m_target.hasCharacteristic(Elements.FIRE.getResistanceBonusCharacteristic())) {
            elements.add(Elements.FIRE);
        }
        if (this.m_target.hasCharacteristic(Elements.WATER.getResistanceBonusCharacteristic())) {
            elements.add(Elements.WATER);
        }
        if (elements.isEmpty()) {
            return this.m_element;
        }
        Collections.sort(elements, new Comparator<Elements>() {
            @Override
            public int compare(final Elements o1, final Elements o2) {
                return Integer.valueOf(HpLossComputerImpl.this.m_target.getCharacteristicValue(o1.getResistanceBonusCharacteristic())).compareTo(Integer.valueOf(HpLossComputerImpl.this.m_target.getCharacteristicValue(o2.getResistanceBonusCharacteristic())));
            }
        });
        int i;
        for (i = 0; i < elements.size() - 1; ++i) {
            final int currentResist = this.m_target.getCharacteristicValue(elements.get(i).getResistanceBonusCharacteristic());
            final int nextResist = this.m_target.getCharacteristicValue(elements.get(i + 1).getResistanceBonusCharacteristic());
            if (currentResist < nextResist) {
                break;
            }
        }
        return elements.get(MathHelper.random(i + 1));
    }
    
    private int computeBonusDmgModificator() {
        if ((this.m_conditions & 0x1) == 0x0 || this.m_caster == null) {
            return 0;
        }
        int bonusDmgModificator = this.m_customDmgPercentBonus;
        bonusDmgModificator += this.computeLocalisationBonusCharac();
        if (this.m_caster.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
            if (this.m_caster.hasCharacteristic(FighterCharacteristicType.RES_IN_PERCENT)) {
                bonusDmgModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.RES_IN_PERCENT);
            }
            if (this.m_element != Elements.PHYSICAL) {
                bonusDmgModificator += this.damageModificatorFromCharacs(this.m_element.getResistanceBonusCharacteristic());
            }
        }
        else {
            if (this.m_caster.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
                bonusDmgModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT);
            }
            if (this.m_element != Elements.PHYSICAL) {
                bonusDmgModificator += this.damageModificatorFromCharacs(this.m_element.getDamageBonusCharacteristic());
            }
        }
        if (this.m_target instanceof BasicCharacterInfo && ((BasicCharacterInfo)this.m_target).isSummoned() && this.m_caster.hasCharacteristic(FighterCharacteristicType.DMG_VS_SUMMMONS)) {
            bonusDmgModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.DMG_VS_SUMMMONS);
        }
        if (this.m_isCritical) {
            if (this.m_caster.hasCharacteristic(FighterCharacteristicType.CRITICAL_BONUS)) {
                bonusDmgModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.CRITICAL_BONUS);
            }
            if (this.m_target.hasCharacteristic(FighterCharacteristicType.CRITICAL_RES)) {
                bonusDmgModificator -= this.m_target.getCharacteristicValue(FighterCharacteristicType.CRITICAL_RES);
            }
        }
        else if (this.m_caster.hasCharacteristic(FighterCharacteristicType.NON_CRIT_DMG)) {
            bonusDmgModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.NON_CRIT_DMG);
        }
        if (hasProperty(this.m_effect, RunningEffectPropertyType.SINGLE_TARGET_EFFECT) && this.m_caster.hasCharacteristic(FighterCharacteristicType.SINGLE_TARGET_DMG)) {
            bonusDmgModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.SINGLE_TARGET_DMG);
        }
        if (this.isMeleeEffect() && this.m_caster.hasCharacteristic(FighterCharacteristicType.MELEE_DMG)) {
            bonusDmgModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.MELEE_DMG);
        }
        if (this.isRangedEffect() && this.m_caster.hasCharacteristic(FighterCharacteristicType.RANGED_DMG)) {
            bonusDmgModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.RANGED_DMG);
        }
        if (hasProperty(this.m_effect, RunningEffectPropertyType.ZONE_EFFECT) && this.m_caster.hasCharacteristic(FighterCharacteristicType.AOE_DMG)) {
            bonusDmgModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.AOE_DMG);
        }
        int hpMax = 0;
        int hpValue = 0;
        if (this.m_caster.hasCharacteristic(FighterCharacteristicType.HP)) {
            hpValue += this.m_caster.getCharacteristicValue(FighterCharacteristicType.HP);
            hpMax += this.m_caster.getCharacteristic(FighterCharacteristicType.HP).max();
        }
        if (this.m_caster.hasCharacteristic(FighterCharacteristicType.VIRTUAL_HP)) {
            hpValue += this.m_caster.getCharacteristicValue(FighterCharacteristicType.VIRTUAL_HP);
            hpMax += this.m_caster.getCharacteristic(FighterCharacteristicType.VIRTUAL_HP).max();
        }
        if (this.m_caster.hasCharacteristic(FighterCharacteristicType.BERSERK_DMG)) {
            final float hpPercent = hpValue / hpMax * 100.0f;
            if (hpPercent < 50.0f) {
                bonusDmgModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.BERSERK_DMG);
            }
        }
        return bonusDmgModificator;
    }
    
    private boolean isRangedEffect() {
        return isRangedEffect(this.m_caster, this.m_target, this.m_effect);
    }
    
    public static boolean isRangedEffect(final EffectUser caster, final EffectUser target, final WakfuEffect effect) {
        final int intersectionDistance = DistanceUtils.getIntersectionDistance(caster, target);
        return hasProperty(effect, RunningEffectPropertyType.RANGED_EFFECT) || (!hasRangeOrMeleeProperty(effect) && intersectionDistance > 2);
    }
    
    private boolean isMeleeEffect() {
        return isMeleeEffect(this.m_caster, this.m_target, this.m_effect);
    }
    
    public static boolean isMeleeEffect(final EffectUser caster, final EffectUser target, final WakfuEffect effect) {
        final int intersectionDistance = DistanceUtils.getIntersectionDistance(caster, target);
        return hasProperty(effect, RunningEffectPropertyType.MELEE_EFFECT) || (!hasRangeOrMeleeProperty(effect) && intersectionDistance <= 2);
    }
    
    private static boolean hasRangeOrMeleeProperty(final WakfuEffect effect) {
        return hasProperty(effect, RunningEffectPropertyType.RANGED_EFFECT) || hasProperty(effect, RunningEffectPropertyType.MELEE_EFFECT);
    }
    
    private static boolean hasProperty(final WakfuEffect effect, final RunningEffectPropertyType property) {
        return effect != null && effect.hasProperty(property);
    }
    
    private int damageModificatorFromCharacs(final FighterCharacteristicType damageBonusCharac) {
        int modificator = 0;
        if (this.m_caster.hasCharacteristic(damageBonusCharac)) {
            final FighterCharacteristic characteristic = (FighterCharacteristic)this.m_caster.getCharacteristic(damageBonusCharac);
            modificator += characteristic.unboundedValue();
            if (modificator > characteristic.getUpperBound()) {
                modificator = characteristic.getUpperBound();
            }
        }
        return modificator;
    }
    
    private int computePostModificator() {
        int postModificator = this.getLocalisationModificator();
        postModificator += this.getFinalResDmgModificator();
        return postModificator;
    }
    
    private int getFinalResDmgModificator() {
        if ((this.m_conditions & 0x10) == 0x0 || this.m_caster == null || this.m_target == null) {
            return 0;
        }
        int result = 0;
        if (this.m_caster.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
            if (this.m_caster.hasCharacteristic(FighterCharacteristicType.FINAL_RES_IN_PERCENT)) {
                result += this.m_caster.getCharacteristicValue(FighterCharacteristicType.FINAL_RES_IN_PERCENT);
            }
        }
        else if (this.m_caster.hasCharacteristic(FighterCharacteristicType.FINAL_DMG_IN_PERCENT)) {
            result += this.m_caster.getCharacteristicValue(FighterCharacteristicType.FINAL_DMG_IN_PERCENT);
        }
        if (this.m_target.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
            if (this.m_target.hasCharacteristic(FighterCharacteristicType.FINAL_DMG_IN_PERCENT)) {
                result -= this.m_target.getCharacteristicValue(FighterCharacteristicType.FINAL_DMG_IN_PERCENT);
            }
        }
        else if (this.m_target.hasCharacteristic(FighterCharacteristicType.FINAL_RES_IN_PERCENT)) {
            result -= this.m_target.getCharacteristicValue(FighterCharacteristicType.FINAL_RES_IN_PERCENT);
        }
        return result;
    }
    
    private int getLocalisationModificator() {
        final Part part = this.getPartLocalisation();
        if (part == null) {
            return 0;
        }
        switch (part.getPartId()) {
            case 2: {
                return 25;
            }
            case 1:
            case 3: {
                return 10;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public Elements getElementForResistance() {
        return this.m_elementForResistance;
    }
    
    @Override
    public int getValue() {
        return this.m_value;
    }
    
    @Override
    public void setConditions(final int conditions) {
        this.m_conditions = conditions;
    }
    
    @Override
    public void setAffectedByLocalisation(final boolean affectedByLocalisation) {
        this.m_isAffectedByLocalisation = affectedByLocalisation;
    }
    
    @Override
    public void setValue(final int value) {
        this.m_value = value;
    }
    
    @Override
    public void setWeaponUsed(final Item weaponUsed) {
    }
    
    @Override
    public void setIsCritical(final boolean critical) {
        this.m_isCritical = critical;
    }
    
    @Override
    public void setCustomResistPercentBonus(final int customResistPercentBonus) {
        this.m_customResistPercentBonus = customResistPercentBonus;
    }
    
    @Override
    public void setCustomDmgPercentBonus(final int customDmgPercentBonus) {
        this.m_customDmgPercentBonus = customDmgPercentBonus;
    }
    
    static {
        HpLossComputerImpl.m_logger = Logger.getLogger((Class)HpLossComputerImpl.class);
    }
}
