package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

public class TableTurnStartEvent implements TimeEvent
{
    private static final TableTurnStartEvent instance;
    
    public static TableTurnStartEvent getInstance() {
        return TableTurnStartEvent.instance;
    }
    
    @Override
    public void sendTo(final TimeEventHandler handler) {
        handler.handleTableTurnStartEvent(this);
    }
    
    static {
        instance = new TableTurnStartEvent();
    }
}
