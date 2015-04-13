package com.ankamagames.baseImpl.common.clientAndServer.game.effectArea;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public interface AreaOwnerProvider
{
    EffectUser getOwner();
    
    int getType();
}
