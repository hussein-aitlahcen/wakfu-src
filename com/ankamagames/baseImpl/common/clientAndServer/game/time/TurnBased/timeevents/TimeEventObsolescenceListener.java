package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

public interface TimeEventObsolescenceListener<T extends TimeEvent>
{
    void onBeingObsolete(T p0);
}
