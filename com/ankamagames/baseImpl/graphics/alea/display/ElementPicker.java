package com.ankamagames.baseImpl.graphics.alea.display;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import java.util.*;

public class ElementPicker
{
    private static final Logger m_logger;
    private static final Comparator<DisplayedScreenElement> SORT_BY_ZORDER;
    int m_lastX;
    int m_lastY;
    final ArrayList<DisplayedScreenElement> elements;
    
    public ElementPicker() {
        super();
        this.m_lastX = Integer.MAX_VALUE;
        this.m_lastY = Integer.MAX_VALUE;
        this.elements = new ArrayList<DisplayedScreenElement>();
    }
    
    void reset() {
        this.m_lastX = Integer.MAX_VALUE;
        this.m_lastY = Integer.MAX_VALUE;
        this.elements.clear();
    }
    
    private void prepareCell(final DisplayedScreenWorld world, final int x, final int y) {
        if (x == this.m_lastX && y == this.m_lastY) {
            return;
        }
        this.elements.clear();
        world.getElements(x, y, this.elements, ElementFilter.ALL);
        this.m_lastX = x;
        this.m_lastY = y;
    }
    
    int getAltitudeOrder(final DisplayedScreenWorld world, final int x, final int y, final int z) {
        DisplayedScreenElement found = this.getElement(world, x, y, z);
        try {
            for (int i = this.elements.size() - 1; i >= 0; --i) {
                final DisplayedScreenElement elt = this.elements.get(i);
                if (elt.isHollow()) {
                    if (found != null && elt.getEntitySprite().m_zOrder > found.getEntitySprite().m_zOrder) {
                        found = elt;
                    }
                }
            }
        }
        catch (Exception e) {
            ElementPicker.m_logger.warn((Object)"Probl\u00e8me dans le process d'affichage :", (Throwable)e);
            return 0;
        }
        return (found != null) ? found.m_element.getAltitudeOrder() : 0;
    }
    
    DisplayedScreenElement getElement(final DisplayedScreenWorld world, final int x, final int y, final int z) {
        this.prepareCell(world, x, y);
        final int count = this.elements.size();
        if (count == 0) {
            return null;
        }
        if (count == 1) {
            return this.elements.get(0);
        }
        int delta = Integer.MAX_VALUE;
        DisplayedScreenElement found = null;
        try {
            Collections.sort(this.elements, ElementPicker.SORT_BY_ZORDER);
            for (int i = 0; i < count; ++i) {
                final DisplayedScreenElement elt = this.elements.get(i);
                if (!elt.isHollow()) {
                    final int top = elt.getWorldCellAltitude();
                    final int bottom = top - elt.m_element.getHeight();
                    final boolean isInside = z >= bottom && z <= top;
                    final int dz = Math.abs(top - z);
                    if ((dz == 0 && delta == 0) || isInside) {
                        final int foundAltitudeOrder = (found == null) ? -1 : found.m_element.getAltitudeOrder();
                        if (elt.m_element.getAltitudeOrder() > foundAltitudeOrder) {
                            found = elt;
                        }
                    }
                    if (dz < delta) {
                        delta = (short)dz;
                        found = elt;
                    }
                }
            }
        }
        catch (Exception e) {
            ElementPicker.m_logger.warn((Object)"Probl\u00e8me dans le process d'affichage :", (Throwable)e);
            return null;
        }
        return found;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ElementPicker.class);
        SORT_BY_ZORDER = new Comparator<DisplayedScreenElement>() {
            @Override
            public int compare(final DisplayedScreenElement o1, final DisplayedScreenElement o2) {
                return o1.m_element.m_altitudeOrder - o2.m_element.m_altitudeOrder;
            }
        };
    }
}
