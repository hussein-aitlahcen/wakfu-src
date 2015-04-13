package com.ankamagames.xulor2.component.mapOverlay;

import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public class MapOverlayPoint
{
    public final DisplayableMapPoint m_point;
    public final EntitySprite m_mesh;
    public final EntitySprite m_overlayMesh;
    private byte m_refCount;
    
    public MapOverlayPoint(final DisplayableMapPoint point, final EntitySprite mesh, final EntitySprite overlayMesh) {
        super();
        this.m_refCount = 0;
        this.m_point = point;
        this.m_mesh = mesh;
        this.m_overlayMesh = overlayMesh;
    }
    
    public Object getValue() {
        return this.m_point.getValue();
    }
    
    public void addReference() {
        this.m_mesh.addReference();
        this.m_overlayMesh.addReference();
        ++this.m_refCount;
    }
    
    public void removeReference() {
        if (this.m_refCount > 0) {
            this.m_mesh.removeReference();
            this.m_overlayMesh.removeReference();
        }
        --this.m_refCount;
    }
}
