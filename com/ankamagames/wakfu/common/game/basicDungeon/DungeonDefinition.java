package com.ankamagames.wakfu.common.game.basicDungeon;

import gnu.trove.*;

public class DungeonDefinition
{
    private final int m_id;
    private final short m_minLevel;
    private final short m_instanceId;
    private final TIntArrayList m_tps;
    
    public DungeonDefinition(final int id, final short minLevel, final short instanceId, final int[] tps) {
        super();
        this.m_tps = new TIntArrayList();
        this.m_id = id;
        this.m_minLevel = minLevel;
        this.m_instanceId = instanceId;
        this.m_tps.add(tps);
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public short getInstanceId() {
        return this.m_instanceId;
    }
    
    public short getMinLevel() {
        return this.m_minLevel;
    }
    
    public boolean forEachDungeon(final TIntProcedure procedure) {
        return this.m_tps.forEach(procedure);
    }
}
