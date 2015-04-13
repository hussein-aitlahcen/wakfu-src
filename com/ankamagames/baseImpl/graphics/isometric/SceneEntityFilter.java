package com.ankamagames.baseImpl.graphics.isometric;

import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class SceneEntityFilter
{
    public static final SceneEntityFilter DEFAULT;
    
    public void clear() {
    }
    
    public boolean acceptEntity(final Entity entity, final boolean sorted) {
        return true;
    }
    
    public boolean accept(final AnimatedElement animatedElement) {
        return true;
    }
    
    public void addEntities(final IsoWorldScene scene, final ArrayList<DisplayedScreenElement> elements) {
        for (int i = 0, size = elements.size(); i < size; ++i) {
            scene.addEntity(elements.get(i).getEntitySprite(), true);
        }
    }
    
    static {
        DEFAULT = new SceneEntityFilter();
    }
}
