package com.ankamagames.wakfu.common.game.fight.tackle;

import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

public final class TackleOnPathComputer
{
    public static List<TackleResult> getTackleResultsOnPath(final PathFindResult pathResult, final BasicCharacterInfo mover) {
        if (pathResult == null) {
            return Collections.emptyList();
        }
        final TackleRules tackleRules = new TackleRules();
        final Collection potentialTacklers = mover.getCurrentFight().getPotentialTacklers(mover);
        final TackleResult tackleResultOnStartCell = tackleRules.getTackleResult(mover, potentialTacklers, mover.getPosition());
        final List<TackleResult> res = new ArrayList<TackleResult>();
        res.add(tackleResultOnStartCell);
        final int pathLength = pathResult.getPathLength();
        final Point3 currentPos = new Point3();
        for (int i = 0; i < pathLength - 1; ++i) {
            currentPos.set(pathResult.getPathStep(i));
            res.add(tackleRules.getTackleResult(mover, potentialTacklers, currentPos));
        }
        return res;
    }
}
