package com.ankamagames.wakfu.common.game.havenWorld.buff;

import gnu.trove.*;

public class HavenWorldZoneBuffsManager
{
    private final TShortObjectHashMap<HavenWorldZoneBuffs> m_buffs;
    public static final HavenWorldZoneBuffsManager INSTANCE;
    
    private HavenWorldZoneBuffsManager() {
        super();
        this.m_buffs = new TShortObjectHashMap<HavenWorldZoneBuffs>();
    }
    
    public HavenWorldZoneBuffs getBuffs(final short worldId) {
        HavenWorldZoneBuffs havenWorldZoneBuffs = this.m_buffs.get(worldId);
        if (havenWorldZoneBuffs == null) {
            havenWorldZoneBuffs = this.createBuffs(worldId);
        }
        return havenWorldZoneBuffs;
    }
    
    public HavenWorldZoneBuffs resetBuffs(final short worldId) {
        return this.createBuffs(worldId);
    }
    
    private HavenWorldZoneBuffs createBuffs(final short worldId) {
        final HavenWorldZoneBuffs havenWorldZoneBuffs;
        this.m_buffs.put(worldId, havenWorldZoneBuffs = new HavenWorldZoneBuffs());
        return havenWorldZoneBuffs;
    }
    
    static {
        INSTANCE = new HavenWorldZoneBuffsManager();
    }
}
