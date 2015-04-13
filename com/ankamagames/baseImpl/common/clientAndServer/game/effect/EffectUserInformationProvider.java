package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

public interface EffectUserInformationProvider
{
    EffectUser getEffectUserFromId(long p0);
    
    long getNextFreeEffectUserId(byte p0);
}
