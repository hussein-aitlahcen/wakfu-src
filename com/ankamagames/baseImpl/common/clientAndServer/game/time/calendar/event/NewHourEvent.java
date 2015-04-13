package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public final class NewHourEvent extends CyclicCalendarEvent
{
    public NewHourEvent(final GameDateConst date) {
        super(date, null, EventPeriod.HOURLY);
    }
    
    @Override
    public void runEvent(final GameCalendar calendar) {
    }
}
