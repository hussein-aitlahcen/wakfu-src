package com.ankamagames.wakfu.client.core.game.almanach.zodiac;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.*;

public class AlmanachSOAPHelper
{
    private static final GregorianCalendar CALENDAR;
    
    public static GameDate convertToGameDate(final long time) {
        AlmanachSOAPHelper.CALENDAR.setTime(new Date(time));
        return new GameDate(0, 0, 0, AlmanachSOAPHelper.CALENDAR.get(5), AlmanachSOAPHelper.CALENDAR.get(2) + 1, AlmanachSOAPHelper.CALENDAR.get(1));
    }
    
    static {
        CALENDAR = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
    }
}
