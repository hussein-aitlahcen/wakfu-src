package com.ankamagames.wakfu.common.datas.Breed;

import com.ankamagames.framework.external.*;

public enum MonsterRankConstants implements ExportableEnum
{
    NONE(0, "Aucun"), 
    YOUNG(1, "Mob juv\u00e9nile"), 
    ADULT_MAIN(2, "Mob adulte (type1)"), 
    ADULT_SECONDARY(3, "Mob adulte (type2)"), 
    TEAM_LEADER(4, "Mob chef de groupe"), 
    FAMILY_LEADER(5, "Mob chef de famille"), 
    EXTRA(6, "Extra mob"), 
    CORRUPT(7, "Mob corrompu"), 
    LITTLE_GOD(8, "Mob petit dieu"), 
    UNDEAD(9, "Mob mort-vivant"), 
    CHALLENGE(10, "Mob de challenge");
    
    private byte m_id;
    private String m_label;
    
    private MonsterRankConstants(final int id, final String label) {
        this.m_id = (byte)id;
        this.m_label = label;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static MonsterRankConstants getRankById(final byte rank) {
        for (final MonsterRankConstants constant : values()) {
            if (constant.getId() == rank) {
                return constant;
            }
        }
        return null;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
}
