package com.ankamagames.wakfu.common.account.subscription;

import java.util.*;

enum SubscriptionRightsSet
{
    NO_RIGHTS(EnumSet.noneOf(SubscriptionRight.class)), 
    EU_FREE(EnumSet.of(SubscriptionRight.FULL_REGEN_WITH_ITEM, SubscriptionRight.PVP_AGGRESSION, SubscriptionRight.CRAFT, SubscriptionRight.USE_COMPANION, SubscriptionRight.ACCESS_TO_MARKET_PLACE, SubscriptionRight.ALL_EMOTES, SubscriptionRight.NO_DUNGEON_DAILY_LIMITATION, SubscriptionRight.DUEL)), 
    EU_SUBSCRIBER_RIGHTS(EnumSet.of(SubscriptionRight.FULL_REGEN_WITH_ITEM, SubscriptionRight.PVP_AGGRESSION, SubscriptionRight.CRAFT, SubscriptionRight.USE_BOAT, SubscriptionRight.ACCESS_TO_ELECTION, SubscriptionRight.USE_COMPANION, SubscriptionRight.EXCHANGE, SubscriptionRight.ACCESS_TO_MARKET_PLACE, SubscriptionRight.ALL_EMOTES, SubscriptionRight.NO_DUNGEON_DAILY_LIMITATION, SubscriptionRight.DUEL, SubscriptionRight.DROP_ITEM, SubscriptionRight.PICK_UP_ITEM, SubscriptionRight.POLITIC_INTERACTION, SubscriptionRight.REROLL_BONUS_XP, SubscriptionRight.VAULT, SubscriptionRight.CONNECTION_QUEUE_PRIORITY)), 
    F2P_FREE(EnumSet.of(SubscriptionRight.PVP_AGGRESSION, SubscriptionRight.CRAFT, SubscriptionRight.USE_BOAT, SubscriptionRight.USE_COMPANION, SubscriptionRight.EXCHANGE, SubscriptionRight.ACCESS_TO_MARKET_PLACE, SubscriptionRight.ALL_EMOTES, SubscriptionRight.NO_DUNGEON_DAILY_LIMITATION, SubscriptionRight.DUEL, SubscriptionRight.DROP_ITEM, SubscriptionRight.PICK_UP_ITEM)), 
    F2P_SUBSCRIBER_RIGHTS(EnumSet.of(SubscriptionRight.FULL_REGEN_WITH_ITEM, SubscriptionRight.PVP_AGGRESSION, SubscriptionRight.CRAFT, SubscriptionRight.USE_BOAT, SubscriptionRight.ACCESS_TO_ELECTION, SubscriptionRight.USE_COMPANION, SubscriptionRight.EXCHANGE, SubscriptionRight.ACCESS_TO_MARKET_PLACE, SubscriptionRight.ALL_EMOTES, SubscriptionRight.NO_DUNGEON_DAILY_LIMITATION, SubscriptionRight.DUEL, SubscriptionRight.DROP_ITEM, SubscriptionRight.PICK_UP_ITEM, SubscriptionRight.POLITIC_INTERACTION, SubscriptionRight.REROLL_BONUS_XP, SubscriptionRight.VAULT, SubscriptionRight.CONNECTION_QUEUE_PRIORITY)), 
    ZH_FREE(EnumSet.of(SubscriptionRight.USE_BOAT, SubscriptionRight.USE_COMPANION, SubscriptionRight.ALL_EMOTES, SubscriptionRight.NO_DUNGEON_DAILY_LIMITATION, SubscriptionRight.DUEL)), 
    ZH_VIP1(EnumSet.of(SubscriptionRight.PVP_AGGRESSION, SubscriptionRight.CRAFT, SubscriptionRight.USE_BOAT, SubscriptionRight.ACCESS_TO_ELECTION, SubscriptionRight.USE_COMPANION, SubscriptionRight.EXCHANGE, SubscriptionRight.ACCESS_TO_MARKET_PLACE, SubscriptionRight.ALL_EMOTES, SubscriptionRight.NO_DUNGEON_DAILY_LIMITATION, SubscriptionRight.DUEL, SubscriptionRight.DROP_ITEM, SubscriptionRight.PICK_UP_ITEM, SubscriptionRight.POLITIC_INTERACTION, SubscriptionRight.CONNECTION_QUEUE_PRIORITY)), 
    ZH_VIP2(EnumSet.of(SubscriptionRight.FULL_REGEN_WITH_ITEM, SubscriptionRight.PVP_AGGRESSION, SubscriptionRight.CRAFT, SubscriptionRight.USE_BOAT, SubscriptionRight.ACCESS_TO_ELECTION, SubscriptionRight.USE_COMPANION, SubscriptionRight.EXCHANGE, SubscriptionRight.ACCESS_TO_MARKET_PLACE, SubscriptionRight.ALL_EMOTES, SubscriptionRight.NO_DUNGEON_DAILY_LIMITATION, SubscriptionRight.DUEL, SubscriptionRight.DROP_ITEM, SubscriptionRight.PICK_UP_ITEM, SubscriptionRight.POLITIC_INTERACTION, SubscriptionRight.CONNECTION_QUEUE_PRIORITY)), 
    ZH_VIP3(EnumSet.of(SubscriptionRight.FULL_REGEN_WITH_ITEM, SubscriptionRight.PVP_AGGRESSION, SubscriptionRight.CRAFT, SubscriptionRight.USE_BOAT, SubscriptionRight.ACCESS_TO_ELECTION, SubscriptionRight.USE_COMPANION, SubscriptionRight.EXCHANGE, SubscriptionRight.ACCESS_TO_MARKET_PLACE, SubscriptionRight.ALL_EMOTES, SubscriptionRight.NO_DUNGEON_DAILY_LIMITATION, SubscriptionRight.DUEL, SubscriptionRight.DROP_ITEM, SubscriptionRight.PICK_UP_ITEM, SubscriptionRight.POLITIC_INTERACTION, SubscriptionRight.CONNECTION_QUEUE_PRIORITY)), 
    ZH_VIP4(EnumSet.of(SubscriptionRight.FULL_REGEN_WITH_ITEM, SubscriptionRight.PVP_AGGRESSION, SubscriptionRight.CRAFT, SubscriptionRight.USE_BOAT, SubscriptionRight.ACCESS_TO_ELECTION, SubscriptionRight.USE_COMPANION, SubscriptionRight.EXCHANGE, SubscriptionRight.ACCESS_TO_MARKET_PLACE, SubscriptionRight.ALL_EMOTES, SubscriptionRight.NO_DUNGEON_DAILY_LIMITATION, SubscriptionRight.DUEL, SubscriptionRight.DROP_ITEM, SubscriptionRight.PICK_UP_ITEM, SubscriptionRight.POLITIC_INTERACTION, SubscriptionRight.CONNECTION_QUEUE_PRIORITY));
    
    private final EnumSet<SubscriptionRight> m_rights;
    
    private SubscriptionRightsSet(final EnumSet<SubscriptionRight> rights) {
        this.m_rights = rights.clone();
    }
    
    public EnumSet<SubscriptionRight> getRights() {
        return this.m_rights.clone();
    }
    
    public static void main(final String... args) {
        for (final SubscriptionRightsSet rightsSet : values()) {
            System.out.println(rightsSet);
            for (final SubscriptionRight right : rightsSet.m_rights) {
                System.out.println("\t" + right.name());
            }
        }
    }
}
