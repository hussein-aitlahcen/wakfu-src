package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.xulor2.component.map.*;
import com.ankamagames.framework.graphics.image.*;

public abstract class WakfuParentMapZoneDescription extends DefaultParentMapZoneDescription<WakfuParentMapZoneDescription>
{
    public WakfuParentMapZoneDescription(final int id, final Color color, final String textDescription, final String iconUrl) {
        super(id, color, textDescription, iconUrl);
    }
    
    public abstract void onLoad(final AbstractMapManager p0);
    
    public boolean canZoomOut() {
        return this.getParent() != null;
    }
    
    public void zoomOut(final AbstractMapManager mapManager) {
        final WakfuParentMapZoneDescription parent = this.getParent();
        if (parent != null) {
            mapManager.setMap(parent);
        }
    }
    
    public abstract boolean canZoomIn();
    
    public abstract void zoomIn(final AbstractMapManager p0);
}
