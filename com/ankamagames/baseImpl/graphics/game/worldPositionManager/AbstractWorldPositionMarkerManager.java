package com.ankamagames.baseImpl.graphics.game.worldPositionManager;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;

public abstract class AbstractWorldPositionMarkerManager implements RenderProcessHandler
{
    public static int[] APS_IDS;
    private boolean m_visible;
    private boolean m_newVisible;
    private IsoWorldScene m_scene;
    private final IntObjectLightWeightMap<LongObjectLightWeightMap<ValuePoint>> m_points;
    private ValuePointManager m_manager;
    private final ArrayList<SceneLoadedListener> m_sceneLoadedListeners;
    
    protected AbstractWorldPositionMarkerManager() {
        super();
        this.m_visible = true;
        this.m_newVisible = true;
        this.m_points = new IntObjectLightWeightMap<LongObjectLightWeightMap<ValuePoint>>();
        this.m_sceneLoadedListeners = new ArrayList<SceneLoadedListener>();
    }
    
    public void setValuePointManager(final ValuePointManager manager) {
        this.m_manager = manager;
    }
    
    public void setScene(final IsoWorldScene scene) {
        this.m_scene = scene;
    }
    
    public boolean isVisible() {
        return this.m_newVisible;
    }
    
    public void setVisible(final boolean visible) {
        this.m_newVisible = visible;
    }
    
    protected void setScreenTarget(final int type, final long id, final IsoWorldTarget target) {
        final LongObjectLightWeightMap<ValuePoint> points = this.m_points.get(type);
        if (points == null) {
            return;
        }
        final ValuePoint point = points.get(id);
        if (point == null) {
            return;
        }
        point.setTarget(target);
    }
    
    public boolean hasPoint(final int type, final long id) {
        final LongObjectLightWeightMap<ValuePoint> map = this.m_points.get(type);
        return map != null && map.contains(id);
    }
    
    public boolean hasPoint(final int type) {
        final LongObjectLightWeightMap<ValuePoint> map = this.m_points.get(type);
        return map != null && map.size() != 0;
    }
    
    public void setPoint(final int type, final long id, final int x, final int y, final int z, final Object value, final ValuePointDeleteListener deleteListener, final boolean forceZ) {
        this.removeAll();
        this.addPoint(type, id, x, y, z, value, deleteListener, forceZ);
    }
    
    private void addPoint(final int type, final long id, final int x, final int y, final int z, final Object value, final ValuePointDeleteListener deleteListener, final boolean forceZ) {
        LongObjectLightWeightMap<ValuePoint> map = this.m_points.get(type);
        if (map == null) {
            map = new LongObjectLightWeightMap<ValuePoint>();
            this.m_points.put(type, map);
        }
        ValuePoint point = map.get(id);
        if (point != null) {
            this.updatePosition(point, x, y, z, false);
            point.setValue(value);
        }
        else {
            point = this.createPoint(type, this.m_manager, value, AbstractWorldPositionMarkerManager.APS_IDS[type], x, y, z, forceZ);
            point.addToManager();
            this.updatePosition(point, x, y, z, true);
            map.put(id, point);
        }
        point.setDeleteListener(deleteListener);
    }
    
    protected abstract ValuePoint createPoint(final int p0, final ValuePointManager p1, final Object p2, final int p3, final int p4, final int p5, final int p6, final boolean p7);
    
    public void updatePosition(final int type, final long id, final int x, final int y, final int z) {
        final LongObjectLightWeightMap<ValuePoint> map = this.m_points.get(type);
        if (map == null) {
            return;
        }
        final ValuePoint point = map.get(id);
        if (point == null) {
            return;
        }
        this.updatePosition(point, x, y, z, false);
    }
    
