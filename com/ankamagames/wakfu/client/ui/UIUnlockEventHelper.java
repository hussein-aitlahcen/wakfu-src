package com.ankamagames.wakfu.client.ui;

import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.xulor2.util.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event.*;

public class UIUnlockEventHelper
{
    public static void loadEvents() {
        loadAlmanaxEvent();
    }
    
    private static void loadAlmanaxEvent() {
        PropertiesProvider.getInstance().setPropertyValue("almanachUnlocked", false);
        final TextWidgetFormater sb = new TextWidgetFormater();
        final String almanaxTooltip = WakfuTranslator.getInstance().getString("openCloseAlmanachDialog");
        sb.append(almanaxTooltip);
        sb.newLine();
        sb.append(WakfuTranslator.getInstance().getString("unlocked.the", WakfuTranslator.getInstance().formatDateShort(ActivationConstants.ALMANAX_UNLOCK_DATE)));
        PropertiesProvider.getInstance().setPropertyValue("almanachTooltipDescription", sb.toString());
        WakfuGameCalendar.getInstance().addEvent(new PropertySetEvent(ActivationConstants.ALMANAX_UNLOCK_DATE, "almanachUnlocked", true));
        WakfuGameCalendar.getInstance().addEvent(new PropertySetEvent(ActivationConstants.ALMANAX_UNLOCK_DATE, "almanachTooltipDescription", almanaxTooltip));
    }
}
