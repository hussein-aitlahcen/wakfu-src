package com.ankamagames.xulor2.component.mapOverlay;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.kernel.core.common.*;

public class MapOverlayPointList
{
    private static final Logger m_logger;
    private final ArrayList<DisplayableMapPoint> m_points;
    private final ArrayList<EntitySprite> m_meshes;
    private final ArrayList<EntitySprite> m_overlayMeshes;
    
    public MapOverlayPointList() {
        super();
        this.m_points = new ArrayList<DisplayableMapPoint>();
        this.m_meshes = new ArrayList<EntitySprite>();
        this.m_overlayMeshes = new ArrayList<EntitySprite>();
    }
    
    public void addAll(final ArrayList<DisplayableMapPoint> item) {
        this.m_points.clear();
        this.m_points.addAll(item);
        this.updateMeshList();
    }
    
    public DisplayableMapPoint getPoint(final int index) {
        return this.m_points.get(index);
    }
    
    public EntitySprite getMesh(final int index) {
        return this.m_meshes.get(index);
    }
    
    public EntitySprite getOverlayMesh(final int index) {
        return this.m_overlayMeshes.get(index);
    }
    
    public void add(final DisplayableMapPoint item) {
        this.m_points.add(item);
        this.m_meshes.add(createSprite(this));
        this.m_overlayMeshes.add(createSprite(this));
        assert this.m_meshes.size() == this.m_points.size();
    }
    
    public void remove(final DisplayableMapPoint item) {
        final int index = this.m_points.indexOf(item);
        if (index != -1) {
            this.m_points.remove(index);
            this.m_meshes.remove(index).removeReference();
            this.m_overlayMeshes.remove(index).removeReference();
        }
        assert this.m_meshes.size() == this.m_points.size();
    }
    
    public int size() {
        return this.m_points.size();
    }
    
    public void clear() {
        this.m_points.clear();
        for (int i = this.m_meshes.size() - 1; i >= 0; --i) {
            this.m_meshes.remove(i).removeReference();
        }
        this.m_meshes.clear();
        assert this.m_meshes.size() == this.m_points.size();
    }
    
    private void updateMeshList() {
        final int count = this.m_points.size();
        assert this.m_meshes.size() == this.m_overlayMeshes.size();
        while (this.m_meshes.size() > count) {
            this.m_meshes.remove(this.m_meshes.size() - 1).removeReference();
            this.m_overlayMeshes.remove(this.m_overlayMeshes.size() - 1).removeReference();
        }
        for (int i = this.m_meshes.size(); i < count; ++i) {
            final EntitySprite meshSprite = createSprite("MapOverlay");
            final EntitySprite overlaySprite = createSprite("MapOverlay");
            this.m_meshes.add(meshSprite);
            this.m_overlayMeshes.add(overlaySprite);
        }
        assert this.m_meshes.size() == this.m_points.size() && this.m_meshes.size() == this.m_overlayMeshes.size();
    }
    
    private static EntitySprite createSprite(final Object owner) {
        final EntitySprite entitySprite = EntitySprite.Factory.newPooledInstance();
        entitySprite.m_owner = owner;
        final GeometrySprite geom = ((MemoryObject.ObjectFactory<GeometrySprite>)GLGeometrySprite.Factory).newPooledInstance();
        entitySprite.setGeometry(geom);
        geom.removeReference();
        return entitySprite;
    }
    
    public void translateMeshes(final float x, final float y) {
        for (int i = 0, size = this.m_meshes.size(); i < size; ++i) {
            final EntitySprite sprite = this.m_meshes.get(i);
            sprite.setTopLeft(sprite.getTop() - y, sprite.getLeft() - x);
            final EntitySprite sprite2 = this.m_overlayMeshes.get(i);
            sprite2.setTopLeft(sprite2.getTop() - y, sprite2.getLeft() - x);
        }
    }
    
    public MapOverlayPoint getMapOverlayPoint(final int index) {
        if (index < 0 || index >= this.m_points.size()) {
            return null;
        }
        return new MapOverlayPoint(this.m_points.get(index), this.m_meshes.get(index), this.m_overlayMeshes.get(index));
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapOverlayPointList.class);
    }
}
