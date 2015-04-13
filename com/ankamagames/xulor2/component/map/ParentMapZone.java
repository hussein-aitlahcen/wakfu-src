package com.ankamagames.xulor2.component.map;

import java.util.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import gnu.trove.*;
import com.ankamagames.framework.graphics.image.*;

public class ParentMapZone
{
    private final ArrayList<MapZone> m_children;
    private final ParentMapZoneDescription m_zoneDescription;
    private int m_minX;
    private int m_minY;
    private int m_maxX;
    private int m_maxY;
    private int m_centerWorldX;
    private int m_centerWorldY;
    private float m_zoomFactor;
    private Pixmap m_pixmap;
    
    public ParentMapZone(final ParentMapZoneDescription zoneDescription) {
        super();
        this.m_children = new ArrayList<MapZone>();
        final int n = Integer.MAX_VALUE;
        this.m_minY = n;
        this.m_minX = n;
        final int n2 = Integer.MIN_VALUE;
        this.m_maxY = n2;
        this.m_maxX = n2;
        this.m_zoneDescription = zoneDescription;
    }
    
    public ParentMapZoneDescription getZoneDescription() {
        return this.m_zoneDescription;
    }
    
    public void addChild(final MapZone child) {
        this.m_children.add(child);
    }
    
    public void removeChild(final MapZone child) {
        this.m_children.remove(child);
    }
    
    public ArrayList<MapZone> getChildren() {
        return this.m_children;
    }
    
    public void setZoomFactor(final float zoomFactor) {
        this.m_zoomFactor = zoomFactor;
    }
    
    public MapZone.MapZoneElement get(final int x, final int y) {
        for (int i = this.m_children.size() - 1; i >= 0; --i) {
            final MapZone mapZone = this.m_children.get(i);
            if (mapZone.isVisible()) {
                final MapZone.MapZoneElement element = mapZone.get(x, y);
                if (element != null) {
                    return element;
                }
            }
        }
        return null;
    }
    
    public void computeCenter() {
        for (int i = this.m_children.size() - 1; i >= 0; --i) {
            final MapZone mapZone = this.m_children.get(i);
            if (mapZone.isVisible()) {
                this.m_minX = Math.min(this.m_minX, mapZone.getMinX());
                this.m_minY = Math.min(this.m_minY, mapZone.getMinY());
                this.m_maxX = Math.max(this.m_maxX, mapZone.getMaxX());
                this.m_maxY = Math.max(this.m_maxY, mapZone.getMaxY());
            }
        }
    }
    
    public void process(final int deltaTime) {
        for (int i = 0, size = this.m_children.size(); i < size; ++i) {
            final MapZone zone = this.m_children.get(i);
            zone.processAnim(deltaTime);
        }
    }
    
    public float[] getBorderVertices() {
        final TFloatArrayList vertices = new TFloatArrayList();
        final int altitude = (int)(this.m_zoneDescription.getAltitudeAt00() * 10.0f);
        for (int i = 0, size = this.m_children.size(); i < size; ++i) {
            final MapZone mapZone = this.m_children.get(i);
            if (mapZone.isVisible()) {
                for (int x = mapZone.getMinX() - 1; x < mapZone.getMaxX() + 1; ++x) {
                    for (int y = mapZone.getMinY() - 1; y < mapZone.getMaxY() + 1; ++y) {
                        final MapZone.MapZoneElement zone = mapZone.get(x, y);
                        final boolean hasSouthZone = mapZone.get(x, y + 1) == null && this.get(x, y + 1) != null;
                        final boolean hasEastZone = mapZone.get(x + 1, y) == null && this.get(x + 1, y) != null;
                        if (zone != null) {
                            if (hasSouthZone) {
                                vertices.add(zone.m_x1);
                                vertices.add(zone.m_y1 + altitude);
                                vertices.add(zone.m_x4);
                                vertices.add(zone.m_y4 + altitude);
                            }
                            if (hasEastZone) {
                                vertices.add(zone.m_x3);
                                vertices.add(zone.m_y3 + altitude);
                                vertices.add(zone.m_x4);
                                vertices.add(zone.m_y4 + altitude);
                            }
                        }
                    }
                }
            }
        }
        return vertices.toNativeArray();
    }
    
