package com.ankamagames.wakfu.client.sound.ambiance2D;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;

public class TimeEventListener implements GameCalendarEventListener
{
    public static final TimeEventListener INSTANCE;
    private int m_lastHour;
    
    public TimeEventListener() {
        super();
        this.m_lastHour = -1;
    }
    
    public void reinit() {
        this.m_lastHour = -1;
    }
    
    @Override
    public void onCalendarEvent(final CalendarEventType eventType, final GameCalendar gameCalendar) {
        if (eventType == CalendarEventType.CALENDAR_UPDATED && gameCalendar.isSynchronized()) {
            final int hours = gameCalendar.getDate().getHours();
            if (hours != this.m_lastHour) {
                if (this.m_lastHour != -1) {
                    WakfuSoundManager.getInstance().onEvent(new TimeSoundEvent(hours));
                }
                this.m_lastHour = hours;
            }
        }
    }
    
    static {
        INSTANCE = new TimeEventListener();
    }
}
