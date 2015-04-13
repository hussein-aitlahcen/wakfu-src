package com.ankamagames.wakfu.common.game.eventsCalendar;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public interface CalendarEvent
{
    GameDateConst getStartDate();
    
    String getTitle();
    
    String getDescription();
    
    long getOwnerId();
    
    byte getType();
}
