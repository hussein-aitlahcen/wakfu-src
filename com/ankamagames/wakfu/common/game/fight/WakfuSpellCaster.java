package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;

public interface WakfuSpellCaster extends SpellCaster
{
    byte computeApCost();
    
    byte computeWpCost();
    
    byte computeMpCost();
    
    AbstractSpellLevel getSpellLevel();
}
