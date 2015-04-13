package com.ankamagames.wakfu.common.game.fighter;

public interface FightActionHistory
{
    boolean hasCastSpell(int p0);
    
    int getSpellCastCount(int p0);
    
    boolean hasMoved();
    
    boolean hasCastSpell();
}
