package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.wakfu.common.game.havenWorld.exception.*;

public enum HavenWorldError
{
    NO_ERROR, 
    NO_ACTIONS, 
    ACTION_IN_PROGRESS, 
    WORLD_NOT_FOUND, 
    WORLD_ALREADY_HAVE_GUILD, 
    BAD_RESOURCES_AMOUNT, 
    GUILD_ALREADY_HAVE_WORLD, 
    PLAYER_NOT_FOUND, 
    PLAYER_HAVE_NO_GUILD, 
    PLAYER_HAVE_NO_RIGHTS, 
    PLAYER_HAVE_WRONG_GUILD, 
    NO_DEFINITION, 
    WORKERS_NEEDED, 
    MONEY_NEEDED, 
    BUILDING_NEEDED, 
    RESOURCES_NEEDED, 
    BUILDING_DEFINITION_NOT_FOUND, 
    BUILDING_NOT_FOUND, 
    BUILDING_CANT_BE_DESTROYED, 
    BUILDING_ALREADY_EXIST, 
    EVOLUTION_NOT_FOUND, 
    TIMED_EVOLUTION_NOT_FINISHED, 
    BUILDING_WRONG_ITEM, 
    BUILDING_EQUIP_MISSING_ITEM, 
    BUILDING_EQUIP_ITEM_ERROR, 
    BUILDING_ELEMENT_NOT_FOUND, 
    BUILDING_ELEMENT_ALREADY_EXIST, 
    BUILDING_ELEMENT_REMAINING, 
    BUILDING_ELEMENT_UNVALID_POSITION, 
    BUILDING_ELEMENT_COULD_NOT_BE_SPAWNED, 
    INTERACTIVE_ELEMENTS_PRESENT, 
    PARTITION_NOT_FOUND, 
    PARTITION_ALREADY_EXIST, 
    TEMP_TURTLE, 
    HAS_ANOTHER_BID, 
    BAD_BID_AMOUNT, 
    BDD_ERROR, 
    BAD_RESOURCES_NUMBER, 
    MAX_QUANTITY, 
    TOO_MUCH_MINERALS_PATCH;
    
    public static HavenWorldError valueOf(final int idx) {
        final HavenWorldError[] values = values();
        if (idx < 0 || idx >= values.length) {
            throw new HavenWorldRuntimeException("[HAVEN_WORLD] Erreur " + idx + " inconnue");
        }
        return values[idx];
    }
}
