package com.ankamagames.wakfu.common.game.spell;

public enum CastValidity
{
    OK, 
    OK_BUT_NO_EFFECT_ON_TARGET, 
    INVALID_CONTAINER, 
    INVALID_LINE_OF_SIGHT, 
    INVALID_TARGET_CELL, 
    INVALID_RANGE, 
    NOT_ENOUGH_AP, 
    NOT_ENOUGH_FP, 
    NOT_ENOUGH_MP, 
    NOT_ENOUGH_CHRAGE, 
    TOO_MUCH_CASTS_ON_THIS_TARGET, 
    TOO_MUCH_CASTS_THIS_TURN, 
    LAST_CAST_TOO_RECENT, 
    CONTAINER_UNKNOWN, 
    CELL_NOT_FREE, 
    CELLS_NOT_ALIGNED, 
    CAST_CRITERIONS_NOT_VALID, 
    PASSIVE, 
    NEED_EMPTY_CELLS, 
    CANT_CAST_BETWEEN_TURN, 
    CANNOT_USE_ITEM_WHEN_CARRYING, 
    CANNOT_USE_ITEM, 
    LOCKED_SPELL, 
    CANNOT_CAST_SPELL, 
    CANNOT_EVALUATE;
    
    public boolean isValid() {
        return this == CastValidity.OK || this == CastValidity.OK_BUT_NO_EFFECT_ON_TARGET;
    }
}
