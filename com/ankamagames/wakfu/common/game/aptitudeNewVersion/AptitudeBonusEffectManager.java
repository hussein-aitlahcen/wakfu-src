package com.ankamagames.wakfu.common.game.aptitudeNewVersion;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class AptitudeBonusEffectManager extends AbstractEffectManager<WakfuEffect>
{
    public static final AptitudeBonusEffectManager INSTANCE;
    
    static {
        INSTANCE = new AptitudeBonusEffectManager();
    }
}
