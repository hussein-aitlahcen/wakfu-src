package com.ankamagames.wakfu.common.game.group.partySearch;

public class PvePartyOccupation extends PartyOccupation
{
    private final short m_level;
    private final int m_monsterType;
    
    public PvePartyOccupation(final int id, final int referenceId, final PartyOccupationType type, final short level, final int monsterType) {
        super(id, referenceId, type);
        this.m_level = level;
        this.m_monsterType = monsterType;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public int getMonsterType() {
        return this.m_monsterType;
    }
    
    @Override
    public String toString() {
        return "PvePartyOccupation{m_level=" + this.m_level + '}';
    }
}
