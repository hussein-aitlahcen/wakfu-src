package com.ankamagames.wakfu.common.game.guild;

import java.util.*;

public enum GuildRankAuthorisation
{
    ADD_MEMBER(1), 
    REMOVE_MEMBER(2, true), 
    CHANGE_MEMBER_RANK(3, true), 
    EDIT_RANK(4, true), 
    REMOVE_CHEST_ITEM(5), 
    CHANGE_GUILD_DESCRIPTION(6), 
    CHANGE_GUILD_MESSAGE(7), 
    REMOVE_CHEST_MONEY(8), 
    MANAGE_HAVEN_WORLD(9), 
    BUY_BONUS(10), 
    MANAGE_GUILD_CHEST_1(11), 
    MANAGE_GUILD_CHEST_2(12), 
    MANAGE_GUILD_CHEST_3(13), 
    MANAGE_GUILD_CHEST_4(14), 
    MANAGE_HOUSE_CHEST_1(15), 
    MANAGE_HOUSE_CHEST_2(16), 
    MANAGE_HOUSE_CHEST_3(17), 
    MANAGE_HOUSE_CHEST_4(18), 
    MANAGE_HOUSE_CHEST_5(19), 
    MANAGE_HOUSE_CHEST_6(20), 
    MANAGE_HOUSE_CHEST_7(21), 
    MANAGE_HOUSE_CHEST_8(22), 
    MANAGE_MANSION_CHEST_1(23), 
    MANAGE_MANSION_CHEST_2(24), 
    MANAGE_MANSION_CHEST_3(25), 
    MANAGE_MANSION_CHEST_4(26), 
    MANAGE_COLLECT_CHEST(27);
    
    public static final long ALL;
    public static final long NONE;
    public final byte id;
    public final boolean m_positionDependant;
    
    private GuildRankAuthorisation(final int idx) {
        this(idx, false);
    }
    
    private GuildRankAuthorisation(final int idx, final boolean positionDependant) {
        this.id = (byte)idx;
        this.m_positionDependant = positionDependant;
    }
    
    public boolean hasAuthorisationConcerningPosition(final short sourcePosition, final short targetPosition) {
        return !this.m_positionDependant || sourcePosition == 0 || sourcePosition < targetPosition;
    }
    
    public static long longValueOf(final Iterable<GuildRankAuthorisation> set) {
        long longRepresentation = 0L;
        final Iterator<GuildRankAuthorisation> it = set.iterator();
        while (it.hasNext()) {
            final int mask = 1 << it.next().id;
            longRepresentation |= mask;
        }
        return longRepresentation;
    }
    
    public static EnumSet<GuildRankAuthorisation> setValueOf(final long authorisations) {
        final EnumSet<GuildRankAuthorisation> set = EnumSet.noneOf(GuildRankAuthorisation.class);
        final GuildRankAuthorisation[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final GuildRankAuthorisation auth = values[i];
            final int mask = 1 << auth.id;
            if ((authorisations & mask) == mask) {
                set.add(auth);
            }
        }
        return set;
    }
    
    static {
        ALL = longValueOf(EnumSet.allOf(GuildRankAuthorisation.class));
        NONE = longValueOf(EnumSet.noneOf(GuildRankAuthorisation.class));
    }
}
