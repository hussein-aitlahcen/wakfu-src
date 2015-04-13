package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import java.text.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class HourCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final WakfuGameCalendar calendar = WakfuGameCalendar.getInstance();
        final DecimalFormat digitFormatter = new DecimalFormat("00");
        final GameDateConst date = calendar.getDate();
        final StringBuilder sb = new StringBuilder().append(calendar.getSeason().getName()).append(", ").append(digitFormatter.format(date.getDay())).append("/").append(digitFormatter.format(date.getMonth())).append("/").append(date.getYear()).append(" ").append(digitFormatter.format(date.getHours())).append(":").append(digitFormatter.format(date.getMinutes()));
        manager.trace(sb.toString());
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
