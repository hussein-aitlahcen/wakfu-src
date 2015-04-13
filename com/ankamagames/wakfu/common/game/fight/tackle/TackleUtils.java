package com.ankamagames.wakfu.common.game.fight.tackle;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;

final class TackleUtils
{
    static boolean moverIsNotInTacklerRange(final TackleUser tackler, final TackleUser mover, final Point3 currentMoverPos) {
        if (tackler == null || mover == null) {
            return true;
        }
        final int radiusSum = tackler.getPhysicalRadius() + mover.getPhysicalRadius();
        final Point3 moverPos = (currentMoverPos == null) ? mover.getPositionConst() : currentMoverPos;
        final Point3 opponentPos = tackler.getPositionConst();
        if (moverPos.equals(opponentPos)) {
            return true;
        }
        final int xDistance = Math.abs(opponentPos.getX() - moverPos.getX());
        if (xDistance > radiusSum + 1) {
            return true;
        }
        final int yDistance = Math.abs(opponentPos.getY() - moverPos.getY());
        return yDistance > radiusSum + 1 || (xDistance == radiusSum + 1 && yDistance == radiusSum + 1);
    }
}