    public float[] getOuterBorderVertices() {
        final float deltaX = 1.0f / this.m_zoomFactor;
        final float deltaY = deltaX / 2.0f;
        final int altitude = (int)(this.m_zoneDescription.getAltitudeAt00() * 10.0f);
        final TFloatArrayList vertices = new TFloatArrayList();
        for (int x = this.m_minX; x < this.m_maxX + 1; ++x) {
            for (int y = this.m_minY; y < this.m_maxY + 1; ++y) {
                final MapZone.MapZoneElement zone = this.get(x, y);
                if (zone != null) {
                    final MapZone.MapZoneElement eastZone = this.get(x + 1, y);
                    final MapZone.MapZoneElement southZone = this.get(x, y + 1);
                    final MapZone.MapZoneElement northZone = this.get(x, y - 1);
                    final MapZone.MapZoneElement westZone = this.get(x - 1, y);
                    final MapZone.MapZoneElement northEastZone = this.get(x + 1, y - 1);
                    final MapZone.MapZoneElement southEastZone = this.get(x + 1, y + 1);
                    final MapZone.MapZoneElement northWestZone = this.get(x - 1, y - 1);
                    final MapZone.MapZoneElement southWestZone = this.get(x - 1, y + 1);
                    float x2 = zone.m_x1 + 2.0f * deltaX + ((westZone != null) ? (-deltaX) : 0.0f) + ((southZone != null) ? (-deltaX) : 0.0f);
                    float y2 = zone.m_y1 + altitude + ((westZone != null) ? deltaY : 0.0f) + ((southZone != null) ? (-deltaY) : 0.0f);
                    float x3 = zone.m_x3 - 2.0f * deltaX - ((eastZone != null) ? (-deltaX) : 0.0f) - ((northZone != null) ? (-deltaX) : 0.0f);
                    float y3 = zone.m_y3 + altitude - ((eastZone != null) ? deltaY : 0.0f) - ((northZone != null) ? (-deltaY) : 0.0f);
                    float x4 = zone.m_x2 + ((westZone != null) ? (-deltaX) : 0.0f) + ((northZone != null) ? deltaX : 0.0f);
                    float y4 = zone.m_y2 + altitude - 2.0f * deltaY + ((westZone != null) ? deltaY : 0.0f) + ((northZone != null) ? deltaY : 0.0f);
                    float x5 = zone.m_x4 + ((eastZone != null) ? deltaX : 0.0f) + ((southZone != null) ? (-deltaX) : 0.0f);
                    float y5 = zone.m_y4 + altitude + 2.0f * deltaY - ((eastZone != null) ? deltaY : 0.0f) - ((southZone != null) ? deltaY : 0.0f);
                    if (northZone == null) {
                        if (northEastZone != null && eastZone != null) {
                            x3 = zone.m_x3;
                            y3 = zone.m_y3 - 2.0f * deltaY + altitude;
                        }
                        if (northWestZone != null && westZone != null) {
                            x4 = zone.m_x2 - 2.0f * deltaX;
                            y4 = zone.m_y2 + altitude;
                        }
                    }
                    if (southZone == null) {
                        if (southWestZone != null && westZone != null) {
                            x2 = zone.m_x1;
                            y2 = zone.m_y1 + 2.0f * deltaY + altitude;
                        }
                        if (southEastZone != null && eastZone != null) {
                            x5 = zone.m_x4 + 2.0f * deltaX;
                            y5 = zone.m_y4 + altitude;
                        }
                    }
                    if (westZone == null) {
                        if (northWestZone != null && northZone != null) {
                            x4 = zone.m_x2 + 2.0f * deltaX;
                            y4 = zone.m_y2 + altitude;
                        }
                        if (southWestZone != null && southZone != null) {
                            x2 = zone.m_x1;
                            y2 = zone.m_y1 - 2.0f * deltaY + altitude;
                        }
                    }
                    if (eastZone == null) {
                        if (northEastZone != null && northZone != null) {
                            x3 = zone.m_x3;
                            y3 = zone.m_y3 + 2.0f * deltaY + altitude;
                        }
                        if (southEastZone != null && southZone != null) {
                            x5 = zone.m_x4 - 2.0f * deltaX;
                            y5 = zone.m_y4 + altitude;
                        }
                    }
                    if (southZone == null) {
                        vertices.add(x2);
                        vertices.add(y2);
                        vertices.add(x5);
                        vertices.add(y5);
                    }
                    if (northZone == null) {
                        vertices.add(x4);
                        vertices.add(y4);
                        vertices.add(x3);
                        vertices.add(y3);
                    }
                    if (eastZone == null) {
                        vertices.add(x3);
                        vertices.add(y3);
                        vertices.add(x5);
                        vertices.add(y5);
                    }
                    if (westZone == null) {
                        vertices.add(x4);
                        vertices.add(y4);
                        vertices.add(x2);
                        vertices.add(y2);
                    }
                }
            }
        }
        return vertices.toNativeArray();
    }
    
    public Color getColor() {
        return this.m_zoneDescription.getZoneColor();
    }
    
    public int getLineWidth() {
        return this.m_zoneDescription.getBorderWidth();
    }
    
    public Pixmap getPixmap() {
        return this.m_pixmap;
    }
    
    public void setPixmap(final Pixmap pixmap) {
        this.m_pixmap = pixmap;
    }
    
    public int getCenterWorldX() {
        return this.m_centerWorldX;
    }
    
    public int getCenterWorldY() {
        return this.m_centerWorldY;
    }
    
    public void cleanUp() {
        for (int i = this.m_children.size() - 1; i >= 0; --i) {
            final MapZone mapZone = this.m_children.get(i);
            if (mapZone != null && mapZone.getPixmap() != null && mapZone.getPixmap().getTexture() != null) {
                mapZone.cleanUp();
            }
        }
    }
}
