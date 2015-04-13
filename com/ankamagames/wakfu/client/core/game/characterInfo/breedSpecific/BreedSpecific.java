package com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific;

import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public interface BreedSpecific
{
    void clear();
    
    void onSpecialFighterEvent(SpecialEvent p0);
    
    void onBarrelCarried(BasicEffectArea p0);
    
    void onBarrelUncarried(BasicEffectArea p0);
}
