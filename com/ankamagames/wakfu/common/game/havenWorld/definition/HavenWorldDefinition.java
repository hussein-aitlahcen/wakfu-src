package com.ankamagames.wakfu.common.game.havenWorld.definition;

public class HavenWorldDefinition
{
    private final int m_id;
    private final short m_worldInstanceId;
    private final byte m_workers;
    private short m_exitWorldInstanceId;
    private short m_exitCellX;
    private short m_exitCellY;
    private short m_exitCellZ;
    
    public HavenWorldDefinition(final int id, final short worldInstanceId, final byte workers) {
        super();
        this.m_id = id;
        this.m_worldInstanceId = worldInstanceId;
        this.m_workers = workers;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public short getWorldInstanceId() {
        return this.m_worldInstanceId;
    }
    
    public byte getWorkers() {
        return this.m_workers;
    }
    
    public void setExit(final short worldId, final short cellX, final short cellY, final short cellZ) {
        this.m_exitWorldInstanceId = worldId;
        this.m_exitCellX = cellX;
        this.m_exitCellY = cellY;
        this.m_exitCellZ = cellZ;
    }
    
    public short getExitWorldInstanceId() {
        return this.m_exitWorldInstanceId;
    }
    
    public short getExitCellX() {
        return this.m_exitCellX;
    }
    
    public short getExitCellY() {
        return this.m_exitCellY;
    }
    
    public short getExitCellZ() {
        return this.m_exitCellZ;
    }
}
