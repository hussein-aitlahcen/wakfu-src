package com.ankamagames.wakfu.common.game.group.partySearch;

public final class PartySearchConstants
{
    public static final byte MAX_SEARCH_SIZE = 30;
    public static final byte MAX_DESCRIPTION_SIZE = 80;
    public static final byte BASIC_LEVEL_DIFFERENCE = 20;
    public static final byte MAX_OCCUPATION_NUMBER = 20;
    public static final PartyRole DEFAULT_PARTY_ROLE;
    public static final short DUNGEON_FAMILY_LEVEL_DELTA = 5;
    
    static {
        DEFAULT_PARTY_ROLE = PartyRole.DAMAGE_DEALER;
    }
}
