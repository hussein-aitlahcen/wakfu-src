package com.ankamagames.wakfu.common.game.xp;

public enum SpellLockValidity
{
    VALID, 
    CAP_REACHED_BUT_REMAINING_XP, 
    CAP_NOT_REACHED, 
    BAD_CHARACTER_STATE, 
    SPELL_UNKNOWN, 
    SPELL_HAS_NO_XP, 
    UNAUTHORIZED_SPELL;
}
