package com.ankamagames.wakfu.common.game.aptitude;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.utils.*;
import gnu.trove.*;

public final class AptitudeAlternativeCostConstants
{
    public static final int FORCE_APTITUDE_ID = 167;
    public static final int AGILITY_APTITUDE_ID = 168;
    public static final int INTELLIGENCE_APTITUDE_ID = 169;
    public static final int CHANCE_APTITUDE_ID = 170;
    public static final int REDUCED_COST_FOR_ELEMENTARY_APTITUDES = 1;
    public static final TIntHashSet ELEMENTARY_APTITUDES_IDS;
    
    public static boolean shouldUseReducedCost(final BasicCharacterInfo characterInfo, final int aptitudeId, final short level) {
        final AptitudeInventory aptitudeInventory = characterInfo.getAptitudeInventory();
        if (aptitudeInventory == null) {
            return false;
        }
        final Aptitude aptitude = aptitudeInventory.getWithUniqueId((short)aptitudeId);
        if (aptitude == null) {
            return false;
        }
        final Accumulator hasAptitudeWithHigherLevel = new Accumulator();
        if (!AptitudeAlternativeCostConstants.ELEMENTARY_APTITUDES_IDS.contains(aptitudeId)) {
            return false;
        }
        AptitudeAlternativeCostConstants.ELEMENTARY_APTITUDES_IDS.forEach(new TIntProcedure() {
            @Override
            public boolean execute(final int value) {
                if (value == aptitudeId) {
                    return true;
                }
                final Aptitude anotherAptitude = aptitudeInventory.getWithUniqueId((short)value);
                if (anotherAptitude == null) {
                    return true;
                }
                if (anotherAptitude.getLevel() >= level) {
                    hasAptitudeWithHigherLevel.inc();
                    return false;
                }
                return true;
            }
        });
        return hasAptitudeWithHigherLevel.getValue() > 0;
    }
    
    static {
        (ELEMENTARY_APTITUDES_IDS = new TIntHashSet()).add(167);
        AptitudeAlternativeCostConstants.ELEMENTARY_APTITUDES_IDS.add(168);
        AptitudeAlternativeCostConstants.ELEMENTARY_APTITUDES_IDS.add(169);
        AptitudeAlternativeCostConstants.ELEMENTARY_APTITUDES_IDS.add(170);
    }
}
