package com.ankamagames.wakfu.client.core.game.miniMap;

import gnu.trove.*;
import org.jetbrains.annotations.*;

public class MapZoneManager
{
    private static final MapZoneManager m_instance;
    private final TByteObjectHashMap<TIntObjectHashMap<WakfuParentMapZoneDescription>> m_zoneTypes;
    private MapZoneType m_currentType;
    
    private MapZoneManager() {
        super();
        this.m_zoneTypes = new TByteObjectHashMap<TIntObjectHashMap<WakfuParentMapZoneDescription>>();
        this.m_currentType = MapZoneType.TERRITORY;
    }
    
    public static MapZoneManager getInstance() {
        return MapZoneManager.m_instance;
    }
    
    public void addMapZone(final MapZoneType type, final int id, final WakfuParentMapZoneDescription zone) {
        TIntObjectHashMap<WakfuParentMapZoneDescription> zoneMap = this.m_zoneTypes.get(type.m_id);
        if (zoneMap == null) {
            zoneMap = new TIntObjectHashMap<WakfuParentMapZoneDescription>();
            this.m_zoneTypes.put(type.m_id, zoneMap);
        }
        zoneMap.put(id, zone);
    }
    
    public void removeMapZone(final MapZoneType type, final int id) {
        final TIntObjectHashMap<WakfuParentMapZoneDescription> zoneMap = this.m_zoneTypes.get(type.m_id);
        if (zoneMap == null) {
            return;
        }
        zoneMap.remove(id);
    }
    
    public void removeMapZone(final MapZoneType type) {
        this.m_zoneTypes.remove(type.m_id);
    }
    
    public TIntObjectIterator<WakfuParentMapZoneDescription> getCurrentMapZones() {
        final TIntObjectHashMap<WakfuParentMapZoneDescription> zoneMap = this.m_zoneTypes.get(this.m_currentType.m_id);
        if (zoneMap == null) {
            return null;
        }
        return zoneMap.iterator();
    }
    
    @Nullable
    public WakfuParentMapZoneDescription getZone(final MapZoneType type, final int id) {
        final TIntObjectHashMap<WakfuParentMapZoneDescription> map = this.m_zoneTypes.get(type.getId());
        if (map == null) {
            return null;
        }
        return map.get(id);
    }
    
    @Nullable
    public TIntObjectHashMap<WakfuParentMapZoneDescription> getZones(final MapZoneType type) {
        return this.m_zoneTypes.get(type.getId());
    }
    
    public void setCurrentType(final MapZoneType type) {
        this.m_currentType = type;
    }
    
    public MapZoneType getCurrentType() {
        return this.m_currentType;
    }
    
    static {
        m_instance = new MapZoneManager();
    }
    
    public enum MapZoneType
    {
        TERRITORY((byte)0, "territory", true), 
        SUB_INSTANCE((byte)1, "subInstance", true), 
        INSTANCE((byte)2, "instance", true), 
        PARENT_MAP((byte)3, "parentMap", true), 
        FULL_MAP((byte)4, "full", true), 
        DECO((byte)5, "deco", false);
        
        private final byte m_id;
        private final String m_type;
        private final boolean m_isInteractive;
        
        private MapZoneType(final byte id, final String typeName, final boolean interactive) {
            this.m_id = id;
            this.m_type = typeName;
            this.m_isInteractive = interactive;
        }
        
        public byte getId() {
            return this.m_id;
        }
        
        public boolean isInteractive() {
            return this.m_isInteractive;
        }
        
        public static MapZoneType fromName(final String name) {
            for (final MapZoneType type : values()) {
                if (type.m_type.equals(name)) {
                    return type;
                }
            }
            return null;
        }
    }
}
