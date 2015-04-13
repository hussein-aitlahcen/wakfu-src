package com.ankamagames.wakfu.common.game.guild.storage;

import java.util.*;
import org.apache.commons.lang3.*;

public enum GuildStorageCompartmentUnlockType
{
    HAVEN_WORLD_GUILD((byte)0, new GuildStorageCompartmentType[] { GuildStorageCompartmentType.GUILD_1, GuildStorageCompartmentType.GUILD_2, GuildStorageCompartmentType.GUILD_3, GuildStorageCompartmentType.GUILD_4 }), 
    HAVEN_WORLD_HOUSE((byte)1, new GuildStorageCompartmentType[] { GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_1, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_2, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_3, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_4, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_5, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_6, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_7, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_8 }), 
    HAVEN_WORLD_MANSION((byte)2, new GuildStorageCompartmentType[] { GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_1, GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_2, GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_3, GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_4 }), 
    COLLECT((byte)3, new GuildStorageCompartmentType[] { GuildStorageCompartmentType.COLLECT });
    
    private final byte m_id;
    private final GuildStorageCompartmentType[] m_types;
    
    private GuildStorageCompartmentUnlockType(final byte id, final GuildStorageCompartmentType[] types) {
        this.m_id = id;
        this.m_types = types;
    }
    
    static void init() {
        final GuildStorageCompartmentUnlockType guild = GuildStorageCompartmentUnlockType.HAVEN_WORLD_GUILD;
    }
    
    public static GuildStorageCompartmentUnlockType fromId(final byte id) {
        for (final GuildStorageCompartmentUnlockType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public GuildStorageCompartmentType[] getTypes() {
        return this.m_types;
    }
    
    public static GuildStorageCompartmentType getBaseComportmentType(final GuildStorageCompartmentUnlockType unlock, final ArrayList<GuildStorageCompartmentType> types) {
        for (int i = 0, size = types.size(); i < size; ++i) {
            final GuildStorageCompartmentType type = types.get(i);
            if (ArrayUtils.contains(unlock.getTypes(), type)) {
                return type;
            }
        }
        return null;
    }
    
    static {
        for (final GuildStorageCompartmentUnlockType unlockType : values()) {
            for (final GuildStorageCompartmentType type : unlockType.getTypes()) {
                type.setUnlockType(unlockType);
            }
        }
    }
}
