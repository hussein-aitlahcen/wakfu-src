package com.ankamagames.wakfu.common.game.havenWorld.definition;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class BuildingEvolution implements Comparable<BuildingEvolution>
{
    private final short m_buildingFromId;
    private final short m_buildingToId;
    private final GameIntervalConst m_delay;
    private final short m_catalogEntryId;
    private final byte m_order;
    
    public BuildingEvolution(final short catalogEntryId, final short buildingFromId, final short buildingToId, final long delay, final byte order) {
        super();
        this.m_catalogEntryId = catalogEntryId;
        this.m_buildingFromId = buildingFromId;
        this.m_buildingToId = buildingToId;
        this.m_delay = GameInterval.fromLong(delay);
        this.m_order = order;
    }
    
    public short getCatalogEntryId() {
        return this.m_catalogEntryId;
    }
    
    public short getBuildingFromId() {
        return this.m_buildingFromId;
    }
    
    public short getBuildingToId() {
        return this.m_buildingToId;
    }
    
    public GameIntervalConst getDelay() {
        return this.m_delay;
    }
    
    public boolean waitForResource() {
        return !this.m_delay.isPositive();
    }
    
    @Override
    public int compareTo(final BuildingEvolution o) {
        return this.m_order - o.m_order;
    }
}
