package com.ankamagames.wakfu.common.game.dungeon;

public class DungeonLadderDefinition
{
    private final short m_instanceId;
    private final DungeonLadderType m_ladderType;
    private final short m_maxDungeonLevel;
    
    public DungeonLadderDefinition(final short instanceId, final DungeonLadderType ladderType, final short maxDungeonLevel) {
        super();
        this.m_instanceId = instanceId;
        this.m_ladderType = ladderType;
        this.m_maxDungeonLevel = maxDungeonLevel;
    }
    
    public short getInstanceId() {
        return this.m_instanceId;
    }
    
    public DungeonLadderType getLadderType() {
        return this.m_ladderType;
    }
    
    public short getMaxDungeonLevel() {
        return this.m_maxDungeonLevel;
    }
}
