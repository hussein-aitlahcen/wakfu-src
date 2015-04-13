package com.ankamagames.wakfu.common.game.time.calendar.event;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public final class NewDayNightCycleEvent extends CyclicCalendarEvent
{
    public NewDayNightCycleEvent(final GameDateConst date, final EventPeriod period) {
        super(date, GameDate.NULL_DATE, period);
    }
    
    @Override
    public void runEvent(final GameCalendar calendar) {
    }
}
