package com.ankamagames.wakfu.common.game.protector.event;

public interface ProtectorEventListener<P extends ProtectorEvent>
{
    void onProtectorEvent(P p0);
}
