package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public interface WakfuFightEffectContextInterface extends EffectContext<WakfuEffect>
{
    int getFightId();
    
    void onEffectAreaGoesOffPlay(AbstractEffectArea p0);
}
