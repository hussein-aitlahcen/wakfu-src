package com.ankamagames.wakfu.common.game.time.calendar.event;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public final class DayNightCycleUpdateEvent extends CyclicCalendarEvent
{
    public DayNightCycleUpdateEvent(final GameDateConst date, final EventPeriod period) {
        super(date, null, period);
    }
    
    @Override
    public void runEvent(final GameCalendar calendar) {
    }
}
