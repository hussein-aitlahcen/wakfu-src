package com.ankamagames.wakfu.common.game.guild.storage;

import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public enum GuildStorageCompartmentType
{
    GUILD_1(0, 40), 
    GUILD_2(1, 40), 
    GUILD_3(2, 40), 
    GUILD_4(3, 40), 
    HAVEN_WORLD_HOUSE_1_1(4, 5), 
    HAVEN_WORLD_HOUSE_2_1(5, 7), 
    HAVEN_WORLD_HOUSE_3_1(6, 8), 
    HAVEN_WORLD_HOUSE_1_2(7, 5), 
    HAVEN_WORLD_HOUSE_2_2(8, 7), 
    HAVEN_WORLD_HOUSE_3_2(9, 8), 
    HAVEN_WORLD_HOUSE_1_3(10, 5), 
    HAVEN_WORLD_HOUSE_2_3(11, 7), 
    HAVEN_WORLD_HOUSE_3_3(12, 8), 
    HAVEN_WORLD_HOUSE_1_4(13, 5), 
    HAVEN_WORLD_HOUSE_2_4(14, 7), 
    HAVEN_WORLD_HOUSE_3_4(15, 8), 
    HAVEN_WORLD_HOUSE_1_5(16, 5), 
    HAVEN_WORLD_HOUSE_2_5(17, 7), 
    HAVEN_WORLD_HOUSE_3_5(18, 8), 
    HAVEN_WORLD_HOUSE_1_6(19, 5), 
    HAVEN_WORLD_HOUSE_2_6(20, 7), 
    HAVEN_WORLD_HOUSE_3_6(21, 8), 
    HAVEN_WORLD_HOUSE_1_7(22, 5), 
    HAVEN_WORLD_HOUSE_2_7(23, 7), 
    HAVEN_WORLD_HOUSE_3_7(24, 8), 
    HAVEN_WORLD_HOUSE_1_8(25, 5), 
    HAVEN_WORLD_HOUSE_2_8(26, 7), 
    HAVEN_WORLD_HOUSE_3_8(27, 8), 
    HAVEN_WORLD_MANSION_1_1(28, 20), 
    HAVEN_WORLD_MANSION_2_1(29, 30), 
    HAVEN_WORLD_MANSION_1_2(30, 20), 
    HAVEN_WORLD_MANSION_2_2(31, 30), 
    HAVEN_WORLD_MANSION_1_3(32, 20), 
    HAVEN_WORLD_MANSION_2_3(33, 30), 
    HAVEN_WORLD_MANSION_1_4(34, 20), 
    HAVEN_WORLD_MANSION_2_4(35, 30), 
    COLLECT(36, 40);
    
    public static final Map<GuildStorageCompartmentType, GuildStorageCompartmentType> EVOLUTIONS;
    public final int m_id;
    public final short m_size;
    private GuildStorageCompartmentLinkType m_linkType;
    private GuildStorageCompartmentUnlockType m_unlockType;
    
    private GuildStorageCompartmentType(final int id, final int size) {
        this.m_id = id;
        this.m_size = MathHelper.ensureShort(size);
    }
    
    public static GuildStorageCompartmentType getFromId(final int id) {
        for (final GuildStorageCompartmentType type : values()) {
            if (id == type.m_id) {
                return type;
            }
        }
        return null;
    }
    
    void setLinkType(final GuildStorageCompartmentLinkType link) {
        this.m_linkType = link;
    }
    
    void setUnlockType(final GuildStorageCompartmentUnlockType unlockType) {
        this.m_unlockType = unlockType;
    }
    
    public GuildStorageCompartmentLinkType getLinkType() {
        return this.m_linkType;
    }
    
    public GuildStorageCompartmentUnlockType getUnlockType() {
        return this.m_unlockType;
    }
    
    static {
        final Map<GuildStorageCompartmentType, GuildStorageCompartmentType> evolutions = new EnumMap<GuildStorageCompartmentType, GuildStorageCompartmentType>(GuildStorageCompartmentType.class);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_1, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_1);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_1, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_1);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_2, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_2);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_2, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_2);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_3, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_3);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_3, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_3);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_4, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_4);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_4, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_4);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_5, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_5);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_5, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_5);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_6, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_6);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_6, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_6);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_7, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_7);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_7, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_7);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_8, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_8);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_8, GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_8);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_1, GuildStorageCompartmentType.HAVEN_WORLD_MANSION_2_1);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_2, GuildStorageCompartmentType.HAVEN_WORLD_MANSION_2_2);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_3, GuildStorageCompartmentType.HAVEN_WORLD_MANSION_2_3);
        evolutions.put(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_4, GuildStorageCompartmentType.HAVEN_WORLD_MANSION_2_4);
        EVOLUTIONS = Collections.unmodifiableMap((Map<? extends GuildStorageCompartmentType, ? extends GuildStorageCompartmentType>)evolutions);
        GuildStorageCompartmentUnlockType.init();
        GuildStorageCompartmentLinkType.init();
    }
}
