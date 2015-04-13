package com.ankamagames.wakfu.common.game.fight.microbotCombination.utils;

import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;

class MicrobotPositionComparator implements Comparator<AbstractFakeFighterEffectArea>
{
    @Override
    public int compare(final AbstractFakeFighterEffectArea bot1, final AbstractFakeFighterEffectArea bot2) {
        final Point3 pos1 = bot1.getPosition();
        final Point3 pos2 = bot2.getPosition();
        return pos1.getX() - pos2.getX() + (pos1.getY() - pos2.getY());
    }
}
