package com.ankamagames.wakfu.common.game.fight.microbotCombination;

import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import org.jetbrains.annotations.*;

public interface MicrobotManager
{
    void handleMicrobotAdded(AbstractFakeFighterEffectArea p0);
    
    void handleMicrobotRemoved(AbstractFakeFighterEffectArea p0);
    
    void setListener(MicrobotCombinationEventListener p0);
    
    void setFightMap(@Nullable FightMap p0);
    
    @Nullable
    MicrobotSet getMicrobotSet(long p0);
}
