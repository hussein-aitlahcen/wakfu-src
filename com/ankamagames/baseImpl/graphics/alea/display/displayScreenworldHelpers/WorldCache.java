package com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import java.util.*;

abstract class WorldCache<T>
{
    private final ArrayList<Map<T>> _sortedMaps;
    private final DistanceComparator<Map<T>> m_distanceComparator;
    protected final IntObjectLightWeightMap<T> m_mapsCached;
    
    protected WorldCache(final DistanceComparator<Map<T>> distanceComparator) {
        super();
        this._sortedMaps = new ArrayList<Map<T>>();
        this.m_mapsCached = new IntObjectLightWeightMap<T>();
        this.m_distanceComparator = distanceComparator;
    }
    
    public final T getMap(final int key) {
        return this.m_mapsCached.get(key);
    }
    
    public final void put(final int key, final T map) {
        this.m_mapsCached.put(key, map);
    }
    
    public void clear() {
        this.m_mapsCached.clear();
    }
    
    public final void compact(final float centerX, final float centerY, final int maxCacheSize, final ArrayList<T> mapsUsed) {
        final int toRemove = this.m_mapsCached.size() - maxCacheSize;
        if (toRemove <= 0) {
            return;
        }
        this.m_distanceComparator.setCenter(centerX, centerY);
        this._sortedMaps.clear();
        for (int i = this.m_mapsCached.size() - 1; i >= 0; --i) {
            final int key = this.m_mapsCached.getQuickKey(i);
            final T map = this.m_mapsCached.getQuickValue(i);
            this._sortedMaps.add(new Map<T>(key, map));
        }
        Collections.sort(this._sortedMaps, this.m_distanceComparator);
        for (int i = 0; i < toRemove; ++i) {
            final int key = this._sortedMaps.get(i).m_key;
            final T toClean = this._sortedMaps.get(i).m_map;
            assert toClean == this.m_mapsCached.get(key);
            if (!mapsUsed.contains(toClean)) {
                this.m_mapsCached.remove(key);
                this.onRemove(toClean);
            }
        }
    }
    
    protected abstract void onRemove(final T p0);
    
    static final class Map<T>
    {
        final int m_key;
        final T m_map;
        
        Map(final int key, final T map) {
            super();
            this.m_key = key;
            this.m_map = map;
        }
    }
}
