package com.ankamagames.wakfu.common.game.protector;

public interface ProtectorManagerObserver<P extends ProtectorBase>
{
    void onProtectorRegistered(P p0);
    
    void onProtectorUnregistered(P p0);
}
