package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public final class NewDayEvent extends CyclicCalendarEvent
{
    public NewDayEvent(final GameDateConst date) {
        super(date, null, EventPeriod.DAILY);
    }
    
    @Override
    public void runEvent(final GameCalendar calendar) {
    }
}
