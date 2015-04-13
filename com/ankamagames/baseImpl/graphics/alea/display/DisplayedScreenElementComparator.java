package com.ankamagames.baseImpl.graphics.alea.display;

import java.util.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.maths.*;

public enum DisplayedScreenElementComparator
{
    CELL_CENTER_DISTANCE(true, (DisplayedScreenElementParameteredComparator)new DisplayedScreenElementParameteredComparator() {
        @Override
        public int compare(final DisplayedScreenElement o1, final DisplayedScreenElement o2) {
            final Parameters parameters = this.getParameters();
            final float mouseX = parameters.m_mouseX;
            final float mouseY = parameters.m_mouseY;
            final AleaWorldScene scene = parameters.m_scene;
            final float d1 = this.getDistance(o1, mouseX, mouseY, scene);
            final float d2 = this.getDistance(o2, mouseX, mouseY, scene);
            if (d1 > d2) {
                return 1;
            }
            if (d1 < d2) {
                return -1;
            }
            return 0;
        }
        
        private float getDistance(final DisplayedScreenElement elt, final float mouseX, final float mouseY, final AleaWorldScene scene) {
            final ScreenElement element = elt.getElement();
            final Point2 pt = IsoCameraFunc.getScreenPosition(scene, element.getCellX(), element.getCellY(), element.getCellZ());
            return Vector2.sqrLength(mouseX - pt.m_x, mouseY - pt.m_y);
        }
    }), 
    Z_ORDER(true, (DisplayedScreenElementParameteredComparator)new DisplayedScreenElementParameteredComparator() {
        @Override
        public int compare(final DisplayedScreenElement o1, final DisplayedScreenElement o2) {
            return zOrderCompare(o1, o2);
        }
    }), 
    Z_ORDER_DIRECT(false, (DisplayedScreenElementParameteredComparator)new DisplayedScreenElementParameteredComparator() {
        @Override
        public int compare(final DisplayedScreenElement o1, final DisplayedScreenElement o2) {
            return zOrderCompare(o1, o2);
        }
    });
    
    private final boolean m_removeTooFar;
    private final DisplayedScreenElementParameteredComparator m_comparator;
    
    private static int zOrderCompare(final DisplayedScreenElement o1, final DisplayedScreenElement o2) {
        final long z1 = o1.getEntitySprite().m_zOrder;
        final long z2 = o2.getEntitySprite().m_zOrder;
        if (z1 < z2) {
            return 1;
        }
        if (z1 > z2) {
            return -1;
        }
        return 0;
    }
    
    private DisplayedScreenElementComparator(final boolean removeTooFar, final DisplayedScreenElementParameteredComparator comparator) {
        this.m_removeTooFar = removeTooFar;
        this.m_comparator = comparator;
    }
    
    public void sort(final List<DisplayedScreenElement> collection, final DisplayedScreenElementParameteredComparator.Parameters parameters) {
        this.m_comparator.setParameters(parameters);
        Collections.sort(collection, this.m_comparator);
    }
    
    public boolean isRemoveTooFar() {
        return this.m_removeTooFar;
    }
}
