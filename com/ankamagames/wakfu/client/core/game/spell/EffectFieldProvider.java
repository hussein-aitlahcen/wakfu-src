package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.framework.reflect.*;

public interface EffectFieldProvider extends FieldProvider
{
    short getBaseId();
    
    short getLevel();
    
    short getDisplayedMaxLevel();
    
    void forceLevel(short p0);
}
