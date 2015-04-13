package com.ankamagames.wakfu.common.game.almanach;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.*;

public class AlmanachHelper
{
    private static final Calendar CALENDAR;
    
    public static short dateToHashcode(final Date date) {
        synchronized (AlmanachHelper.CALENDAR) {
            AlmanachHelper.CALENDAR.setTime(date);
            final byte day = (byte)AlmanachHelper.CALENDAR.get(5);
            final byte month = (byte)AlmanachHelper.CALENDAR.get(2);
            return MathHelper.getShortFromTwoBytes(day, month);
        }
    }
    
    public static short dateToHashcode(final GameDateConst date) {
        final byte day = (byte)date.getDay();
        final byte month = (byte)(date.getMonth() - 1);
        return MathHelper.getShortFromTwoBytes(day, month);
    }
    
    static {
        CALENDAR = new GregorianCalendar(GameDate.DEFAULT_TZ);
    }
}
