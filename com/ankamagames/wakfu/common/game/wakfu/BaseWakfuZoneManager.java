package com.ankamagames.wakfu.common.game.wakfu;

import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public class BaseWakfuZoneManager
{
    static final Accumulator computedEquilibriumHelper;
    
    public float computeEquilibrium(final TIntObjectHashMap<Interval> targets, final TIntIntHashMap currentCounts) {
        if (targets == null) {
            return 0.0f;
        }
        BaseWakfuZoneManager.computedEquilibriumHelper.reset();
        if (!targets.isEmpty()) {
            targets.forEachEntry(new TIntObjectProcedure<Interval>() {
                @Override
                public boolean execute(final int familyId, final Interval targetInterval) {
                    final int currentCount = currentCounts.get(familyId);
                    if (targetInterval.isIn(currentCount)) {
                        BaseWakfuZoneManager.computedEquilibriumHelper.accumulate(1);
                    }
                    return true;
                }
            });
        }
        return BaseWakfuZoneManager.computedEquilibriumHelper.getValue() / targets.size();
    }
    
    static {
        computedEquilibriumHelper = new Accumulator();
    }
}
