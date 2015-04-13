package com.ankamagames.wakfu.common.game.fight.creation;

public enum FightCreationError
{
    NO_ERROR, 
    TARGETED_FIGHTER_NULL, 
    FIGHTER_CANT_STAKE, 
    NPC_WITH_GROUP_NULL, 
    NPC_GROUP_BUSY, 
    CHALLENGE_NPC_RESTRICTION, 
    FIGHT_MODEL_NULL, 
    INITIATING_FIGHTER_NULL, 
    EXCEPTION_RAISED, 
    IMPOSSIBLE_TO_READ_FIGHT_PARAMETERS, 
    IMPOSSIBLE_TO_CREATE_BATTLEGROUND, 
    NOT_SUBSCRIBER, 
    FIGHTER_NULL, 
    TARGET_IS_DEAD, 
    TARGET_IS_STAFF, 
    TARGET_IS_ON_FIGHT, 
    TARGET_IS_BUSY, 
    ZONE_IS_UNDER_MODERATION, 
    INSTANCE_DOES_NOT_ALLOW_PVE, 
    INSTANCE_DOES_NOT_ALLOW_DUEL, 
    INSTANCE_DOES_NOT_ALLOW_PVP, 
    COORDINATES_ERROR, 
    NOT_ENEMY_PROTECTOR, 
    NO_TERRITORY, 
    NO_PATH_BETWEN_FIGHTERS, 
    FIGHTER_CANT_DUEL, 
    TARGET_CANT_DUEL, 
    FIGHTER_CANT_AGRO, 
    FIGHTER_NO_NATION, 
    TARGET_CANT_BE_AGRO, 
    PROTECTOR_FIGHT_ALREADY_ENDED, 
    PROTECTOR_FIGHT_FULL, 
    PROTECTOR_FIGHT_PLACEMENT_PHASE_ENDED;
    
    public static byte indexOf(final FightCreationError error) {
        final FightCreationError[] values = values();
        for (byte i = 0; i < values.length; ++i) {
            final FightCreationError value = values[i];
            if (error == value) {
                return i;
            }
        }
        return -1;
    }
    
    public static FightCreationError valueOf(final byte index) {
        if (index < 0 || index >= values().length) {
            return null;
        }
        return values()[index];
    }
}
