package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

public class TableTurnEndEvent implements TimeEvent
{
    private static final TableTurnEndEvent instance;
    
    public static TableTurnEndEvent getInstance() {
        return TableTurnEndEvent.instance;
    }
    
    @Override
    public void sendTo(final TimeEventHandler handler) {
        handler.handleTableTurnEndEvent(this);
    }
    
    static {
        instance = new TableTurnEndEvent();
    }
}
