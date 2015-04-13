package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

public class PlacementEndEvent implements TimeEvent
{
    private static final PlacementEndEvent instance;
    
    public static PlacementEndEvent getInstance() {
        return PlacementEndEvent.instance;
    }
    
    @Override
    public void sendTo(final TimeEventHandler handler) {
        handler.handlePlacementEndEvent(this);
    }
    
    static {
        instance = new PlacementEndEvent();
    }
}
