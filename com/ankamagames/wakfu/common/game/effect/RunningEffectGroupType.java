package com.ankamagames.wakfu.common.game.effect;

import gnu.trove.*;

public final class RunningEffectGroupType
{
    public static final TIntHashSet EFFECT_GROUP_TYPE_IDS;
    public static final TIntHashSet WITHOUT_EFFECT_GROUP_DESCRIPTION;
    
    static {
        EFFECT_GROUP_TYPE_IDS = new TIntHashSet();
        WITHOUT_EFFECT_GROUP_DESCRIPTION = new TIntHashSet();
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_LEVEL_FUNCTION_STATE.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_LEVEL_FUNCTION_SPELL.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.EFFECT_RANDOM_IN_AREA.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_WITH_AT_LEAST_NULL_EFFECT.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_RANDOM_TARGETS.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RANDOM_RUNNING_EFFECT_GROUP.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.EFFECT_VALUE_FUNCTION_GLYPH_CHARGE.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.EFFECT_VALUE_FUNCTION_FECA_ARMOR.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.HP_LOSS_ACCUMULATION.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.GROUP_SECOND_VALUE_FUNCTION_FIRST.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_PROBA_FUNCTION_PA_PM_PW.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.EFFECT_PROBA_FUNCTION_GLYPH_CHARGE.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.EFFECT_PROBA_FUNCTION_ARMOR_PLATE.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.MODIFY_SUB_EFFECT_BY_CHRAGE.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.MODIFY_SUB_EFFECT_BY_TARGET_PLATE.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RANDOM_IN_AREA_BY_AREA_HP.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_PROBA_FUNCTION_STATE_LEVEL.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.MODIFY_SUB_EFFECT_BY_ARMOR_PLATE.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.REG_EXECUTION_COUNT_FUNCTION_AREA_HP.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.REG_EXECUTION_COUNT_FUNCTION_ALLIES_COUNT.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.BLITZKRIEK_EFFECT.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_LEVEL_FUNCTION_TRIGGERING_SPELL_COST.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_LEVEL_FUNCTION_TRIGGERING_ACTION_COST.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_LEVEL_FUNCTION_CHARACTERISTIC.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.REG_EXECUTION_COUNT_FUNCTION_TRIGGERING_ACTION_COST.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.REG_EXECUTION_COUNT_FIXED.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_LEVEL_FUNCTION_CHARACTER_LEVEL.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.REG_SUB_EFFECT_VALUE_FUNCTION_CASTER_AP.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.REG_SUB_EFFECT_VALUE_FUNCTION_CASTER_MP.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.SPECIFIC_FOR_ARMOR_DAMAGE_REBOUND.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_LEVEL_FUNCTION_TRIGGERING_VALUE.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.STEAMER_BLOCK_DAMAGE_REDIRECTION.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.HP_LOSS_WITH_REBOUND.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.HP_AIR_LOSS_WITH_REBOUND.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.HP_FIRE_LOSS_WITH_REBOUND.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.HP_WATER_LOSS_WITH_REBOUND.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.HP_EARTH_LOSS_WITH_REBOUND.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.DIVIDE_HP_LOSS_UNLESS_THRESHOLD.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.RUNNING_EFFECT_GROUP_LEVEL_FUNCTION_TOTAL_HP.getId());
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.add(RunningEffectConstants.CARRY.getId());
        RunningEffectGroupType.WITHOUT_EFFECT_GROUP_DESCRIPTION.add(RunningEffectConstants.HP_LOSS_WITH_REBOUND.getId());
        RunningEffectGroupType.WITHOUT_EFFECT_GROUP_DESCRIPTION.add(RunningEffectConstants.HP_AIR_LOSS_WITH_REBOUND.getId());
        RunningEffectGroupType.WITHOUT_EFFECT_GROUP_DESCRIPTION.add(RunningEffectConstants.HP_FIRE_LOSS_WITH_REBOUND.getId());
        RunningEffectGroupType.WITHOUT_EFFECT_GROUP_DESCRIPTION.add(RunningEffectConstants.HP_WATER_LOSS_WITH_REBOUND.getId());
        RunningEffectGroupType.WITHOUT_EFFECT_GROUP_DESCRIPTION.add(RunningEffectConstants.HP_EARTH_LOSS_WITH_REBOUND.getId());
        RunningEffectGroupType.WITHOUT_EFFECT_GROUP_DESCRIPTION.add(RunningEffectConstants.CARRY.getId());
    }
}
