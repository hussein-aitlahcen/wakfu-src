package com.ankamagames.wakfu.common.game.characteristics.skill;

public enum CraftSkillType
{
    CRAFT_QUICKNESS((byte)1), 
    CRAFT_CRAFT_XP_BOOST((byte)2), 
    CRAFT_ECOSYSTEM_XP_BOOST((byte)3);
    
    private final byte m_id;
    
    private CraftSkillType(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static CraftSkillType getById(final byte id) {
        final CraftSkillType[] types = values();
        for (int i = 0; i < types.length; ++i) {
            final CraftSkillType type = types[i];
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
}
