package com.ankamagames.baseImpl.common.clientAndServer.world.topology;

import java.util.*;

public class TopologyMapInstanceSet
{
    protected final ArrayList<TopologyMapInstance> m_maps;
    protected int m_minX;
    protected int m_minY;
    protected int m_maxX;
    protected int m_maxY;
    protected int m_width;
    protected int m_height;
    
    public TopologyMapInstanceSet() {
        super();
        this.m_maps = new ArrayList<TopologyMapInstance>();
        this.reset();
    }
    
    public TopologyMap getTopologyMapFromCell(final int x, final int y) {
        if (x < this.m_minX || x >= this.m_minX + this.m_width) {
            return null;
        }
        if (y < this.m_minY || y >= this.m_minY + this.m_height) {
            return null;
        }
        final TopologyMapInstance mapInstance = this.getMap(x, y);
        if (mapInstance != null) {
            return mapInstance.getTopologyMap();
        }
        return null;
    }
    
    public boolean isInMap(final int x, final int y) {
        return x >= this.m_minX && x < this.m_minX + this.m_width && y >= this.m_minY && y < this.m_minY + this.m_height;
    }
    
    public TopologyMapInstance getMap(final int x, final int y) {
        final int mapIndex = this.getMapIndex(x, y);
        if (mapIndex < 0 || mapIndex >= this.m_maps.size()) {
            return null;
        }
        return this.m_maps.get(mapIndex);
    }
    
    public boolean isMovementBlocked(final int x, final int y, final short z) {
        final TopologyMapInstance map = this.getMap(x, y);
        return map == null || map.isBlocked(x, y, z);
    }
    
    public boolean isSightBlocked(final int x, final int y, final short z) {
        final TopologyMapInstance map = this.getMap(x, y);
        return map == null || map.isBlocked(x, y, z);
    }
    
    public int getMinX() {
        return this.m_minX;
    }
    
    public int getMinY() {
        return this.m_minY;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    public int getMaxX() {
        return this.getMinX() + this.getWidth() - 1;
    }
    
    public int getMaxY() {
        return this.getMinY() + this.getHeight() - 1;
    }
    
    public void reset() {
        this.m_maps.clear();
        this.m_minX = Integer.MAX_VALUE;
        this.m_minY = Integer.MAX_VALUE;
        this.m_maxX = Integer.MIN_VALUE;
        this.m_maxY = Integer.MIN_VALUE;
        this.m_width = 0;
        this.m_height = 0;
    }
    
    public void addMap(final TopologyMapInstance mapInstance, int mapX, int mapY) {
        this.m_maps.add(mapInstance);
        mapX *= 18;
        mapY *= 18;
        if (mapX < this.m_minX) {
            this.m_minX = mapX;
        }
        if (mapX > this.m_maxX) {
            this.m_maxX = mapX;
        }
        if (mapY < this.m_minY) {
            this.m_minY = mapY;
        }
        if (mapY > this.m_maxY) {
            this.m_maxY = mapY;
        }
        this.m_width = 18 + this.m_maxX - this.m_minX;
        this.m_height = 18 + this.m_maxY - this.m_minY;
        assert this.m_maps.size() < 100 : "C'est pas un peu abus\u00e9, comme taille : " + this.m_maps.size() + " ?????";
        assert this.getMapIndex(mapX, mapY) == this.m_maps.size() - 1 : " Map ajout\u00e9e non valide. index : " + this.getMapIndex(mapX, mapY) + " attendu : " + (this.m_maps.size() - 1);
    }
    
    protected int getMapIndex(final int x, final int y) {
        if (x < this.m_minX) {
            return -1;
        }
        if (y < this.m_minY) {
            return -1;
        }
        final int mapX = (x - this.m_minX) / 18;
        final int mapY = (y - this.m_minY) / 18;
        assert mapX >= 0;
        assert mapY >= 0;
        return mapY * (this.m_width / 18) + mapX;
    }
}
