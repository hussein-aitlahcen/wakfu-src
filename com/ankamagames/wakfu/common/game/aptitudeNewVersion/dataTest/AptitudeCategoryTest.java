package com.ankamagames.wakfu.common.game.aptitudeNewVersion.dataTest;

import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import gnu.trove.*;

public final class AptitudeCategoryTest
{
    public static final AptitudeCategoryModel FORCE_CAT;
    public static final AptitudeCategoryModel INTEL_CAT;
    public static final AptitudeCategoryModel AGI_CAT;
    public static final AptitudeCategoryModel CHANCE_CAT;
    public static final AptitudeCategoryModel CAPITAL_CAT;
    
    private static TIntArrayList getProcLevels(final short start, final short modulo) {
        final TIntArrayList set = new TIntArrayList();
        set.add(start);
        for (short i = start; i <= 200; ++i) {
            if (i % modulo == 0) {
                set.add((short)(i + start));
            }
        }
        return set;
    }
    
    static {
        FORCE_CAT = new AptitudeCategoryModel(1, getProcLevels((short)2, (short)4));
        INTEL_CAT = new AptitudeCategoryModel(2, getProcLevels((short)3, (short)4));
        AGI_CAT = new AptitudeCategoryModel(3, getProcLevels((short)4, (short)4));
        CHANCE_CAT = new AptitudeCategoryModel(4, getProcLevels((short)5, (short)4));
        CAPITAL_CAT = new AptitudeCategoryModel(5, new TIntArrayList(new int[] { 40, 80, 120, 160 }));
        AptitudeCategoryTest.FORCE_CAT.addBonus(AptitudeBonusTest.DEG_ALL_BOOST);
        AptitudeCategoryTest.FORCE_CAT.addBonus(AptitudeBonusTest.DEG_MONO_CLOSE_BOOST);
        AptitudeCategoryTest.FORCE_CAT.addBonus(AptitudeBonusTest.DEG_ZONE_CLOSE_BOOST);
        AptitudeCategoryTest.FORCE_CAT.addBonus(AptitudeBonusTest.DEG_MONO_RANGE_BOOST);
        AptitudeCategoryTest.FORCE_CAT.addBonus(AptitudeBonusTest.DEG_ZONE_RANGE_BOOST);
        AptitudeCategoryTest.INTEL_CAT.addBonus(AptitudeBonusTest.HP_MAX_BOOST);
        AptitudeCategoryTest.INTEL_CAT.addBonus(AptitudeBonusTest.RES_BOOST);
        AptitudeCategoryTest.INTEL_CAT.addBonus(AptitudeBonusTest.HP_REGEN_BY_TURN_BOOST);
        AptitudeCategoryTest.AGI_CAT.addBonus(AptitudeBonusTest.LOCK_BOOST);
        AptitudeCategoryTest.AGI_CAT.addBonus(AptitudeBonusTest.DODGE_BOOST);
        AptitudeCategoryTest.AGI_CAT.addBonus(AptitudeBonusTest.INIT_BOOST);
        AptitudeCategoryTest.AGI_CAT.addBonus(AptitudeBonusTest.LOCK_N_DODGE_BOOST);
        AptitudeCategoryTest.CHANCE_CAT.addBonus(AptitudeBonusTest.CRITICAL_BOOST);
        AptitudeCategoryTest.CHANCE_CAT.addBonus(AptitudeBonusTest.BLOCK_BOOST);
        AptitudeCategoryTest.CHANCE_CAT.addBonus(AptitudeBonusTest.CRITICAL_DAMAGES_BOOST);
        AptitudeCategoryTest.CHANCE_CAT.addBonus(AptitudeBonusTest.BACKSTAB_BOOST);
        AptitudeCategoryTest.CHANCE_CAT.addBonus(AptitudeBonusTest.BERZERK_DAMAGES_BOOST);
        AptitudeCategoryTest.CHANCE_CAT.addBonus(AptitudeBonusTest.HEAL_BOOST);
        AptitudeCategoryTest.CHANCE_CAT.addBonus(AptitudeBonusTest.NON_CRITICAL_BOOST);
        AptitudeCategoryTest.CAPITAL_CAT.addBonus(AptitudeBonusTest.PA_BOOST);
        AptitudeCategoryTest.CAPITAL_CAT.addBonus(AptitudeBonusTest.PM_BOOST);
        AptitudeCategoryTest.CAPITAL_CAT.addBonus(AptitudeBonusTest.PO_BOOST);
        AptitudeCategoryTest.CAPITAL_CAT.addBonus(AptitudeBonusTest.PW_BOOST);
        AptitudeCategoryTest.CAPITAL_CAT.addBonus(AptitudeBonusTest.CONTROL_BOOST);
        AptitudeCategoryTest.CAPITAL_CAT.addBonus(AptitudeBonusTest.KIT_SKILL_BOOST);
    }
}
