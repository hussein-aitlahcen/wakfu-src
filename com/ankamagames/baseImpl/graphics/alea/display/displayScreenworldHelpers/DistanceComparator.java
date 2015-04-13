package com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers;

import java.util.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

abstract class DistanceComparator<M extends WorldCache.Map> implements Comparator<M>
{
    private static final Logger m_logger;
    private float m_centerX;
    private float m_centerY;
    
    public final void setCenter(final float centerX, final float centerY) {
        this.m_centerX = centerX;
        this.m_centerY = centerY;
    }
    
    @Override
    public final int compare(final M o1, final M o2) {
        final ScreenMap m1 = this.getScreenMap(o1);
        final ScreenMap m2 = this.getScreenMap(o2);
        final float d1 = distanceToCenter(m1, this.m_centerX, this.m_centerY);
        final float d2 = distanceToCenter(m2, this.m_centerX, this.m_centerY);
        if (d1 == d2) {
            return 0;
        }
        return (d1 < d2) ? 1 : -1;
    }
    
    private static float distanceToCenter(final ScreenMap map, final float centerX, final float centerY) {
        if (map == null) {
            return Float.POSITIVE_INFINITY;
        }
        final float x1 = map.getX() - centerX;
        final float y1 = map.getY() - centerY;
        return x1 * x1 + y1 * y1;
    }
    
    protected abstract ScreenMap getScreenMap(final M p0);
    
    static {
        m_logger = Logger.getLogger((Class)DistanceComparator.class);
    }
}
