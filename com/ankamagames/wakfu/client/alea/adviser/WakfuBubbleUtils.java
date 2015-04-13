package com.ankamagames.wakfu.client.alea.adviser;

import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;

public final class WakfuBubbleUtils
{
    public static String getNewWakfuBubbleId() {
        return new StringBuffer("bubbleDialog").append(WakfuBubbleWidget.UID++).toString();
    }
    
    public static WakfuBubbleWidget loadBubble(final String widgetId) {
        return (WakfuBubbleWidget)Xulor.getInstance().load(widgetId, Dialogs.getDialogPath("bubbleDialog"), 73744L, (short)9999);
    }
    
    public static ImmutableWakfuBubbleWidget loadPetBubble() {
        return (ImmutableWakfuBubbleWidget)Xulor.getInstance().load("petBubbleDialog", Dialogs.getDialogPath("petBubbleDialog"), 139280L, (short)9999);
    }
}
