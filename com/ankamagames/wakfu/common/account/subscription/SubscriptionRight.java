package com.ankamagames.wakfu.common.account.subscription;

public enum SubscriptionRight
{
    UNKNOWN(0), 
    FULL_REGEN_WITH_ITEM(1), 
    PVP_AGGRESSION(2), 
    CRAFT(3), 
    USE_BOAT(4), 
    ACCESS_TO_ELECTION(5), 
    USE_COMPANION(6), 
    EXCHANGE(7), 
    ACCESS_TO_MARKET_PLACE(8), 
    ALL_EMOTES(9), 
    NO_DUNGEON_DAILY_LIMITATION(10), 
    DUEL(11), 
    DROP_ITEM(12), 
    PICK_UP_ITEM(13), 
    POLITIC_INTERACTION(14), 
    REROLL_BONUS_XP(15), 
    VAULT(16), 
    CONNECTION_QUEUE_PRIORITY(17);
    
    public final int id;
    
    private SubscriptionRight(final int idx) {
        this.id = idx;
    }
    
    public static SubscriptionRight fromId(final int idx) {
        for (final SubscriptionRight level : values()) {
            if (level.id == idx) {
                return level;
            }
        }
        return SubscriptionRight.UNKNOWN;
    }
    
    @Override
    public String toString() {
        return "SubscriptionRight{name=" + this.name() + ", m_id=" + this.id + '}';
    }
}
