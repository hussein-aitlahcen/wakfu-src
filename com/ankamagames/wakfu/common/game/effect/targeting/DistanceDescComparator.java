package com.ankamagames.wakfu.common.game.effect.targeting;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

final class DistanceDescComparator implements Comparator<EffectUser>
{
    private final int m_centerX;
    private final int m_centerY;
    
    DistanceDescComparator(final int x, final int y) {
        super();
        this.m_centerX = x;
        this.m_centerY = y;
    }
    
    @Override
    public int compare(final EffectUser o1, final EffectUser o2) {
        final int distanceToFirst = o1.getPosition().getDistance(this.m_centerX, this.m_centerY);
        final int distanceToSecond = o2.getPosition().getDistance(this.m_centerX, this.m_centerY);
        if (distanceToFirst > distanceToSecond) {
            return -1;
        }
        if (distanceToFirst < distanceToSecond) {
            return 1;
        }
        return 0;
    }
}
