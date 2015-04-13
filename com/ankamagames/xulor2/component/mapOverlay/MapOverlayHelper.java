package com.ankamagames.xulor2.component.mapOverlay;

import com.ankamagames.framework.graphics.engine.entity.*;

public class MapOverlayHelper
{
    public static boolean isHit(final EntitySprite sprite, final int x, final int y) {
        return sprite.getLeft() <= x && sprite.getRight() >= x && sprite.getBottom() <= y && sprite.getTop() >= y;
    }
}
