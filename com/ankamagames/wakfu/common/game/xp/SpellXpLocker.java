package com.ankamagames.wakfu.common.game.xp;

public interface SpellXpLocker
{
    boolean hasLockedSpell();
    
    int getLockedSpellId();
    
    void registerLockedSpellId(int p0);
    
    void unregisterLockedSpell();
}
