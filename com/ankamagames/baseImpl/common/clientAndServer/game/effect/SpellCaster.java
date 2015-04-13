package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

public interface SpellCaster
{
    boolean isSpellTunnelable();
    
    int getSpellElement();
    
    int getSpellId();
    
    short getLevelOfSpell();
    
    EffectContainer getSpellLevel();
}