    private void updatePosition(final ValuePoint point, final int x, final int y, final int z, final boolean set) {
        if (this.m_scene == null) {
            this.m_sceneLoadedListeners.add(new SceneLoadedListener() {
                @Override
                public void onScenedLoaded() {
                    System.out.println("ON REJOUE UPDATE POSITION");
                    AbstractWorldPositionMarkerManager.this.updatePosition(point, x, y, z, set);
                    AbstractWorldPositionMarkerManager.this.m_sceneLoadedListeners.remove(this);
                }
            });
            return;
        }
        final Point2 pt = IsoCameraFunc.getScreenPosition(this.m_scene, x, y, z);
        if (set) {
            point.setPosition(x, y, z);
            point.setScreenPosition((int)pt.m_x, (int)pt.m_y);
        }
        else {
            point.updatePosition(x, y, z);
            point.updateScreenPosition((int)pt.m_x, (int)pt.m_y);
        }
    }
    
    public void removePoint(final int type, final long id) {
        final LongObjectLightWeightMap<ValuePoint> map = this.m_points.get(type);
        if (map == null) {
            return;
        }
        final ValuePoint valuePoint = map.remove(id);
        if (valuePoint != null) {
            valuePoint.clear();
        }
    }
    
    public void removeAllOfType(final int type) {
        final LongObjectLightWeightMap<ValuePoint> map = this.m_points.get(type);
        if (map == null) {
            return;
        }
        for (int i = map.size() - 1; i >= 0; --i) {
            map.getQuickValue(i).clear();
        }
        map.clear();
    }
    
    public void removeAll() {
        for (int i = 0, size = this.m_points.size(); i < size; ++i) {
            this.removeAllOfType(this.m_points.getQuickKey(i));
        }
        this.m_points.clear();
    }
    
    @Override
    public void process(final IsoWorldScene scene, final int deltaTime) {
        final IsoWorldScene previousScene = this.m_scene;
        this.m_scene = scene;
        if (previousScene == null && this.m_scene != null) {
            this.OnScenedLoaded();
        }
        boolean justBecameVisible = false;
        if (this.m_newVisible != this.m_visible) {
            this.m_visible = this.m_newVisible;
            if (this.m_visible) {
                justBecameVisible = true;
            }
            else {
                for (int type = 0, mapSize = this.m_points.size(); type < mapSize; ++type) {
                    final LongObjectLightWeightMap<ValuePoint> map = this.m_points.getQuickValue(type);
                    for (int i = 0, size = map.size(); i < size; ++i) {
                        map.getQuickValue(i).clear();
                    }
                }
            }
        }
        if (this.m_visible) {
            this.computePointDisplaying(scene, deltaTime, justBecameVisible);
        }
    }
    
    private void OnScenedLoaded() {
        for (int i = this.m_sceneLoadedListeners.size() - 1; i >= 0; --i) {
            this.m_sceneLoadedListeners.get(i).onScenedLoaded();
        }
    }
    
    private void computePointDisplaying(final IsoWorldScene scene, final int deltaTime, final boolean justBecameVisible) {
        final IsoCamera camera = scene.getIsoCamera();
        final IsoWorldTarget trackingTarget = camera.getTrackingTarget();
        for (int type = 0, mapSize = this.m_points.size(); type < mapSize; ++type) {
            final LongObjectLightWeightMap<ValuePoint> map = this.m_points.getQuickValue(type);
            for (int i = map.size() - 1; i >= 0; --i) {
                final ValuePoint point = map.getQuickValue(i);
                short nearestZ = (short)(point.isForceZ() ? -32768 : TopologyMapManager.getNearestZ(point.getX(), point.getY(), (short)point.getZ()));
                if (nearestZ == -32768) {
                    nearestZ = (short)point.getZ();
                }
                this.updatePosition(point, point.getX(), point.getY(), nearestZ, false);
                point.process(deltaTime);
                final float dx = trackingTarget.getWorldX() - point.getX();
                final float dy = trackingTarget.getWorldY() - point.getY();
                final float dist2 = Vector2.sqrLength(dx, dy);
                if (dist2 < 4.0f) {
                    this.removeAll();
                    return;
                }
                final int screenX = point.getScreenX();
                final int screenY = point.getScreenY();
                final boolean inScreen = camera.isVisibleInScreen(screenY, screenX, screenY, screenX);
                point.setOnScreen(inScreen, justBecameVisible);
            }
        }
    }
    
    @Override
    public void prepareBeforeRendering(final IsoWorldScene isoWorldScene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
    }
    
    private interface SceneLoadedListener
    {
        void onScenedLoaded();
    }
}
