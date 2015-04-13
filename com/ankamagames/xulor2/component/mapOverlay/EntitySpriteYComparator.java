package com.ankamagames.xulor2.component.mapOverlay;

import java.util.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public class EntitySpriteYComparator implements Comparator<Entity>
{
    public static final EntitySpriteYComparator COMPARATOR;
    
    @Override
    public int compare(final Entity o1, final Entity o2) {
        if (o1 instanceof XulorParticleSystem) {
            return -1;
        }
        if (o2 instanceof XulorParticleSystem) {
            return 1;
        }
        final float y1 = ((EntitySprite)o1).getCenterY();
        final float y2 = ((EntitySprite)o2).getCenterY();
        return (int)(y2 - y1);
    }
    
    static {
        COMPARATOR = new EntitySpriteYComparator();
    }
}
